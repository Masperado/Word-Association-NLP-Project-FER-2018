package hr.fer.takelab.robocijacije.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fran on 12/14/2017.
 */

public class WordList {
    @SerializedName("word_list")
    public String[] wordList;

    public WordList(){
        super();
    }

    public String[] getWordList() {
        return wordList;
    }
}
