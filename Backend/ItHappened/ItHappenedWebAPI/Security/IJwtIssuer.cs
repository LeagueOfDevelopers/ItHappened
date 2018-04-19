using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ItHappenedWebAPI.Security
{
  public interface IJwtIssuer
  {
    string IssueJwt(string id);
  }
}
