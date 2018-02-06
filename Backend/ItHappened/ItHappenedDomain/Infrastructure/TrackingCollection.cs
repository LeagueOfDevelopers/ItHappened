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
      if (trackingCollection == null)
      {
        return _trackingCollection;
      }

      if (_trackingCollection == null)
      {
        _trackingCollection = trackingCollection;
        return _trackingCollection;
      }

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
        if (tracking.EventCollection == null)
          continue;
        bool contains = _trackingCollection.Any(item => item.trackingId.Equals(tracking.trackingId));
        if (contains)
        {
          int indexOfTracking = _trackingCollection
            .IndexOf(_trackingCollection.First(item => item.trackingId.Equals(tracking.trackingId)));

          List<Event> oldEventCollection = _trackingCollection[indexOfTracking].EventCollection;

          if (oldEventCollection != null)
          {

            List<Event> eventCollectionToChange = oldEventCollection
              .Where(_event => tracking.EventCollection.Any(item => _event.eventId.Equals(item.eventId)))
              .Where(_event => _event.dateOfChange > tracking.EventCollection
                                 .First(item => _event.eventId.Equals(item.eventId)).dateOfChange).ToList();

            List<Event> eventCollectionToAdd = oldEventCollection
              .Where(_event => !(tracking.EventCollection.Any(item => _event.eventId.Equals(item.eventId)))).ToList();

            foreach (var _event in eventCollectionToChange)
            {
              int eventIndex = _trackingCollection[indexOfTracking].EventCollection
                .IndexOf(_trackingCollection[indexOfTracking].EventCollection
                  .First(item => item.eventId.Equals(_event.eventId)));

              _trackingCollection[indexOfTracking].EventCollection[eventIndex] = _event;
            }

            _trackingCollection[indexOfTracking].EventCollection.AddRange(eventCollectionToAdd);
          }
          else
          {
            _trackingCollection[indexOfTracking].EventCollection = tracking.EventCollection;
          }
        }
      }
    }

    private List<Tracking> _trackingCollection;
  }
}
