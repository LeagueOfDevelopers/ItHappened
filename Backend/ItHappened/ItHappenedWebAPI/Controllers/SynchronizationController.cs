using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using ItHappenedWebAPI.Extensions;
using ItHappenedWebAPI.Security;

namespace ItHappenedWebAPI.Controllers
{
  [Produces("application/json")]
  [Route("synchronization")]
  public class SynchronizationController : Controller
  {
    public SynchronizationController(UserList users, IJwtIssuer jwtIssuer)
    {
      this._users = users;
      this._jwtIssuer = jwtIssuer;
    }

    private UserList _users;
    private IJwtIssuer _jwtIssuer;

    [HttpPost]
    [Authorize]
    [Route("synchronize")]
    public IActionResult SynchronizeData([FromBody] SynchronisationRequest request)
    {
      var userId = Request.GetUserId();

      SynchronisationRequest response = _users.Synchronisation(userId, request.NicknameDateOfChange, 
        request.UserNickname, request.trackingCollection);
      return Ok(response);
    }

<<<<<<< HEAD
    private UserList users;
=======
    [HttpPost]
    [Authorize]
    [Route("refresh")]
    public IActionResult RefreshToken()
    {
      var userId = Request.GetUserId();
      if (!_users.UserIsExists(userId))
        return BadRequest("User isn't exists");

      var response = _jwtIssuer.IssueJwt(userId);

      return Ok(response);
    }
>>>>>>> master
  }


}