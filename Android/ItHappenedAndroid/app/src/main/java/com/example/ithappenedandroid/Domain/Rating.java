package com.example.ithappenedandroid.Domain;

/**
 * Created by Ded on 09.12.2017.
 */

public class Rating
{
    public Rating(Integer ratingValue)
    {
        if (ratingValue == null)
            rating = ratingValue;
        else if (ratingValue > 10 || ratingValue<1)
            throw new IndexOutOfBoundsException("Value of scale out of range");
        rating = ratingValue;
    }

    public Integer GetRatingValue () {return rating;}

    private Integer rating;
}
