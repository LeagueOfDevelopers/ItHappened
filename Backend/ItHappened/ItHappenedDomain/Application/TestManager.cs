using System;
using System.Collections.Generic;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using MongoDB.Bson.Serialization.Serializers;

namespace ItHappenedDomain.Application
{
  public class TestManager
  {
    public TestManager()
    {
      
    }

    public SynchronisationRequest CorellationTest()
    {
      var firstIncreasingTrackingGuid = Guid.NewGuid().ToString();
      var secondIncreasingTrackingGuid = Guid.NewGuid().ToString();
      var firstDecreasingTrackingGuid = Guid.NewGuid().ToString();
      var secondDecreasingTrackingGuid = Guid.NewGuid().ToString();

      var firstIncreasingTracking = new Tracking("First increasing tracking", 
        firstIncreasingTrackingGuid, 
        new DateTimeOffset(2010, 01, 01, 12, 30, 30, TimeSpan.Zero), "Optional",
        "Optional", "Optional",
        new DateTimeOffset(2010, 01, 01, 12, 30, 30, TimeSpan.Zero),
        false, new List<Event>(), "event", "", "Optional");

      var secondIncreasingTracking = new Tracking("Second increasing tracking",
        secondIncreasingTrackingGuid,
        new DateTimeOffset(2010, 01, 01, 12, 30, 31, TimeSpan.Zero), "Optional",
        "Optional", "Optional",
        new DateTimeOffset(2010, 01, 01, 12, 30, 31, TimeSpan.Zero),
        false, new List<Event>(), "event", "", "Optional");

      var firstDecreasingTracking = new Tracking("First decreasing tracking",
        firstDecreasingTrackingGuid,
        new DateTimeOffset(2020, 01, 01, 12, 30, 30, TimeSpan.Zero), "Optional",
        "Optional", "Optional",
        new DateTimeOffset(2020, 01, 01, 12, 30, 30, TimeSpan.Zero),
        false, new List<Event>(), "event", "", "Optional");

      var secondDecreasingTracking = new Tracking("Second decreasing tracking",
        secondDecreasingTrackingGuid,
        new DateTimeOffset(2020, 01, 01, 12, 30, 31, TimeSpan.Zero), "Optional",
        "Optional", "Optional",
        new DateTimeOffset(2020, 01, 01, 12, 30, 31, TimeSpan.Zero),
        false, new List<Event>(), "event", "", "Optional");

      for (int i = 0; i < 40; i++)
      {
        var firstDate = new DateTimeOffset(2010, 01, 01, 12, 40, 31, TimeSpan.Zero);
        var secondDate = new DateTimeOffset(2020, 01, 01, 12, 40, 31, TimeSpan.Zero);

        firstDate.AddDays(i);
        secondDate.AddDays(i);

        int increasingRating;
        int decreasingRating;

        if (i <= 5)
        {
          increasingRating = 1;
          decreasingRating = 10;
        }
        else if (i <= 10)
        {
          increasingRating = 2;
          decreasingRating = 8;
        }
        else if (i <= 15)
        {
          increasingRating = 3;
          decreasingRating = 7;
        }
        else if (i <= 20)
        {
          increasingRating = 5;
          decreasingRating = 6;
        }
        else if (i <= 25)
        {
          increasingRating = 6;
          decreasingRating = 5;
        }
        else if (i <= 30)
        {
          increasingRating = 7;
          decreasingRating = 3;
        }
        else if (i <= 35)
        {
          increasingRating = 8;
          decreasingRating = 2;
        }
        else
        {
          increasingRating = 10;
          decreasingRating = 1;
        }

        var firstIncreasingEvent = new Event(Guid.NewGuid().ToString(), firstIncreasingTrackingGuid,
          firstDate, i*10, new Rating(increasingRating), "", firstDate, false, 0, 0);
        var secondIncreasingEvent = new Event(Guid.NewGuid().ToString(), secondIncreasingTrackingGuid,
          firstDate, i * 10, new Rating(increasingRating), "", firstDate, false, 0, 0);

        firstIncreasingTracking.EventCollection.Add(firstIncreasingEvent);
        secondIncreasingTracking.EventCollection.Add(firstIncreasingEvent);

        var firstDecreasingEvent = new Event(Guid.NewGuid().ToString(), firstDecreasingTrackingGuid,
          secondDate, 400 - i * 10, new Rating(decreasingRating), "", secondDate, false, 0, 0);
        var secondDecreasingEvent = new Event(Guid.NewGuid().ToString(), secondDecreasingTrackingGuid,
          secondDate, 400 - i * 10, new Rating(decreasingRating), "", secondDate, false, 0, 0);

        firstDecreasingTracking.EventCollection.Add(firstDecreasingEvent);
        secondDecreasingTracking.EventCollection.Add(secondDecreasingEvent);
      }

      return new SynchronisationRequest()
      {
        NicknameDateOfChange = DateTimeOffset.UtcNow,
        trackingCollection = new List<Tracking>()
        {
          firstIncreasingTracking,
          secondIncreasingTracking,
          firstDecreasingTracking,
          secondDecreasingTracking
        },
        UserNickname = "test user"
      };
    }
  }
}