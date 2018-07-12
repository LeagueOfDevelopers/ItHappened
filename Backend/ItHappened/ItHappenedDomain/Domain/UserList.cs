using System;
using System.Collections.Generic;
using ItHappenedDomain.AuthServices;
using ItHappenedDomain.Domain.Exceptions;
using ItHappenedDomain.Models;
using MongoDB.Driver;

namespace ItHappenedDomain.Domain
{
  public class UserList
  {
    public UserList(IMongoDatabase db)
    {
      this.db = db;
    }

    public RegistrationResponse SignUp(string idToken)
    {
      GoogleResponseJson response = new GoogleResponseJson();
      GoogleIdTokenVerifyer verifyer = new GoogleIdTokenVerifyer();
      response = verifyer.Verify(idToken);
      if (response.IsEmpty)
        return null;

      if (response.email == null)
        return null;

      var collection = db.GetCollection<User>("Users");

      var user = collection.Find(us => us.UserId == response.email);

      if (user.Count() != 0)
      {
        return new RegistrationResponse
        {
          PicUrl = user.First().PictureUrl,
          NicknameDateOfChange = user.First().NicknameDateOfChange,
          UserId = user.First().UserId,
          UserNickname = user.First().UserNickname
        };
      }
      DateTimeOffset date = DateTimeOffset.UtcNow;
      User newUser = new User(response.email, response.picture, date);
      collection.InsertOne(newUser);
      RegistrationResponse toReturn = new RegistrationResponse
      {
        NicknameDateOfChange = date,
        PicUrl = response.picture,
        UserId = response.email,
        UserNickname = response.email
      };
      return toReturn;
    }

    public SynchronisationRequest Synchronisation(string userId, DateTimeOffset NicknameDateOfChange, string UserNickname,
      List<Tracking> trackingCollection)
    {
      var collection = db.GetCollection<User>("Users");
      var user = collection.Find(us => us.UserId == userId);
      if (user.Count() == 0)
        throw new UserNotFoundException();
      if (user.First().NicknameDateOfChange < NicknameDateOfChange)
      {
        user.First().NicknameDateOfChange = NicknameDateOfChange;
        user.First().UserNickname = UserNickname;
      }

      List<Tracking> collectionToReturn = user.First().ChangeTrackingCollection(trackingCollection);

      var filter = Builders<User>.Filter.Eq(us => us.UserId, user.First().UserId);
      collection.ReplaceOne(filter, user.First());

      SynchronisationRequest toReturn = new SynchronisationRequest()
      {
        NicknameDateOfChange = user.First().NicknameDateOfChange,
        UserNickname = user.First().UserNickname,
        trackingCollection = collectionToReturn
      };
      return toReturn;
    }

    public bool UserIsExists(string userId)
    {
      var collection = db.GetCollection<User>("Users");
      var user = collection.Find(us => us.UserId == userId);

      return user.Count() != 0;
    }

    private IMongoDatabase db;

  }
}
