using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  class Comment : Customization
  {
    public Comment(string customizationName)
    {
      CustomizationName = customizationName;
    }

    private string _comment;
  }
}
