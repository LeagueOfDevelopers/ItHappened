using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using Microsoft.AspNetCore.Mvc;

namespace ItHappenedWebAPI.Controllers
{
  [Produces("application/json")]
  [Route("synchronization")]
  public class SynchronizationController : Controller
  {
    [HttpPost]
    [Route("{userId}")]
    public IActionResult SynchronizeData([FromRoute] Guid userId,
      [FromBody] List<Tracking> trackingCollection)
    {
      List<Tracking> collectionToReturn = users.ChangeTrackingCollection(userId, trackingCollection);
      return Ok(collectionToReturn);
    }

    private UserList users;
  }


}