using System;
using System.Collections.Generic;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using ItHappenedDomain.AuthServices;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;

namespace ItHappenedDomain.Application
{
  public class TrackingManager
  {
    public TrackingManager(IUserRepository userRepository)
    {
      _userRepository = userRepository;
    }

    public RegistrationResponse SingIn(string googleToken)
    {
      var verifier = new GoogleIdTokenVerifyer();

      var response = verifier.Verify(googleToken);
      if (response.IsEmpty)
        return null;
      
      if (response.email == null)
        return null;

      var toReturn = _userRepository.AddOrFindUser(response);
      return toReturn;
    }

    public bool FindUser(string userId)
    {
      return _userRepository.FindUser(userId);
    }

    public SynchronisationRequest SynchronizeData(string userId, DateTimeOffset nicknameDateOfChange, 
      string userNickname, List<Tracking> trackingCollection)
    {
      var toReturn = _userRepository.GetTrackingCollection(userId);
    }

    private readonly IUserRepository _userRepository;
  }
}