using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Threading.Tasks;

namespace ItHappenedWebAPI.Filters.Exceptions
{
  public class RefreshTokenValidationException : Exception
  {
    public RefreshTokenValidationException(string message) : base(message)
    {
    }

    public RefreshTokenValidationException(string message, Exception innerException) : base(message, innerException)
    {
    }

    public RefreshTokenValidationException(SerializationInfo info, StreamingContext context) : base(info,
      context)
    {
    }
  }
}
