using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  public class Rating
  {

    public int rating { set; get; }

    public Rating(int rating)
    {
      this.rating = rating;
    }
  }
}
