using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  public class Event
  {
    public Guid EventId { private set; get; }
    public Guid trackingId { private set; get; }
    private DateTimeOffset eventDate;
    private Double scale;
    private Rating rating;
    private String comment;
    public DateTimeOffset DateOfChange { private set; get; }
    public ItemStatus Status { private set; get; }
  }
}
