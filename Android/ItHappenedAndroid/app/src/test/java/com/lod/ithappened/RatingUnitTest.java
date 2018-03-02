package com.lod.ithappened;

import com.lod.ithappened.Domain.Rating;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Ded on 12.12.2017.
 */

public class RatingUnitTest
{

    @Test
    public void CreateScaleWithValueLessThen1__ThrowException ()
    {
        Integer scaleValue = 0;
        Rating rating;
        boolean thrown = false;

        try { rating = new Rating(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void CreateScaleWithValueMoreThan10_ThrowException ()
    {
        Integer scaleValue = 11;
        Rating rating;
        boolean thrown = false;

        try { rating = new Rating(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void ScaleWithValueLessThan11AndMoreThan0_ThereIsNoException ()
    {
        Integer scaleValue = 10;
        Rating rating;
        boolean thrown = false;

        try { rating = new Rating(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertFalse(thrown);
    }

    @Test
    public void ValueOfRatingIsNull_ThereIsNoException ()
    {
        Integer ratingValue = null;
        Rating rating;
        boolean thrown = false;

        try { rating = new Rating(ratingValue);}
        catch (NullPointerException e) { thrown = true; }

        Assert.assertFalse(thrown);
    }
}
