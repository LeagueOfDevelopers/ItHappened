﻿using System;
using System.Collections.Generic;
using System.Text;
using MongoDB.Bson.Serialization.Attributes;

namespace ItHappenedDomain.Domain
{
  public class Tracking
  {

    public Tracking(string trackingName, string trackingId, 
      DateTimeOffset trackingDate, 
      string scale, 
      string rating, 
      string comment,
      DateTimeOffset dateOfChange, bool isDeleted, 
      List<Event> eventCollection,
      string scaleName)
    {
      this.scaleName = scaleName;
      this.trackingName = trackingName;
      this.trackingId = trackingId;
      this.trackingDate = trackingDate;
      this.scale = scale;
      this.rating = rating;
      this.comment = comment;
      this.dateOfChange = dateOfChange;
      this.isDeleted = isDeleted;
      this.EventCollection = eventCollection;
    }

    public string trackingName { set; get; }
    public string trackingId { set; get; }
    public DateTimeOffset trackingDate { set; get; }
    public string scale { set; get; }
    public string rating { set; get; }
    public string comment { set; get; }
    public DateTimeOffset dateOfChange { set; get; }
    public bool isDeleted { set; get; }
    public string scaleName { set; get; }
    public List<Event> EventCollection {get; set;}
  }
}
