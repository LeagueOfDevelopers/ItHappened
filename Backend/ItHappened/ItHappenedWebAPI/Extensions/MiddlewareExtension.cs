using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedWebAPI.Middlewares;
using Microsoft.AspNetCore.Builder;

namespace ItHappenedWebAPI.Extensions
{
  public static class MiddlewareExtension
  {
    public static IApplicationBuilder DomainErrorHandlingMiddleware(this IApplicationBuilder app)
    {
      if (app == null)
        throw new ArgumentNullException(nameof(app));
      return app.UseMiddleware<ErrorHandlingMiddleware>(Array.Empty<object>());
    }
  }
}
