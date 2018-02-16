using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
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
      RegistrationResponse userData = userList.SignUp(idToken);
      if (userData != null)
        return Ok(userData);
      return BadRequest("Registration failed");
    }

    private UserList userList;
  }


}