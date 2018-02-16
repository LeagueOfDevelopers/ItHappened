using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Linq;
using ItHappenedDomain.Infrastructure;

namespace ItHappenedDomain.Domain
{
  public class User
  {
    public User(string userId, string pictureUrl, DateTimeOffset nicknameDateOfChange)
    {
      NicknameDateOfChange = nicknameDateOfChange;
      PictureUrl = pictureUrl;
      UserId = userId;
      UserNickname = userId;
      _trackingCollection = new TrackingCollection();
    }

    public List<Tracking> ChangeTrackingCollection(List<Tracking> trackingCollection)
    {
      return _trackingCollection.ChangeTrackingCollection(trackingCollection);
    }

    public string UserId { set; get; }
    public string UserNickname { set; get; }
    public string PictureUrl { set; get; }
    public DateTimeOffset NicknameDateOfChange { get; set; }
    private TrackingCollection _trackingCollection;
  }
}
