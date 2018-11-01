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

        var stackTrace = context.Exception.StackTrace;
        Log.Error("Strack trace: " + stackTrace);

        var exceptionSource = context.Exception.Source;
        Log.Error("Exception source: " + exceptionSource);
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
