package com.example.ithappenedandroid.Domain;

/**
 * Created by Ded on 09.12.2017.
 */

public class Scale
{
    public Scale(Integer ratingValue)
    {
        if (ratingValue > 10 || ratingValue<1)
            throw new IndexOutOfBoundsException("Value of scale out of range");
        rating = ratingValue;
    }
    public Integer GetRatingValue () {return rating;}
    private Integer rating;
}
