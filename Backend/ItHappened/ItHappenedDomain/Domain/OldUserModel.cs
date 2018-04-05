using System;
using System.Collections.Generic;
using System.Text;
using ItHappenedDomain.Infrastructure;
using MongoDB.Bson;

namespace ItHappenedDomain.Domain
{
  public class OldUserModel
  {
    public OldUserModel(string userId, string pictureUrl, DateTimeOffset nicknameDateOfChange)
    {
      NicknameDateOfChange = nicknameDateOfChange;
      PictureUrl = pictureUrl;
      UserId = userId;
      UserNickname = userId;
      TrackingCollection = new OldTrackingCollection();
    }



    public ObjectId _id { get; set; }
    public string UserId { set; get; }
    public string UserNickname { set; get; }
    public string PictureUrl { set; get; }
    public DateTimeOffset NicknameDateOfChange { get; set; }
    public OldTrackingCollection TrackingCollection { get; set; }
  }
}
