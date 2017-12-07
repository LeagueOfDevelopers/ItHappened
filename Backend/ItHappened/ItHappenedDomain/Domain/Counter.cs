namespace ItHappenedDomain.Domain
{
  public class Counter : Customization
  {
    public Counter(string customizationName, TrackingCustomization isOptional)
    {
      CustomizationName = customizationName;
      Optional = isOptional;
    }

    public void SetCount(int count)
    {
      _count = count;
    }

    public override object GetContent()
    {
      return _count;
    }

    private int _count;

  }
}
