using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  public class UserList
  {
    public UserList(Dictionary<Guid, User> userCollection)
    {
      this.userCollection = userCollection;
    }

    public List<Tracking> ChangeTrackingCollection(Guid userId, List<Tracking> trackingCollection)
    {
      return userCollection[userId].ChangeTrackingCollection(trackingCollection);
    }

    private Dictionary<Guid, User> userCollection;

  }
}
