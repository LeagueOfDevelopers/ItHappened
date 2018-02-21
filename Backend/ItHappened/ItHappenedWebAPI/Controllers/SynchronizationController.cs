using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

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
      SynchronisationRequest response = users.Synchronisation(userId, request);
      return Ok(response);
    }

    [HttpPost]
    [Route("add/{userId}")]
    public IActionResult AddUser([FromRoute] string userId)
    {
      RegistrationResponse id = users.Reg(userId);
      return Ok(id);
    }

    private UserList users;
  }


}