using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using Microsoft.AspNetCore.Mvc;

namespace ItHappenedWebAPI.Controllers
{
  [Produces("application/json")]
  [Route("synchronization")]
  public class SynchronizationController : Controller
  {
    public SynchronizationController(UserList users)
    {
      this.users = users;
    }

    [HttpPost]
    [Route("{userId}")]
    public IActionResult SynchronizeData([FromRoute] string userId,
      [FromBody] SynchronisationRequest request)
    {
      SynchronisationRequest response = users.Synchronisation(userId, request.NicknameDateOfChange, 
        request.UserNickname, request.trackingCollection);
      return Ok(response);
    }

    private UserList users;
  }


}