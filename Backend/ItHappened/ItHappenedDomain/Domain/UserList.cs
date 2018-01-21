using System;
using System.Collections.Generic;
using System.Text;
using ItHappenedDomain.AuthServices;

namespace ItHappenedDomain.Domain
{
  public class UserList
  {
    public UserList(Dictionary<string, User> userCollection)
    {
      this._userCollection = userCollection;
    }

    public string SignUp(string idToken)
    {
      GoogleResponseJson response = new GoogleResponseJson();
      GoogleIdTokenVerifyer verifyer = new GoogleIdTokenVerifyer();
      response = verifyer.Verify(idToken);
      if (response.IsEmpty)
        return null;
      User newUser = new User(response.sub);
      _userCollection.Add(response.sub, newUser);
      return response.sub;
    }

    public List<Tracking> ChangeTrackingCollection(string userId, List<Tracking> trackingCollection)
    {
      return _userCollection[userId].ChangeTrackingCollection(trackingCollection);
    }

    private readonly Dictionary<string, User> _userCollection;

  }
}
