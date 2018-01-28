using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Net;
using Newtonsoft.Json;

namespace ItHappenedDomain.AuthServices
{
  class GoogleIdTokenVerifyer
  {
    public GoogleResponseJson Verify(string idToken)
    {
      string uri = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + idToken;
      HttpClient httpClient = new HttpClient(new HttpClientHandler());
      var response = httpClient.SendAsync(new HttpRequestMessage(HttpMethod.Get, uri)).Result;
      string responseValue = String.Empty;
      var googleResponseJson = new GoogleResponseJson();
      if (response.StatusCode == HttpStatusCode.OK)
      {
        responseValue = response.Content.ReadAsStringAsync().Result;
        googleResponseJson = JsonConvert.DeserializeObject<GoogleResponseJson>(responseValue);
      }

      return googleResponseJson;
    }
  }
}
