using System.Collections.Generic;
using ItHappenedDomain.Domain;
using System.Linq;

namespace ItHappenedDomain.Infrastructure
{
  public class TrackingCollection
  {
    public TrackingCollection()
    {
      TrackingList = new List<Tracking>();
    }

    public List<Tracking> ChangeTrackingCollection(List<Tracking> trackingCollection)
    {
      //ColorCheck();
      if (trackingCollection == null)
      {
        return TrackingList;
      }

      if (TrackingList == null)
      {
        TrackingList = trackingCollection;
        return TrackingList;
      }

      ChangeTrackings(trackingCollection);
      ChangeEventCollections(trackingCollection);

      return TrackingList;
    }

    private void ChangeTrackings(List<Tracking> trackingCollection)
    {
      List<Tracking> trackingsToChange = trackingCollection
        .Where(tracking => TrackingList.Any(item => item.trackingId.Equals(tracking.trackingId)))
        .Where(item =>
          item.dateOfChange > TrackingList
          .First(found => found.trackingId == item.trackingId).dateOfChange).ToList();
      List<Tracking> trackingsToAdd = trackingCollection
        .Where(item => !(TrackingList.Any(tracking => tracking.trackingId.Equals(item.trackingId)))).ToList();

      foreach (var tracking in trackingsToChange)
      {
        int trackingInCollectionToChange = TrackingList.IndexOf(TrackingList
          .First(item => item.trackingId.Equals(tracking.trackingId)));
        TrackingList[trackingInCollectionToChange] = tracking;
      }

      TrackingList.AddRange(trackingsToAdd);
    }

    private void ChangeEventCollections(List<Tracking> trackingCollection)
    {
      
      foreach (var tracking in trackingCollection)
      {
        if (tracking.EventCollection == null)
          continue;
        bool contains = TrackingList.Any(item => item.trackingId.Equals(tracking.trackingId));
        if (contains)
        {
          int indexOfTracking = TrackingList
            .IndexOf(TrackingList.First(item => item.trackingId.Equals(tracking.trackingId)));

          List<Event> oldEventCollection = TrackingList[indexOfTracking].EventCollection;

          if (oldEventCollection != null)
          {

            List<Event> eventCollectionToChange = oldEventCollection
              .Where(_event => tracking.EventCollection.Any(item => _event.eventId.Equals(item.eventId)))
              .Where(_event => _event.dateOfChange > tracking.EventCollection
                                 .First(item => _event.eventId.Equals(item.eventId)).dateOfChange).ToList();

            List<Event> eventCollectionToAdd = tracking.EventCollection
              .Where(_event => !(oldEventCollection.Any(item => _event.eventId.Equals(item.eventId)))).ToList();

            foreach (var _event in eventCollectionToChange)
            {
              int eventIndex = TrackingList[indexOfTracking].EventCollection
                .IndexOf(TrackingList[indexOfTracking].EventCollection
                  .First(item => item.eventId.Equals(_event.eventId)));

              TrackingList[indexOfTracking].EventCollection[eventIndex] = _event;
            }

            TrackingList[indexOfTracking].EventCollection.AddRange(eventCollectionToAdd);
          }
          else
          {
            TrackingList[indexOfTracking].EventCollection = tracking.EventCollection;
          }
        }
      }
    }

    //public void ColorCheck()
    //{
    //  TrackingList.ForEach(tracking =>
    //  {
    //    if (tracking.color == null)
    //      tracking.color = "-5658199";
    //  });
    //}

    public List<Tracking> TrackingList { get; set; }
  }
}
