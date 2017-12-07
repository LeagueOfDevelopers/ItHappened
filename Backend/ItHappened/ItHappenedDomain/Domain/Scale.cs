using System;

namespace ItHappenedDomain.Domain
{
  public class Scale : Customization
  {
    public Scale(string customizationName, TrackingCustomization isOptional)
    {
      CustomizationName = customizationName;
      Optional = isOptional;
    }

    public void SetValue(int mark)
    {
      if (mark<1 || mark>10)
        throw new ArgumentOutOfRangeException("Scale out of range");
      scale = mark;
    }
    public override object GetContent()
    {
      return scale;
    }

    private int scale;
   
  }
}
