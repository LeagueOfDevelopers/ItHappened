using System;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Serilog;

namespace ItHappenedWebAPI
{
  public class Program
  {
    public static void Main(string[] args)
    {
      Log.Logger = new LoggerConfiguration()
        .MinimumLevel.Information()
        .Enrich.FromLogContext()
        .WriteTo.RollingFile("log-{Date}.log")
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
