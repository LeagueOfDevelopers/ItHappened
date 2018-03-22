using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using ItHappenedWebAPI;

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