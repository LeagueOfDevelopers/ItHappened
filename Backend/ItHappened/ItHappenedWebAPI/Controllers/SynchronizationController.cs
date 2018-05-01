using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using ItHappenedWebAPI.Extensions;
using ItHappenedWebAPI.Models;
using ItHappenedWebAPI.Security;

namespace ItHappenedWebAPI.Controllers
{
  [Produces("application/json")]
  [Route("synchronization")]
  public class SynchronizationController : Controller
  {
    private readonly UserList _users;
    private readonly IJwtIssuer _jwtIssuer;

    public SynchronizationController(UserList users, IJwtIssuer jwtIssuer)
    {
      this._users = users;
      this._jwtIssuer = jwtIssuer;
    }

    [HttpPost]
    [Authorize]
    [Route("synchronize")]
    public IActionResult SynchronizeData([FromBody] SynchronisationRequest request)
    {
      if (Request.GetTokenType() == TokenType.Refresh)
        return Unauthorized();

      var userId = Request.GetUserId();

      if (!_users.UserIsExists(userId))
        return BadRequest("User isn't exists");

      SynchronisationRequest response = _users.Synchronisation(userId, request.NicknameDateOfChange, 
        request.UserNickname, request.trackingCollection);
      return Ok(response);
    }

    [HttpPost]
    [Route("refresh/{RefreshToken}")]
    public IActionResult RefreshToken([FromRoute] string refreshToken)
    {
      if (refreshToken.GetTokenType() == TokenType.Access)
        return Unauthorized();

      var userId = refreshToken.GetUserId();
      if (!_users.UserIsExists(userId))
        return BadRequest("User isn't exists");

      var response = new RefreshModel
        {
        AccessToken = _jwtIssuer.IssueAccessJwt(userId),
        RefreshToken = _jwtIssuer.IssueRefreshJwt(userId)
        };

      return Ok(response);
    }
  }


}