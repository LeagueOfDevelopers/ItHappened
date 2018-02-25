using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Text;

namespace ItHappenedDomain.Domain.Exceptions
{
  class UserNotFoundException : Exception
  {
    public UserNotFoundException()
    {
    }

    public UserNotFoundException(string message) : base(message)
    {
    }

    public UserNotFoundException(string message, Exception inner) : base(message, inner)
    {
    }

    protected UserNotFoundException(SerializationInfo info, StreamingContext context)
    {
    }
  }
}
