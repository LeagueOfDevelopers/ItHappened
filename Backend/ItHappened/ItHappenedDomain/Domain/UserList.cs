using System;
using System.Collections.Generic;
using System.Text;
using ItHappenedDomain.AuthServices;

namespace ItHappenedDomain.Domain
{
  public class UserList
  {
    public UserList()
    {
      this._userCollection = new Dictionary<string, User>();
    }

    public string SignUp(string idToken)
    {
      GoogleResponseJson response = new GoogleResponseJson();
      GoogleIdTokenVerifyer verifyer = new GoogleIdTokenVerifyer();
      response = verifyer.Verify(idToken);
      if (response.IsEmpty)
        return null;
      User newUser = new User(response.email);
      if (_userCollection.ContainsKey(response.email))
        return response.email;
      _userCollection.Add(response.email, newUser);
      return response.email;
    }

    public string Reg(string id)
    {
      if (_userCollection.ContainsKey(id))
        return id;
      User newUser = new User(id);
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
