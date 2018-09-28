using System;
using System.Collections.Generic;
using ItHappenedDomain.Models;

namespace ItHappenedDomain.Domain
{
  public interface ITrackingManager
  {
    RegistrationResponse SingIn(string googleToken);
    bool FindUser(string userId);
    SynchronisationRequest SynchronizeData(string userId, DateTimeOffset nicknameDateOfChange,
      string userNickname, List<Tracking> trackingCollection);

    RegistrationResponse TestRegistration(string userId);
  }
}