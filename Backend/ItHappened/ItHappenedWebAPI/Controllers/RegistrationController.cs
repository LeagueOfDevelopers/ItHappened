using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using ItHappenedWebAPI.Security;
using Microsoft.AspNetCore.Mvc;

namespace ItHappenedWebAPI.Controllers
{
  public class RegistrationController : Controller
  {
    private readonly IJwtIssuer _jwtIssuer;
    private readonly UserList _userList;

    public RegistrationController(UserList users, IJwtIssuer jwtIssuer)
    {
      _userList = users;
      _jwtIssuer = jwtIssuer;
    }

    [HttpPost]
    [Route("{idToken}")]
    public IActionResult SignUp([FromRoute] string idToken)
    {
      var userData = _userList.SignUp(idToken);
      if (userData != null)
      {
        userData.Token = _jwtIssuer.IssueAccessJwt(userData.UserId);
        userData.RefreshToken = _jwtIssuer.IssueRefreshJwt(userData.UserId);
        return Ok(userData);
      }
      return BadRequest("Registration failed");
    }

    [HttpPost]
    [Route("reg/{userId}")]
    public IActionResult Reg([FromRoute] string userId, [FromBody] RegistrationResponse model)
    {
      var userData = _userList.TestRegistration(userId);
      return Ok(userData);
    }

    
  }


}