using Microsoft.AspNetCore.Mvc;

namespace ItHappenedWebAPI.Controllers
{
  public class ViewController : Controller
  {
    [HttpGet]
    [Route("privacy/policy")]
    public IActionResult PrivacyPolicy()
    {
      ViewData["Policy"] = "";
      return View();
    }
  }
}