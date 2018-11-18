using ARIMA.Models;
using ItHappenedDomain.Domain;
using System.Collections.Generic;
using System.Linq;

namespace ARIMA
{
    public class DataFrameBuilder
    {
        private readonly int MinEventCount = 10;

        public Sequence Build(Tracking tracking)
        {
            if (!CheckEvntCollection(tracking))
            {
                return null;
            }
            var events = tracking.EventCollection.OrderBy(x => x.eventDate).ToList();
            var data = new List<double> { (double)events[0].scale };
            var lastDay = events[0].eventDate.Date;
            for (var i = 1; i < events.Count; i++)
            {
                if (events[i].eventDate.Date.Equals(lastDay))
                {
                    data[data.Count - 1] += (double)events[i].scale;
                }
                else
                {
                    data.Add((double)events[i].scale);
                    lastDay = events[i].eventDate.Date;
                }
            }
            return new Sequence(data);
        }

        private bool CheckEvntCollection(Tracking tracking)
        {
            if (tracking.isDeleted)
            {
                return false;
            }
            if (tracking.scale != "optional" && tracking.scale != "required")
            {
                return false;
            }
            if (tracking.EventCollection.Count < MinEventCount)
            {
                return false;
            }
            if (!tracking.EventCollection.TrueForAll(x => x != null))
            {
                return false;
            }
            if (!tracking.EventCollection.TrueForAll(x => x.scale != null))
            {
                return false;
            }
            return true;
        }
    }
}
