using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;
using ItHappenedWebAPI.Security;
using Microsoft.AspNetCore.Mvc;

namespace ItHappenedWebAPI.Controllers
{
  public class RegistrationController : Controller
  {
    private readonly IJwtIssuer _jwtIssuer;
    private readonly ITrackingManager _trackingManager;

    public RegistrationController(ITrackingManager manager, IJwtIssuer jwtIssuer)
    {
      _trackingManager = manager;
      _jwtIssuer = jwtIssuer;
    }

    [HttpPost]
    [Route("{idToken}")]
    public IActionResult SignUp([FromRoute] string idToken)
    {
      var userData = _trackingManager.SingIn(idToken);
      if (userData != null)
      {
        userData.Token = _jwtIssuer.IssueAccessJwt(userData.UserId);
        userData.RefreshToken = _jwtIssuer.IssueRefreshJwt(userData.UserId);
        return Ok(userData);
      }
      return BadRequest("Registration failed");
    }

    [HttpPost]
    [Route("registration")]
    public IActionResult SignUp()
    {
      if (!HttpContext.Request.Headers.ContainsKey("GoogleToken"))
        return BadRequest("Request doesn't contains token");

      var googleToken = HttpContext.Request.Headers["GoogleToken"];

      var userData = _trackingManager.SingIn(googleToken);
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
    public IActionResult Reg([FromRoute] string userId)
    {
      var userData = _trackingManager.TestRegistration(userId);

      userData.Token = _jwtIssuer.IssueAccessJwt(userData.UserId);
      userData.RefreshToken = _jwtIssuer.IssueRefreshJwt(userData.UserId);

      return Ok(userData);
    }

  }


}