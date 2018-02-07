using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using Newtonsoft.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;

namespace ItHappenedWebAPI.Middlewares
{
  public class ErrorHandlingMiddleware : IMiddleware
  {
    public async Task InvokeAsync(HttpContext context, RequestDelegate next)
    {
      try
      {
        await next(context);
      }
      catch (Exception e) when (Log(e, context))
      {
      }
    }

    private bool Log(Exception e, HttpContext context)
    {
      var mem = new MemoryStream();
      context.Request.Body.CopyTo(mem);
      var body = new StreamReader(mem).ReadToEnd();
      using (var fs = new FileStream("bodies.txt", FileMode.OpenOrCreate))
      {
        var fsw = new StreamWriter(fs);
        fsw.WriteLine(body);
      }
      return false;
    }

  }
}
