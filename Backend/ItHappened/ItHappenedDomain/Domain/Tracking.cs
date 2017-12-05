using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text;

namespace ItHappenedDomain.Domain
{
  class Tracking
  {
    public Tracking(string eventName)
    {
      TrackingName = eventName;
    }

    public void AddEvent()
    {
      
    }
    private string TrackingName { get; set; }
    private IDictionary<string, Customization> Customizations { get; set; }
  }
}
