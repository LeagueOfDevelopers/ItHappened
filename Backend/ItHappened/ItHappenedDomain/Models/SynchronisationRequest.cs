using System;
using System.Collections.Generic;
using System.Text;
using ItHappenedDomain.Domain;

namespace ItHappenedDomain.Models
{
  public class SynchronisationRequest
  {
    public string UserNickname { get; set; }
    public DateTimeOffset NicknameDateOfChange { get; set; }
    public List<Tracking> TrackingCollection { get; set; }
  }
}
