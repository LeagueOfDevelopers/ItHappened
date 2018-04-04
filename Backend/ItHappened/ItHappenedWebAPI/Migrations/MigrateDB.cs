using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using MongoDB.Driver;

namespace ItHappenedWebAPI.Migrations
{
  public class MigrateDB
  {
    public MigrateDB(IMongoDatabase db)
    {
      this.db = db;
    }

    public void Migrate()
    {
      var collection = db.GetCollection<OldUserModel>("Users");
      var users = collection.FindAsync(a => true);
      try
      {
        var result = users.Result;
        if (result != null)
        {
          List<OldUserModel> userList = new List<OldUserModel>(result.ToList().FindAll(a => true));
          List<User> newUserList = new List<User>();

          foreach (var user in userList)
          {
            User newUser = new User(user);
            newUserList.Add(newUser);
          }

          db.DropCollection("Users");

          var newCollection = db.GetCollection<User>("Users");
          newCollection.InsertMany(newUserList);
        }
      }
      catch (Exception e)
      {
      }
    }

    private IMongoDatabase db;
  }
}
