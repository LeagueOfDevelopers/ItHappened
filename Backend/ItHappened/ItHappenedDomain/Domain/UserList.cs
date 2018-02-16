using System;
using System.Collections.Generic;
using System.Text;
using ItHappenedDomain.AuthServices;
using ItHappenedDomain.Models;

namespace ItHappenedDomain.Domain
{
  public class UserList
  {
    public UserList()
    {
      this._userCollection = new Dictionary<string, User>();
    }

    public RegistrationResponse SignUp(string idToken)
    {
      GoogleResponseJson response = new GoogleResponseJson();
      GoogleIdTokenVerifyer verifyer = new GoogleIdTokenVerifyer();
      response = verifyer.Verify(idToken);
      if (response.IsEmpty)
        return null;
      User newUser = new User(response.email, response.picture);
      if (_userCollection.ContainsKey(response.email))
      {
        User user = _userCollection[response.email];
        return new RegistrationResponse()
        {
          PicUrl = user.PictureUrl,
          UserId = user.UserId,
          UserNickname = user.UserNickname
        };
      }
      _userCollection.Add(response.email, newUser);
      RegistrationResponse toReturn = new RegistrationResponse()
      {
        PicUrl = response.picture,
        UserId = response.email,
        UserNickname = response.email
      };
      return toReturn;
    }

    public string Reg(string id)
    {
      if (_userCollection.ContainsKey(id))
        return id;
      User newUser = new User(id, null);
      _userCollection.Add(id, newUser);
      return id;
    }

    public List<Tracking> ChangeTrackingCollection(string userId, List<Tracking> trackingCollection)
    {
      return _userCollection[userId].ChangeTrackingCollection(trackingCollection);
    }

    private readonly Dictionary<string, User> _userCollection;

  }
}
