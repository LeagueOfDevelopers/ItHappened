using System;
using System.Collections.Generic;
using System.Text;

namespace ItHappenedDomain.AuthServices
{
  class GoogleResponseJson
  {
    public string iss { get; set; }
    public string iat { get; set; }
    public string exp { get; set; }
    public string aud { get; set; }
    public string sub { get; set; }
    public bool email_verified { get; set; }
    public string azp { get; set; }
    public string email { get; set; }
    public string name { get; set; }
    public string picture { get; set; }
    public string given_name { get; set; }
    public string family_name { get; set; }
    public string locale { get; set; }
    public string alg { get; set; }
    public string kid { get; set; }

    public GoogleResponseJson()
    {
      kid = String.Empty;
      alg = String.Empty;
      email = String.Empty;
      locale = String.Empty;
      family_name = String.Empty;
      given_name = String.Empty;
      picture = String.Empty;
      name = String.Empty;
      azp = String.Empty;
      email_verified = false;
      sub = String.Empty;
      aud = String.Empty;
      exp = String.Empty;
      iat = String.Empty;
      iss = String.Empty;
    }

    public bool IsEmpty
    {
      get { return sub == String.Empty && name == String.Empty; }
    }
  }
}
