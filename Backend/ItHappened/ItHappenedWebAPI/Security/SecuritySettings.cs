using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ItHappenedWebAPI.Security
{
  public class SecuritySettings
  {
    public SecuritySettings(string encryptionKey, string issue, TimeSpan expirationPeriod)
    {
      EncryptionKey = encryptionKey;
      Issue = issue;
      ExpirationPeriod = expirationPeriod;
    }

    public string EncryptionKey { get; }
    public string Issue { get; }
    public TimeSpan ExpirationPeriod { get; }
  }
}

