using ItHappenedDomain.AuthServices;
using ItHappenedDomain.Infrastructure;
using ItHappenedDomain.Models;

namespace ItHappenedDomain.Domain
{
  public interface IUserRepository
  {
    RegistrationResponse AddOrFindUser(GoogleResponseJson userId);
    User GetUserData(string userId);
    void SaveUserData(User user);
    bool FindUser(string userId);
  }
}