using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  public class Tracking
  {
    private String trackingName;
    public Guid TrackingId { private set; get; }
    private DateTimeOffset trackingDate;
    private TrackingCustomization scale;
    private TrackingCustomization rating;
    private TrackingCustomization comment;
    public DateTimeOffset DateOfChange { private set; get; }
    public ItemStatus Status { private set; get; }
    
    public List<Event> EventCollection {get; set;}
  }
}
