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
    public User(Guid userId, List<Tracking> trackingCollection)
    {
      UserId = userId;
      _trackingCollection = new TrackingRepository(trackingCollection);
    }

    public List<Tracking> ChangeTrackingCollection(List<Tracking> trackingCollection)
    {
      return _trackingCollection.ChangeTrackingCollection(trackingCollection);
    }

    public Guid UserId { private set; get; }
    private TrackingRepository _trackingCollection;
  }
}
