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
      var securitySettings = new SecuritySettings("lod-misis.ru", "asdasfsagfhjkgflsdakjfb.jgglg[jnfjjnrjbvajk", 
        TimeSpan.Parse("720:00:00"), null, TimeSpan.Zero);
      var jwtIssuer = new JwtIssuer(securitySettings);
      var userId = "test";

      var token = jwtIssuer.IssueAccessJwt(userId);
      
      var handler = new JwtSecurityTokenHandler();
      var dycryptedUserId = handler.ReadJwtToken(token).Claims.First(c => c.Type == "UserId").Value;

      Assert.AreEqual(userId, dycryptedUserId);
    }

    
  }
}
