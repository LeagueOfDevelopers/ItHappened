package com.example.ithappenedandroid;

/**
 * Created by Ded on 09.12.2017.
 */

public class Scale
{
    public Scale(Integer scaleValue)
    {
        if (scaleValue > 10 || scaleValue<1)
            throw new IndexOutOfBoundsException("Value of scale out of range");
        scale = scaleValue;
    }
    public Integer GetScaleValue () {return scale;}

    private Integer scale;
}
