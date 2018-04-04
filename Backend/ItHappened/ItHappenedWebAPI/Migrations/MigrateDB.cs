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
      var users = collection.Find(u => true).ToList();
      
      var newUserList = users?.Select(user => new User(user)).ToList();

      db.DropCollection("Users");

      var newCollection = db.GetCollection<User>("Users");
      newCollection.InsertMany(newUserList);
    }

    private IMongoDatabase db;
  }
}
