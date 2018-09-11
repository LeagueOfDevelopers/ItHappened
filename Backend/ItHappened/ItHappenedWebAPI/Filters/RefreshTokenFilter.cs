using System;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.IdentityModel.Tokens;
using Serilog;

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
        Log.Information($"Refresh token failed validation: {e.Message}");
        context.Result = new UnauthorizedResult();
        return;
      }
      catch (ArgumentException e)
      {
        Log.Information($"Refresh token was invalid: {e.Message}");
        context.Result = new UnauthorizedResult();
        return;
      }
      var userId = claims.Claims.First().Value;
      context.HttpContext.Items.Add("Id", userId);
    }
  }
}
