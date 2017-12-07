using System;
using System.Collections.Generic;

namespace ItHappenedDomain.Domain
{
  public class Event
  {
    public Event(IDictionary<string, Customization> customizations, DateTimeOffset eventDate)
    {
      Customizations = customizations;
      _eventDate = eventDate;
    }
    public IDictionary<string, Customization> Customizations { get; private set; }
    private readonly DateTimeOffset _eventDate;

    
  }
}
