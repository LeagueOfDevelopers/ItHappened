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
    private readonly SecuritySettings _accessSecuritySettings;
    private readonly SecuritySettings _refreshSecuritySettings;

    public JwtIssuer(SecuritySettings accessSecuritySettings, SecuritySettings refreshSecuritySettings)
    {
      _accessSecuritySettings = accessSecuritySettings;
      _refreshSecuritySettings = refreshSecuritySettings;
    }

    public string IssueAccessJwt(string id)
    {
      var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_accessSecuritySettings.EncryptionKey));
      var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
      var claims = new Claim[]
      {
        new Claim("UserId", id),
        new Claim("TokenType", TokenType.Access)
      };
      var token = new JwtSecurityToken(_accessSecuritySettings.Issue, claims: claims, expires: DateTime.Now.Add(_accessSecuritySettings.ExpirationPeriod),
        signingCredentials: credentials);
      
      return new JwtSecurityTokenHandler().WriteToken(token);
    }

    public string IssueRefreshJwt(string id)
    {
      var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_refreshSecuritySettings.EncryptionKey));
      var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
      var claims = new Claim[]
      {
        new Claim("UserId", id),
        new Claim("TokenType", TokenType.Refresh)
      };
      var token = new JwtSecurityToken(_refreshSecuritySettings.Issue, claims: claims,
        expires: DateTime.Now.Add(_refreshSecuritySettings.ExpirationPeriod),
        signingCredentials: credentials);

      return new JwtSecurityTokenHandler().WriteToken(token);
    }
  }
}
