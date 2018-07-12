using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedWebAPI.Filters.Exceptions;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using ItHappenedDomain.Domain.Exceptions;
using Serilog;

namespace ItHappenedWebAPI.Filters
{
  public class ExceptionFilter : ExceptionFilterAttribute
  {
    public override void OnException(ExceptionContext context)
    {
      if (!context.ExceptionHandled)
      {
        var msg = context.Exception.Message;
        Log.Error(msg);
      }

      switch (context.Exception)
      {
        case RefreshTokenValidationException exception:
          context.Result = new UnauthorizedResult();
          break;
        case UserNotFoundException exception:
          context.Result = new BadRequestResult();
          break;
      }
    }
  }
}
