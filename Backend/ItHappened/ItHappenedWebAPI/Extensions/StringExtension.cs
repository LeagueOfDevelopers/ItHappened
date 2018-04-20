using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;

namespace ItHappenedWebAPI.Extensions
{
  public static class StringExtension
  {
    public static string GetUserId(this HttpRequest request)
    {
      var auth = request.Headers["Authorization"].ToString();
      var handler = new JwtSecurityTokenHandler();
      var userId = handler.ReadJwtToken(auth.Substring(7)).Claims.First(c => c.Type == "UserId").Value;
      return userId;
    }
  }
}
