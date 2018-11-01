using System;

namespace ItHappenedWebAPI.Security
{
  public class SecuritySettings
  {
    public SecuritySettings(string issue, string accessEncryptionKey, TimeSpan accessExpirationPeriod,
      string refreshEncryptionKey, TimeSpan refreshExpirationPeriod)
    {
      AccessEncryptionKey = accessEncryptionKey;
      Issue = issue;
      AccessExpirationPeriod = accessExpirationPeriod;
      RefreshEncryptionKey = refreshEncryptionKey;
      RefreshExpirationPeriod = refreshExpirationPeriod;
    }

    public string AccessEncryptionKey { get; }
    public string RefreshEncryptionKey { get; }
    public string Issue { get; }
    public TimeSpan AccessExpirationPeriod { get; }
    public TimeSpan RefreshExpirationPeriod { get; }
  }
}

