using Microsoft.AspNetCore.Http;

namespace ItHappenedWebAPI.Extensions
{
  public static class HttpContextExtensions
  {
    public static string GetUserId(this HttpContext context)
    {
      var userId = context.Items["Id"].ToString();
      return userId;
    }
  }
}
