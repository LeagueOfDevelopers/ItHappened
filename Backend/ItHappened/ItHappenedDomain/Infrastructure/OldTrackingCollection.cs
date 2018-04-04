using System;
using System.Collections.Generic;
using System.Text;
using ItHappenedDomain.Domain;

namespace ItHappenedDomain.Infrastructure
{
  public class OldTrackingCollection
  {
    public OldTrackingCollection()
    {
      TrackingList = new List<OldTrackingModel>();
    }
    public List<OldTrackingModel> TrackingList { get; set; }
  }
}
