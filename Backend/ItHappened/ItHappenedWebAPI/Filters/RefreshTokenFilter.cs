using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using ItHappenedWebAPI.Extensions;
using ItHappenedWebAPI.Filters.Exceptions;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.IdentityModel.Clients.ActiveDirectory;
using Microsoft.IdentityModel.Tokens;

namespace ItHappenedWebAPI.Filters
{
  public class RefreshTokenFilter : ActionFilterAttribute
  {
    private TokenValidationParameters _parameters;

    public RefreshTokenFilter(TokenValidationParameters parameters)
    {
      _parameters = parameters;
    }

    public override void OnActionExecuting(ActionExecutingContext context)
    {
      var token = context.ActionArguments["RefreshToken"].ToString();
      var handler = new JwtSecurityTokenHandler();
      var claims = new ClaimsPrincipal();
      try
      {
        claims = handler.ValidateToken(token, _parameters, out var validatedToken);
      }
      catch (SecurityTokenValidationException e)
      {
        throw new RefreshTokenValidationException($"Token failed validation: {e.Message}");
      }
      catch (ArgumentException e)
      {
        throw new RefreshTokenValidationException($"Token was invalid: {e.Message}");
      }
      var userId = claims.Claims.First().Value;
      context.HttpContext.Items.Add("Id", userId);
    }
  }
}
