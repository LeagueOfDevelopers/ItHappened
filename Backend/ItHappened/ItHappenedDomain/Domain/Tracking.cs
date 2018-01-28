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

    public Tracking(string trackingName, Guid trackingId, DateTimeOffset trackingDate, TrackingCustomization scale, TrackingCustomization rating, TrackingCustomization comment, DateTimeOffset dateOfChange, bool isDeleted, List<Event> eventCollection)
    {
      this.trackingName = trackingName;
      this.trackingId = trackingId;
      this.trackingDate = trackingDate;
      this.scale = scale;
      this.rating = rating;
      this.comment = comment;
      this.dateOfChange = dateOfChange;
      this.isDeleted = isDeleted;
      EventCollection = eventCollection;
    }

    public List<Event> EventCollection {get; set;}
  }
}
