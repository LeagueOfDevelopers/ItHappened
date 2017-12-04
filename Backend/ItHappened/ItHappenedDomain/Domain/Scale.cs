using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  class Scale : Customization
  {
    public Scale(string customizationName)
    {
      CustomizationName = customizationName;
    }

    private int _scale;
  }
}
