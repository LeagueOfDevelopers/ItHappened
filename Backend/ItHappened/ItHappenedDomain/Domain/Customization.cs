using System.Runtime.Serialization;

namespace ItHappenedDomain.Domain
{
  public abstract class Customization
  {
    public string CustomizationName { get; protected set; }
    public TrackingCustomization Optional { get; protected set; }

    public bool IsOptional()
    {
      if (Optional == TrackingCustomization.OptionalComment ||
          Optional == TrackingCustomization.OptionalCounter ||
          Optional == TrackingCustomization.OptionalScale)
        return true;
      else return false;
    }
    public abstract object GetContent();
  }
}
