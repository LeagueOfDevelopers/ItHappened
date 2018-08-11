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
    public double? scale { set; get; }
    public Rating rating { set; get; }
    public string comment { set; get; }
    public DateTimeOffset dateOfChange { set; get; }
    public bool isDeleted { set; get; }
    private double longitude;
    private double lotitude;

    public double Lotitude
    {
      get => lotitude;
      set => lotitude = value;
    }
    public double Longitude
    {
      get => longitude;
      set => longitude = value;
    }
    

    public Event(string eventId, string trackingId,
      DateTimeOffset eventDate, 
      double? scale, Rating rating, string comment, 
      DateTimeOffset dateOfChange, bool isDeleted,
      double longitude, double lotitude)
    {
      this.eventId = eventId;
      this.trackingId = trackingId;
      this.eventDate = eventDate;
      this.scale = scale;
      this.rating = rating;
      this.comment = comment;
      this.dateOfChange = dateOfChange;
      this.isDeleted = isDeleted;
      this.lotitude = lotitude;
      this.longitude = longitude;
    }
  }
}
