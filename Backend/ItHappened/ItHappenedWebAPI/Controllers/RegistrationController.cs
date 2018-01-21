using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using Microsoft.AspNetCore.Mvc;

namespace ItHappenedWebAPI.Controllers
{
  public class RegistrationController : Controller
  {
    public RegistrationController(UserList users)
    {
      userList = users;
    }
    [HttpPost]
    [Route("{idToken}")]
    public IActionResult SignUp([FromRoute] string idToken)
    {
      string userId = userList.SignUp(idToken);
      if (userId != null)
        return Ok(userId);
      return BadRequest("Registration failed");
    }

    private UserList userList;
  }


}