using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;

namespace ItHappenedWebAPI.Extensions
{
  public static class HttpRequestExtension
  {
    public static string GetUserId(this HttpRequest request)
    {
      var auth = request.Headers["Authorization"].ToString();
      var userId = auth.Substring(7).GetUserId();
      return userId;
    }
  }
}
