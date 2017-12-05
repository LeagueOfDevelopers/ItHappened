using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.Domain
{
  class Counter : Customization
  {
    public Counter(string customizationName)
    {
      CustomizationName = customizationName;
    }

    private int _counter;
  }
}
