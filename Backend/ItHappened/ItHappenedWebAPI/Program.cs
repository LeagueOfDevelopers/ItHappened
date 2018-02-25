using System;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Serilog;
using Serilog.Events;

namespace ItHappenedWebAPI
{
  public class Program
  {
    public static void Main(string[] args)
    {
      Log.Logger = new LoggerConfiguration()
        .MinimumLevel.Information()
        .MinimumLevel.Override("Microsoft", LogEventLevel.Information)
        .Enrich.FromLogContext()
        .WriteTo.RollingFile("ItHappened.log")
        .CreateLogger();
      try
      {
        Log.Information("Starting....");
        BuildWebHost(args).Run();
      }
      catch (Exception e)
      {
        Log.Fatal(e, "Failed to start services");
      }
    }

    public static IWebHost BuildWebHost(string[] args) =>
        WebHost.CreateDefaultBuilder(args)
            .UseStartup<Startup>()
      .Build();
  }
}
