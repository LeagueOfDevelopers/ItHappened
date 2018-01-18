using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using ItHappenedDomain.Domain;
using System.Linq;

namespace ItHappenedDomain.Infrastructure
{
  class TrackingRepository
  {
    public TrackingRepository(List<Tracking> trackingCollection)
    {
      _trackingCollection = trackingCollection;
    }

    public List<Tracking> ChangeTrackingCollection(List<Tracking> trackingCollection)
    {

      List<Tracking> trackingsToChange = trackingCollection
        .Where(tracking => _trackingCollection.Any(item => item.TrackingId.Equals(tracking.TrackingId)))
      .Where(item =>
          0 < item.DateOfChange.CompareTo(
            _trackingCollection.First(found => found.TrackingId == item.TrackingId).DateOfChange)).ToList();
      List<Tracking> trackingsToAdd = trackingCollection
        .Where(item => !(_trackingCollection.Any(tracking => tracking.TrackingId.Equals(item.TrackingId)))).ToList();

      foreach (var tracking in trackingsToChange)
      {
        int trackingInCollectionToChange = _trackingCollection.IndexOf(_trackingCollection
          .First(item => item.TrackingId.Equals(tracking.TrackingId)));
        _trackingCollection[trackingInCollectionToChange] = tracking;
      }

      _trackingCollection.AddRange(trackingsToAdd);

      foreach (var tracking in trackingCollection)
      {
        bool contains = _trackingCollection.Any(item => item.TrackingId.Equals(tracking.TrackingId));
        if (contains)
        {
          int indexOfTracking = _trackingCollection
            .IndexOf(_trackingCollection.First(item => item.TrackingId.Equals(tracking.TrackingId)));
          List<Event> oldEventCollection = _trackingCollection[indexOfTracking].EventCollection;

          List<Event> eventCollectionToChange = oldEventCollection
            .Where(_event => tracking.EventCollection.Any(item => _event.EventId.Equals(item.EventId)))
            .Where(_event => 0 < _event.DateOfChange.CompareTo(tracking.EventCollection
                               .First(item => _event.EventId.Equals(item.EventId)).DateOfChange)).ToList();

          List<Event> eventCollectionToAdd = oldEventCollection
            .Where(_event => !(tracking.EventCollection.Any(item => _event.EventId.Equals(item.EventId)))).ToList();

          foreach (var _event in eventCollectionToChange)
          {
            int eventIndex = _trackingCollection[indexOfTracking].EventCollection
              .IndexOf(_trackingCollection[indexOfTracking].EventCollection.First(item => item.Equals(_event.EventId)));

            _trackingCollection[indexOfTracking].EventCollection[eventIndex] = _event;
          }

          _trackingCollection[indexOfTracking].EventCollection.AddRange(eventCollectionToAdd);
        }
      }

      return _trackingCollection;
    }

    private List<Tracking> _trackingCollection;
  }
}
