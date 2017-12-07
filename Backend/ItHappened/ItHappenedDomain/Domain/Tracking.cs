using System;
using System.Collections.Generic;

namespace ItHappenedDomain.Domain
{
  public class Tracking
  {
    public Tracking
      (string eventName, 
      TrackingCustomization counter,  
      TrackingCustomization scale, 
      TrackingCustomization comment)
    {
      TrackingName = eventName;
      Customizations = new Dictionary<string, Customization>();
      if (counter == TrackingCustomization.Counter || counter == TrackingCustomization.OptionalCounter)
      {
        Customizations.Add("Counter", new Counter(TrackingName, counter));
      }
      if (scale == TrackingCustomization.Scale || scale == TrackingCustomization.OptionalScale)
      {
        Customizations.Add("Scale", new Scale(TrackingName, scale));
      }
      if (comment == TrackingCustomization.Comment || comment == TrackingCustomization.OptionalComment)
      {
        Customizations.Add("Comment", new Comment(TrackingName, comment));
      }
    }

    public Event AddEvent(int count, int scale, string comment)
    {
      IDictionary<string, Customization> customizations = new Dictionary<string, Customization>();
      if (Customizations.ContainsKey("Counter"))
      {
        Counter counter = new Counter(TrackingName, Customizations["Counter"].Optional);
        counter.SetCount(count);
        customizations.Add("Counter", counter);
      }
      if (Customizations.ContainsKey("Scale"))
      {
        Scale Scale= new Scale(TrackingName, Customizations["Scale"].Optional);
        Scale.SetValue(scale);
        customizations.Add("Counter", Scale);
      }
      if (Customizations.ContainsKey("Counter"))
      {
        Comment Comment = new Comment(TrackingName, Customizations["Comment"].Optional);
        Comment.WriteComment(comment);
        customizations.Add("Comment", Comment);
      }
      Event newEvent = new Event(customizations, DateTimeOffset.Now);
      return newEvent;
    }
    private string TrackingName { get; set; }
    private IDictionary<string, Customization> Customizations { get; set; }
    
  }
}
