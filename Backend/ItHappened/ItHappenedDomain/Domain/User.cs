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
    public User(string userId)
    {
      UserId = userId;
      _trackingCollection = new TrackingCollection();
    }

    public List<Tracking> ChangeTrackingCollection(List<Tracking> trackingCollection)
    {
      return _trackingCollection.ChangeTrackingCollection(trackingCollection);
    }

    public string UserId { private set; get; }
    private TrackingCollection _trackingCollection;
  }
}
