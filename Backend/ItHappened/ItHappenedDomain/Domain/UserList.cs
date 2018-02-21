using System;
using System.Collections.Generic;
using System.Linq;
using ItHappenedDomain.AuthServices;
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
      var collection = db.GetCollection<User>("Users");
      var iUser = collection.FindSync(us => us.UserId == response.email).Current;
      if (iUser.First() != null)
      {
        User user = iUser.First();
        return new RegistrationResponse
        {
          PicUrl = user.PictureUrl,
          NicknameDateOfChange = user.NicknameDateOfChange,
          UserId = user.UserId,
          UserNickname = user.UserNickname
        };
      }
      DateTimeOffset date = DateTimeOffset.Now;
      User newUser = new User(response.email, response.picture, date);
      collection.InsertOne(newUser);
      RegistrationResponse toReturn = new RegistrationResponse
      {
        PicUrl = response.picture,
        UserId = response.email,
        UserNickname = response.email
      };
      return toReturn;
    }

    public RegistrationResponse Reg(string id)
    {
      var collection = db.GetCollection<User>("Users");
      var iUser = collection.FindSync(us => us.UserId == id);
      if (iUser.Current != null)
      {
        User user = iUser.FirstAsync().Result;
        return new RegistrationResponse()
        {
          PicUrl = user.PictureUrl,
          NicknameDateOfChange = user.NicknameDateOfChange,
          UserId = user.UserId,
          UserNickname = user.UserNickname
        };
      }
      User newUser = new User(id, null, DateTimeOffset.Now);
      collection.InsertOne(newUser);
      RegistrationResponse toReturn = new RegistrationResponse()
      {
        PicUrl = null,
        UserId = newUser.UserId,
        UserNickname = newUser.UserNickname
      };
      return toReturn;
    }

    public SynchronisationRequest Synchronisation(string userId, SynchronisationRequest request)
    {
      var collection = db.GetCollection<User>("Users");
      var user = collection.FindSync(us => us.UserId == userId).FirstAsync().Result;
      if (user.NicknameDateOfChange < request.NicknameDateOfChange)
      {
        user.NicknameDateOfChange = request.NicknameDateOfChange;
        user.UserNickname = request.UserNickname;
      }

      List<Tracking> collectionToReturn = user.ChangeTrackingCollection(request.TrackingCollection);

      var filter = Builders<User>.Filter.Eq(us => us.UserId, user.UserId);
      collection.ReplaceOneAsync(filter, user);

      SynchronisationRequest toReturn = new SynchronisationRequest()
      {
        NicknameDateOfChange = user.NicknameDateOfChange,
        UserNickname = user.UserNickname,
        TrackingCollection = collectionToReturn
      };
      return toReturn;
    }


    private IMongoDatabase db;

  }
}
