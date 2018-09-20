using System;
using System.Collections.Generic;
using ItHappenedDomain.AuthServices;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using MongoDB.Driver;

namespace ItHappenedDomain.Infrastructure
{
  public class UserRepository : IUserRepository
  {
    public UserRepository(IMongoDatabase mongoDatabase)
    {
      _mongoDatabase = mongoDatabase;
    }

    public RegistrationResponse AddOrFindUser(GoogleResponseJson response)
    {
      var collection = _mongoDatabase.GetCollection<User>("Users");

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
      var date = DateTimeOffset.UtcNow;
      var newUser = new User(response.email, response.picture, date);
      collection.InsertOne(newUser);
      var toReturn = new RegistrationResponse
      {
        NicknameDateOfChange = date,
        PicUrl = response.picture,
        UserId = response.email,
        UserNickname = response.email
      };
      return toReturn;
    }

    public bool FindUser(string userId)
    {
      var collection = GetMongoCollection();
      var user = collection.Find(us => us.UserId == userId);

      return user.Count() != 0;
    }

    public User GetUserData(string userId)
    {
      
      var collection = GetMongoCollection();
      var user = collection.Find(us => us.UserId == userId);

      if (user.Count() != 0)
        return user.First();
      return null;
    }

    public void SaveUserData(User user)
    {
      var collection = GetMongoCollection();

      var filter = Builders<User>.Filter.Eq(us => us.UserId, user.UserId);
      collection.ReplaceOne(filter, user);
    }

    private IMongoCollection<User> GetMongoCollection()
    {
      return _mongoDatabase.GetCollection<User>("Users");
    }

    private readonly IMongoDatabase _mongoDatabase;
  }
}