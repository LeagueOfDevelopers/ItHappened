using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
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
      [FromBody] List<Tracking> trackingCollection)
    {
      List<Tracking> collectionToReturn = null;
        collectionToReturn = users.ChangeTrackingCollection(userId, trackingCollection);
      return Ok(collectionToReturn);
    }

    [HttpPost]
    [Route("add/{userId}")]
    public IActionResult AddUser([FromRoute] string userId)
    {
      string id = users.Reg(userId);
      return Ok(id);
    }

    private string _filepath = "logs.json";
    private UserList users;
  }


}