package ru.lod_misis.ithappened.Domain.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Ded on 09.12.2017.
 */

public class Rating extends RealmObject
{
    public Rating(Integer ratingValue)
    {
        if (ratingValue == null)
            rating = ratingValue;
        else if (ratingValue > 10 || ratingValue<1)
            throw new IndexOutOfBoundsException("Value of scale out of range");
        rating = ratingValue;
    }

    public Rating(){

    }

    public Integer GetRatingValue () {return rating;}

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Expose
    @SerializedName("rating")
    private Integer rating;
}
