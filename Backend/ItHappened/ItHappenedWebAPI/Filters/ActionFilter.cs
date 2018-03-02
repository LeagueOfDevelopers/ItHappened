using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using Serilog;

namespace ItHappenedWebAPI.Filters
{
  public class ActionFilter : ActionFilterAttribute
  {
    public override void OnActionExecuting(ActionExecutingContext context)
    {
      if (!context.ModelState.IsValid)
      {
        context.Result = new BadRequestObjectResult(context.ModelState);
        Log.Warning(context.ActionDescriptor.DisplayName + "model is not valid");
      }
      Log.Information("Received Arguments {@Arguments}", context.ActionArguments);
      base.OnActionExecuting(context);
    }
  }
}
