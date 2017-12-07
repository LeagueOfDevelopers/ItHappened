namespace ItHappenedDomain.Domain
{
  public class Comment : Customization
  {
    public Comment(string customizationName, TrackingCustomization isOptional)
    {
      CustomizationName = customizationName;
      Optional = isOptional;
    }

    public void WriteComment(string comment)
    {
      _comment = comment;
    }

    public override object GetContent()
    {
      return _comment;
    }

    private string _comment;
  }
}
