using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using ItHappenedWebAPI.Security;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace ItHappenedWebAPI.Controllers
{
  public class RegistrationController : Controller
  {
    public RegistrationController(UserList users, IJwtIssuer jwtIssuer)
    {
      userList = users;
      _jwtIssuer = jwtIssuer;
    }

    private IJwtIssuer _jwtIssuer;

    [HttpPost]
    [Route("{idToken}")]
    [Authorize]
    public IActionResult SignUp([FromRoute] string idToken)
    {
      var userData = userList.SignUp(idToken);
      if (userData != null)
      {
        userData.Token = _jwtIssuer.IssueJwt(userData.UserId);
        return Ok(userData);
      }
      return BadRequest("Registration failed");
    }

    private UserList userList;
  }


}