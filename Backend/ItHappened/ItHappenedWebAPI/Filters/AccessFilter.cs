using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using ItHappenedWebAPI.Filters.Exceptions;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Authorization;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.IdentityModel.Tokens;

namespace ItHappenedWebAPI.Filters
{
  public class AccessFilter : AuthorizeAttribute, IAuthorizationFilter
  {
    private TokenValidationParameters _parameters;

    public AccessFilter(TokenValidationParameters parameters)
    {
      _parameters = parameters;
    }

    public void OnAuthorization(AuthorizationFilterContext context)
    {
      var bearerToken = context.HttpContext.Request.Headers["Authorization"].FirstOrDefault();
      string token;

      try
      {
        token = bearerToken.Split(" ")[1];
      }
      catch (Exception e)
      {
        context.Result = new UnauthorizedResult();
        return;
      }

      var handler = new JwtSecurityTokenHandler();
      var claims = new ClaimsPrincipal();
      try
      {
        claims = handler.ValidateToken(token, _parameters, out var validatedToken);
      }
      catch (SecurityTokenValidationException e)
      {
        context.Result = new UnauthorizedResult();
        return;
      }
      catch (ArgumentException e)
      {
        context.Result = new UnauthorizedResult();
        return;
      }
      var userId = claims.Claims.First().Value;
      context.HttpContext.Items.Add("Id", userId);
    }
  }
}
