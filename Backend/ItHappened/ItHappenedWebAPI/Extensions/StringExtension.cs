using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Threading.Tasks;

namespace ItHappenedWebAPI.Extensions
{
  public static class StringExtension
  {
    public static string GetUserId(this string request)
    {
      var handler = new JwtSecurityTokenHandler();
      var userId = handler.ReadJwtToken(request).Claims.First(c => c.Type == "UserId").Value;
      return userId;
    }
  }
}
