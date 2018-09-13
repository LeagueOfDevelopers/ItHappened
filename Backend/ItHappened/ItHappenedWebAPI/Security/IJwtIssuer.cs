namespace ItHappenedWebAPI.Security
{
  public interface IJwtIssuer
  {
    string IssueAccessJwt(string id);
    string IssueRefreshJwt(string id);
  }
}
