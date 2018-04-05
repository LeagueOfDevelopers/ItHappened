using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Linq;
using ItHappenedDomain.Infrastructure;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Driver;

namespace ItHappenedDomain.Domain
{
  [BsonIgnoreExtraElements]
  public class User
  {
    public User(string userId, string pictureUrl, DateTimeOffset nicknameDateOfChange)
    {
      NicknameDateOfChange = nicknameDateOfChange;
      PictureUrl = pictureUrl;
      UserId = userId;
      UserNickname = userId;
      TrackingCollection = new TrackingCollection();
    }

    public User(OldUserModel user)
    {
      _id = user._id;
      UserId = user.UserId;
      UserNickname = user.UserNickname;
      PictureUrl = user.PictureUrl;
      NicknameDateOfChange = user.NicknameDateOfChange;
      TrackingCollection = new TrackingCollection(user.TrackingCollection);
    }

    public List<Tracking> ChangeTrackingCollection(List<Tracking> trackingCollection)
    {
      return TrackingCollection.ChangeTrackingCollection(trackingCollection);
    }

    
    public ObjectId _id { get; set; }
    public string UserId { set; get; }
    public string UserNickname { set; get; }
    public string PictureUrl { set; get; }
    public DateTimeOffset NicknameDateOfChange { get; set; }
    public TrackingCollection TrackingCollection { get; set; }
  }
}
