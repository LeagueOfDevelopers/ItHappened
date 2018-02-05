using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  public class Event
  {
    public string eventId { set; get; }
    public string trackingId { set; get; }
    public DateTimeOffset eventDate { set; get; }
    public Double scale { set; get; }
    public Rating rating { set; get; }
    public String comment { set; get; }
    public DateTimeOffset dateOfChange { set; get; }
    public bool isDeleted { set; get; }

    public Event(string eventId, string trackingId, 
      DateTimeOffset eventDate, 
      double scale, Rating rating, string comment, 
      DateTimeOffset dateOfChange, bool isDeleted)
    {
      this.eventId = eventId;
      this.trackingId = trackingId;
      this.eventDate = eventDate;
      this.scale = scale;
      this.rating = rating;
      this.comment = comment;
      this.dateOfChange = dateOfChange;
      this.isDeleted = isDeleted;
    }
  }
}
