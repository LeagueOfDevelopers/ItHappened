﻿using System;

namespace ItHappenedDomain.Models
{
  public class RegistrationResponse
  {
    public string UserNickname { get; set; }
    public DateTimeOffset NicknameDateOfChange { get; set; }
    public string UserId { get; set; }
    public string PicUrl { get; set; }
    public string Token { get; set; }
    public string RefreshToken { get; set; }
  }
}

