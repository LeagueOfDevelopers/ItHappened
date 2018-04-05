using System;
using System.Linq;
using ItHappenedDomain.Domain;
using MongoDB.Driver;

namespace Migrator
{
  public class Program
  {
   public static void Main(string[] args)
    {
      var connectionString = "mongodb://localhost";
      var client = new MongoClient(connectionString);
      var db = client.GetDatabase("ItHappenedDB");
      var migrator = new MigrateDb(db);
      migrator.Migrate();
    }
  }

  public class MigrateDb
  {
    public MigrateDb(IMongoDatabase db)
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