package hr.fer.takelab.robocijacije.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Fran on 12/8/2017.
 */

public class RandomWord implements Serializable {

    @SerializedName("word")
    private String word;

    public RandomWord(){
        super();
    }

    public String getWord(){
        return word;
    }
}
