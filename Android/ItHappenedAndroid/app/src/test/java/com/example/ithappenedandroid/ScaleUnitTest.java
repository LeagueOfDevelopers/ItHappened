package com.example.ithappenedandroid;

import com.example.ithappenedandroid.Domain.Scale;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Ded on 12.12.2017.
 */

public class ScaleUnitTest
{
    @Test
    public void CreateScaleWithValueLessThen1__ThrowException ()
    {
        Integer scaleValue = 0;
        Scale scale;
        boolean thrown = false;

        try { scale = new Scale(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void CreateScaleWithValueMoreThan10_ThrowException ()
    {
        Integer scaleValue = 11;
        Scale scale;
        boolean thrown = false;

        try { scale = new Scale(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertTrue(thrown);
    }

    @Test
    public void ScaleWithValueLessThan11AndMoreThan0_ThereIsNoException ()
    {
        Integer scaleValue = 10;
        Scale scale;
        boolean thrown = false;

        try { scale = new Scale(scaleValue);}
        catch (IndexOutOfBoundsException e) { thrown = true; }

        Assert.assertFalse(thrown);
    }
}
