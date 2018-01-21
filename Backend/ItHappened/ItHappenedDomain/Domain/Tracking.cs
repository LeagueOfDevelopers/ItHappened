using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  public class Tracking
  {
    private String trackingName;
    public Guid trackingId { private set; get; }
    private DateTimeOffset trackingDate;
    private TrackingCustomization scale;
    private TrackingCustomization rating;
    private TrackingCustomization comment;
    public DateTimeOffset dateOfChange { private set; get; }
    private bool isDeleted;
    
    public List<Event> EventCollection {get; set;}
  }
}
