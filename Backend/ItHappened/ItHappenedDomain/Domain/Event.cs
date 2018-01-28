using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  public class Event
  {
    public Guid EventId { private set; get; }
    public Guid trackingId { private set; get; }
    private DateTimeOffset eventDate;
    private Double scale;
    private Rating rating;
    private String comment;
    public DateTimeOffset DateOfChange { private set; get; }
    private bool isDeleted;

    public Event(Guid eventId, Guid trackingId, DateTimeOffset eventDate, double scale, Rating rating, string comment, DateTimeOffset dateOfChange, bool isDeleted)
    {
      EventId = eventId;
      this.trackingId = trackingId;
      this.eventDate = eventDate;
      this.scale = scale;
      this.rating = rating;
      this.comment = comment;
      DateOfChange = dateOfChange;
      this.isDeleted = isDeleted;
    }
  }
}
