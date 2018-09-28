using System;
using ItHappenedDomain.Application;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using Microsoft.AspNetCore.Mvc;
using ItHappenedWebAPI.Extensions;
using ItHappenedWebAPI.Filters;
using ItHappenedWebAPI.Models;
using ItHappenedWebAPI.Security;

namespace ItHappenedWebAPI.Controllers
{
  [Produces("application/json")]
  [Route("synchronization")]
  public class SynchronizationController : Controller
  {
    private readonly ITrackingManager _manager;
    private readonly IJwtIssuer _jwtIssuer;

    public SynchronizationController(ITrackingManager manager, IJwtIssuer jwtIssuer)
    {
      _manager = manager;
      _jwtIssuer = jwtIssuer;
    }

    [HttpPost]
    [ServiceFilter(typeof(AccessFilter))]
    [Route("synchronize")]
    public IActionResult SynchronizeData([FromBody] SynchronisationRequest request)
    {
      var userId = HttpContext.GetUserId();

      if (!_manager.FindUser(userId))
        return BadRequest("User does not exist");

      var response = _manager.SynchronizeData(userId, request.NicknameDateOfChange, 
        request.UserNickname, request.trackingCollection);
      return Ok(response);
    }

    [ServiceFilter(typeof(RefreshTokenFilter))]
    [HttpGet]
    [Route("refresh/{RefreshToken}")]
    public IActionResult RefreshToken([FromRoute] string refreshToken)
    {
      var userId = HttpContext.GetUserId();
      if (!_manager.FindUser(userId))
        return BadRequest("User does not exist");

      var response = new RefreshModel
        {
        AccessToken = _jwtIssuer.IssueAccessJwt(userId),
        RefreshToken = _jwtIssuer.IssueRefreshJwt(userId)
        };

      return Ok(response);
    }

    [HttpPost]
    [Route("{userId}")]
    public IActionResult TestSync([FromRoute] string userId, [FromBody] SynchronisationRequest request)
    {
      if (!_manager.FindUser(userId))
        return BadRequest("User does not exist");

      var response = _manager.SynchronizeData(userId, request.NicknameDateOfChange,
        userId, request.trackingCollection);
      return Ok(response);
    }

    [Obsolete]
    [HttpPost]
    [ServiceFilter(typeof(AccessFilter))]
    [Route("refresh")]
    public IActionResult RefreshToken()
    {
      var userId = HttpContext.GetUserId();
      if (!_manager.FindUser(userId))
        return BadRequest("User does not exist");

      var response = _jwtIssuer.IssueAccessJwt(userId);

      return Ok(response);
    }

    [HttpPost]
    [Route("statistics/test")]
    public IActionResult StatisticsTest()
    {
      var manager = new TestManager();
      return Ok(manager.CorellationTest());
    }
  }
}