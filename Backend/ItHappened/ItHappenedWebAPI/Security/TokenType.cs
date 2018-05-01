using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ItHappenedWebAPI.Security
{
  public static class TokenType
  {
    public static string Refresh { get { return "refresh"; } }
    public static string Access { get { return "access"; } }
  }
}
