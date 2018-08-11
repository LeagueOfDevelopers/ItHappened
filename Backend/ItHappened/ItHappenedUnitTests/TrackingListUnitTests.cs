using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.ExceptionServices;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ItHappenedDomain.Infrastructure;
using ItHappenedDomain.Domain;

namespace ItHappenedUnitTests
{
  [TestClass]
  public class TrackingListUnitTests
  {
    [TestMethod]
    public void SendTrackingCollectionToChangeTrackingCollection_CollectionMustSaveAndReturnThisCollection()
    {
      string trackingName = "name";
      var trackingId = new Guid().ToString();
      var eventId1 = new Guid().ToString();
      var event1DateOfChange = new DateTimeOffset();
      var eventId2 = new Guid().ToString();
      var event2DateOfChange = new DateTimeOffset();

      Event event1 = new Event(eventId1, trackingId, event1DateOfChange,
        1, new Rating(1), "", event1DateOfChange, false, 0, 0);
      Event event2 = new Event(eventId2, trackingId, event2DateOfChange,
        1, new Rating(1), "", event2DateOfChange, false, 0, 0);
      List<Event> eventCollection = new List<Event>();
      eventCollection.Add(event1);
      eventCollection.Add(event2);

      Tracking tracking = new Tracking(trackingName, trackingId, event1DateOfChange,
        "Optional", "Optional", "Optional",
        event1DateOfChange, false, eventCollection, "", null, "Optional");

      TrackingCollection trackingCollection = new TrackingCollection();

      List<Tracking> trackings = new List<Tracking>();
      trackings.Add(tracking);

      List<Tracking> returnedTrackings = trackingCollection.ChangeTrackingCollection(trackings);

      CollectionAssert.AreEqual(trackings, returnedTrackings);
    }

    [TestMethod]
    public void SendChangedTrackingCollectionToChangeTrackingCollection_CollectionMustChangeAndReturnThisCollection()
    {
      string trackingName = "name";
      var trackingId = new Guid().ToString();
      var eventId1 = new Guid().ToString();
      var event1DateOfChange = new DateTimeOffset();
      var eventId2 = new Guid().ToString();
      var event2DateOfChange = new DateTimeOffset();

      Event event1 = new Event(eventId1, trackingId, event1DateOfChange,
        1, new Rating(1), "", event1DateOfChange, false, 0, 0);
      Event event2 = new Event(eventId2, trackingId, event2DateOfChange,
        1, new Rating(1), "", event2DateOfChange, false, 0, 0);
      List<Event> eventCollection = new List<Event>();
      eventCollection.Add(event1);
      eventCollection.Add(event2);

      Tracking tracking = new Tracking(trackingName, trackingId, event1DateOfChange,
        "Optional", "Optional", "Optional",
        event1DateOfChange, false, eventCollection, "", null, "Optional");

      TrackingCollection trackingCollection = new TrackingCollection();

      List<Tracking> trackings = new List<Tracking>();
      trackings.Add(tracking);

      List<Tracking> returnedTrackings = trackingCollection.ChangeTrackingCollection(trackings);

      CollectionAssert.AreEqual(trackings, returnedTrackings);
    }
  }
}
