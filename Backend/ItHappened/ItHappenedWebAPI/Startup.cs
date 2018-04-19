using System;
using System.Text;
using ItHappenedDomain.Domain;
using ItHappenedWebAPI.Extensions;
using ItHappenedWebAPI.Filters;
using ItHappenedWebAPI.Middlewares;
using ItHappenedWebAPI.Security;
using Loggly;
using Loggly.Config;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Tokens;
using MongoDB.Driver;
using Serilog;
using Serilog.Events;
using Migrator;

namespace ItHappenedWebAPI
{
  public class Startup
  {
    public Startup(IConfiguration configuration)
    {
      Configuration = configuration;
    }

    public IConfiguration Configuration { get; }

    // This method gets called by the runtime. Use this method to add services to the container.
    public void ConfigureServices(IServiceCollection services)
    {
      StartLoggly();

      var connectionString = "mongodb://localhost";
      var client = new MongoClient(connectionString);
      var db = client.GetDatabase("ItHappenedDB");

      var securityConfiguration = Configuration.GetSection("Security");

      var securitySettings = new SecuritySettings(securityConfiguration["EncryptionKey"],
        securityConfiguration["Issue"],
        securityConfiguration.GetValue<TimeSpan>("ExpirationPeriod"));
      var jwtIssuer = new JwtIssuer(securitySettings);

      services.AddSingleton(securitySettings);
      services.AddSingleton<IJwtIssuer>(jwtIssuer);

      services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
        .AddJwtBearer(options =>
        {
          options.TokenValidationParameters = new TokenValidationParameters
          {
            ValidateIssuer = false,
            ValidateAudience = false,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            ClockSkew = TimeSpan.Zero,
            IssuerSigningKey = new SymmetricSecurityKey(
              Encoding.UTF8.GetBytes(securitySettings.EncryptionKey))
          };
        });

      services
        .AddAuthorization(options =>
        {
          options.DefaultPolicy = new AuthorizationPolicyBuilder(JwtBearerDefaults.AuthenticationScheme)
            .RequireAuthenticatedUser().Build();
        });
        var userList = new UserList(db);
      services
        .AddSingleton<UserList>(userList)
        .AddSingleton<ErrorHandlingMiddleware>();
      services.AddMvc(o =>
      {
        o.Filters.Add(new ActionFilter());
      });
    }

    // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
    public void Configure(IApplicationBuilder app, IHostingEnvironment env)
    {
      if (env.IsDevelopment())
      {
        app.UseDeveloperExceptionPage();
      }

      app.DomainErrorHandlingMiddleware();

      app.UseMvc();
    }

    private void StartLoggly()
    {
      var config = LogglyConfig.Instance;

      config.CustomerToken = Configuration.GetValue<string>("LogglyToken");
      config.ApplicationName = "ItHappenedWebApi";

      config.Transport.EndpointHostname = "logs-01.loggly.com";
      config.Transport.EndpointPort = 443;
      config.Transport.LogTransport = LogTransport.Https;

      var ct = new ApplicationNameTag();
      ct.Formatter = "application-{0}";
      config.TagConfig.Tags.Add(ct);

      Log.Logger = new LoggerConfiguration()
        .MinimumLevel.Information()
        .MinimumLevel.Override("Microsoft", LogEventLevel.Information)
        .Enrich.FromLogContext()
        .WriteTo.Loggly()
        .CreateLogger();
      Log.Information("Loggly started");
    }
  }
}
