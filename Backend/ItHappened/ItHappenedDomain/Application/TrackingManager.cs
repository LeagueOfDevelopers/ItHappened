using System;
using System.Collections.Generic;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using ItHappenedDomain.AuthServices;
using ItHappenedDomain.Domain;
using ItHappenedDomain.Models;

namespace ItHappenedDomain.Application
{
  public class TrackingManager : ITrackingManager
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
      var user = _userRepository.GetUserData(userId);

      if (user.NicknameDateOfChange < nicknameDateOfChange)
      {
        user.NicknameDateOfChange = nicknameDateOfChange;
        user.UserNickname = userNickname;
      }

      List<Tracking> collectionToReturn = user.ChangeTrackingCollection(trackingCollection);

      _userRepository.SaveUserData(user);

      SynchronisationRequest toReturn = new SynchronisationRequest()
      {
        NicknameDateOfChange = user.NicknameDateOfChange,
        UserNickname = user.UserNickname,
        trackingCollection = collectionToReturn
      };

      return toReturn;
    }

    private readonly IUserRepository _userRepository;
  }
}