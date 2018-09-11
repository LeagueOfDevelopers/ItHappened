using ItHappenedDomain.AuthServices;
using ItHappenedDomain.Infrastructure;
using ItHappenedDomain.Models;

namespace ItHappenedDomain.Domain
{
  public interface IUserRepository
  {
    RegistrationResponse AddOrFindUser(GoogleResponseJson userId);
    TrackingCollection GetTrackingCollection(string userId);
    TrackingCollection SaveTrackingCollection(string userId);
    bool FindUser(string userId);
  }
}