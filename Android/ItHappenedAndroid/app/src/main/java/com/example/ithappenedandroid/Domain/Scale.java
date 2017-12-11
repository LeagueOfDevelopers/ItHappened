package com.example.ithappenedandroid.Domain;

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
    public void ChangeScaleValue (Integer value) {scale = value;}
    private Integer scale;
}
