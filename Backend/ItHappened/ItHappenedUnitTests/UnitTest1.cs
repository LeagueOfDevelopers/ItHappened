using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.ExceptionServices;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using ItHappenedDomain.Domain;
namespace ItHappenedUnitTests
{
  [TestClass]
  public class UnitTest1
  {

    private bool IDictionariesAreEqual
    (IDictionary<string, Customization> firstDictionary,
      IDictionary<string, Customization> secondDictionary)
    {
      if (firstDictionary.All(custOne =>
        secondDictionary.Any(custTwo => custOne.Value.GetContent() == custTwo.Value.GetContent() 
        && custOne.Value.CustomizationName == custTwo.Value.CustomizationName
        && custOne.Value.Optional == custTwo.Value.Optional
        && custOne.Key == custTwo.Key)))
        return true;
      return false;
    }
    [TestMethod]
    public void NewTrackingWithCounterAndCommentAndAddNewEvent()
    {
      string eventName= "event";
      int count = 1, scale = 0;
      string comment = "comment";

      Tracking tracking = new Tracking(
        eventName,
        TrackingCustomization.Counter,
        TrackingCustomization.Unknown,
        TrackingCustomization.Comment);

      Event newEvent = tracking.AddEvent(count, scale, comment);

      IDictionary<string, Customization> eventCustomizations = new Dictionary<string, Customization>();
      Counter counter = new Counter(eventName, TrackingCustomization.Counter);
      counter.SetCount(count);
      Comment Comment = new Comment(eventName, TrackingCustomization.Comment);
      Comment.WriteComment(comment);
      eventCustomizations.Add("Counter", counter);
      eventCustomizations.Add("Comment", Comment);
      Event EventMustBe = new Event(eventCustomizations, DateTimeOffset.Now);
      Assert.IsTrue(IDictionariesAreEqual(newEvent.Customizations, EventMustBe.Customizations));
    }

    [TestMethod]
    public void SetCountInClassCounter()
    {
      string customizationName = "name";
      TrackingCustomization customization = TrackingCustomization.Counter;
      Counter counter = new Counter(customizationName, customization);
      int count = 1;
      counter.SetCount(count);

      Assert.AreEqual(count, counter.GetContent());
    }

    [TestMethod]
    public void SetValueInClassScale()
    {
      string customizationName = "name";
      TrackingCustomization customization = TrackingCustomization.Counter;
      Scale scale = new Scale(customizationName, customization);
      int sclaeValue = 10;
      scale.SetValue(sclaeValue);
      
      Assert.AreEqual(sclaeValue, scale.GetContent());
    }

    [TestMethod]
    [ExpectedException(typeof(ArgumentOutOfRangeException))]
    public void SetValueMethodWithValue0ShouldThrowException()
    {
      string customizationName = "name";
      TrackingCustomization customization = TrackingCustomization.Counter;
      Scale scale = new Scale(customizationName, customization);
      int sclaeValue = 0;
      scale.SetValue(sclaeValue);
    }

    [TestMethod]
    [ExpectedException(typeof(ArgumentOutOfRangeException))]
    public void SetValueMethodWithValue11ShouldThrowException()
    {
      string customizationName = "name";
      TrackingCustomization customization = TrackingCustomization.Counter;
      Scale scale = new Scale(customizationName, customization);
      int sclaeValue = 11;
      scale.SetValue(sclaeValue);
    }

    [TestMethod]
    public void WriteCommentInClassComment()
    {
      string customizationName = "name";
      TrackingCustomization customization = TrackingCustomization.Comment;
      Comment comment = new Comment(customizationName, customization);
      string commentary = "comment";
      comment.WriteComment(commentary);

      Assert.AreEqual(comment.GetContent(), commentary);
    }

  }
}
