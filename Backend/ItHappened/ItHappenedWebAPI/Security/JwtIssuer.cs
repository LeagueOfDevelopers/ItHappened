using System;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
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

    public string IssueAccessJwt(string id)
    {
      return CreateToken(_securitySettings.AccessEncryptionKey, _securitySettings.AccessExpirationPeriod, id);
    }

    public string IssueRefreshJwt(string id)
    {
      return CreateToken(_securitySettings.RefreshEncryptionKey, _securitySettings.RefreshExpirationPeriod, id);
    }

    private string CreateToken(string encryptionKey, TimeSpan expirationPeriod, string id)
    {
      var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(encryptionKey));
      var credentials = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);
      var claims = new Claim[]
      {
        new Claim("UserId", id)
      };
      var token = new JwtSecurityToken(_securitySettings.Issue, claims: claims,
        expires: DateTime.UtcNow.Add(expirationPeriod),
        signingCredentials: credentials);

      return new JwtSecurityTokenHandler().WriteToken(token);
    }
  }
}
