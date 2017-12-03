using System;
using System.Collections.Generic;
using System.Text;
using ItHappenedDomain.Domain;

namespace ItHappenedDomain
{
  class Event
  {
    public Event(string eventName)
    {
      _eventName = eventName;
    }
    private string _eventName;
  }
}
