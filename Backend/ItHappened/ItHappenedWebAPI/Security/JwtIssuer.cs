using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using Microsoft.IdentityModel.Tokens;

namespace ItHappenedWebAPI.Security
{
  public class JwtIssuer : IJwtIssuer
  {
    private readonly SecuritySettings _securitySettings;

    public JwtIssuer(SecuritySettings securitySettings)
    {
      _securitySettings = securitySettings;
      
    }

    public string IssueJwt(string id)
    {
      var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_securitySettings.EncryptionKey));
      var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
      var claims = new Claim[] {new Claim("UserId", id)};
      var token = new JwtSecurityToken(_securitySettings.Issue, claims: claims, expires: DateTime.Now.Add(_securitySettings.ExpirationPeriod),
        signingCredentials: credentials);

      return new JwtSecurityTokenHandler().WriteToken(token);
    }
  }
}
