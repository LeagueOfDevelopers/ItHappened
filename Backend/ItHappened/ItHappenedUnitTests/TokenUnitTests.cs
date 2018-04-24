using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Text;
using ItHappenedWebAPI.Security;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace ItHappenedUnitTests
{
  [TestClass]
  public class TokenUnitTests
  {
    [TestMethod]
    public void UserIdMustBeEqualsUserIdEncryptedInToken()
    {
      var securitySettings = new SecuritySettings("ma;xqKKfZbzrKGDpXC]B%FfSB^M&xT7ldHym",
        "lod-misis.ru", TimeSpan.Parse("06:00:00"));
      var jwtIssuer = new JwtIssuer(securitySettings);
      var userId = "gay_club@rkn.pozor";

      var token = jwtIssuer.IssueJwt(userId);
      
      var handler = new JwtSecurityTokenHandler();
      var dycryptedUserId = handler.ReadJwtToken(token).Claims.First(c => c.Type == "UserId").Value;

      Assert.AreEqual(userId, dycryptedUserId);
    }
  }
}
