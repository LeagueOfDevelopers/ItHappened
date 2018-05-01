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
      var securitySettings = new SecuritySettings("asdasfsagfhjkgflsdakjfb.jgglg",
        "lod-misis.ru", TimeSpan.Parse("06:00:00"));
      var jwtIssuer = new JwtIssuer(securitySettings, null);
      var userId = "vmargelov@gmail.com";

      var token = jwtIssuer.IssueAccessJwt(userId);
      
      var handler = new JwtSecurityTokenHandler();
      var dycryptedUserId = handler.ReadJwtToken(token).Claims.First(c => c.Type == "UserId").Value;

      Assert.AreEqual(userId, dycryptedUserId);
    }

    [TestMethod]
    public void TokenTypeMustBeEqualsTokenEncryptedInToken()
    {
      var securitySettings = new SecuritySettings("asdfaffhhdhgjs[]dasc.afg",
        "lod-misis.ru", TimeSpan.Parse("720:00:00"));
      var jwtIssuer = new JwtIssuer(null, securitySettings);
      var userId = "vmargelov@gmail.com";
      var type = TokenType.Refresh;

      var token = jwtIssuer.IssueRefreshJwt(userId);

      var handler = new JwtSecurityTokenHandler();
      var dycryptedType = handler.ReadJwtToken(token).Claims.First(c => c.Type == "TokenType").Value;

      Assert.AreEqual(type, dycryptedType);
    }
  }
}
