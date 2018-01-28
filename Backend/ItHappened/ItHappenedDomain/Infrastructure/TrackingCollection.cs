using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using ItHappenedDomain.Domain;
using System.Linq;

namespace ItHappenedDomain.Infrastructure
{
  public class TrackingCollection
  {
    public TrackingCollection()
    {
      _trackingCollection = new List<Tracking>();
    }

    public List<Tracking> ChangeTrackingCollection(List<Tracking> trackingCollection)
    {

      ChangeTrackings(trackingCollection);
      ChangeEventCollections(trackingCollection);

      return _trackingCollection;
    }

    private void ChangeTrackings(List<Tracking> trackingCollection)
    {
      List<Tracking> trackingsToChange = trackingCollection
        .Where(tracking => _trackingCollection.Any(item => item.trackingId.Equals(tracking.trackingId)))
        .Where(item =>
          item.dateOfChange > _trackingCollection
          .First(found => found.trackingId == item.trackingId).dateOfChange).ToList();
      List<Tracking> trackingsToAdd = trackingCollection
        .Where(item => !(_trackingCollection.Any(tracking => tracking.trackingId.Equals(item.trackingId)))).ToList();

      foreach (var tracking in trackingsToChange)
      {
        int trackingInCollectionToChange = _trackingCollection.IndexOf(_trackingCollection
          .First(item => item.trackingId.Equals(tracking.trackingId)));
        _trackingCollection[trackingInCollectionToChange] = tracking;
      }

      _trackingCollection.AddRange(trackingsToAdd);
    }

    private void ChangeEventCollections(List<Tracking> trackingCollection)
    {
      foreach (var tracking in trackingCollection)
      {
        bool contains = _trackingCollection.Any(item => item.trackingId.Equals(tracking.trackingId));
        if (contains)
        {
          int indexOfTracking = _trackingCollection
            .IndexOf(_trackingCollection.First(item => item.trackingId.Equals(tracking.trackingId)));
          List<Event> oldEventCollection = _trackingCollection[indexOfTracking].EventCollection;

          List<Event> eventCollectionToChange = oldEventCollection
            .Where(_event => tracking.EventCollection.Any(item => _event.EventId.Equals(item.EventId)))
            .Where(_event => _event.DateOfChange > tracking.EventCollection
                               .First(item => _event.EventId.Equals(item.EventId)).DateOfChange).ToList();

          List<Event> eventCollectionToAdd = oldEventCollection
            .Where(_event => !(tracking.EventCollection.Any(item => _event.EventId.Equals(item.EventId)))).ToList();

          foreach (var _event in eventCollectionToChange)
          {
            int eventIndex = _trackingCollection[indexOfTracking].EventCollection
              .IndexOf(_trackingCollection[indexOfTracking].EventCollection
              .First(item => item.Equals(_event.EventId)));

            _trackingCollection[indexOfTracking].EventCollection[eventIndex] = _event;
          }

          _trackingCollection[indexOfTracking].EventCollection.AddRange(eventCollectionToAdd);
        }
      }
    }

    private List<Tracking> _trackingCollection;
  }
}
