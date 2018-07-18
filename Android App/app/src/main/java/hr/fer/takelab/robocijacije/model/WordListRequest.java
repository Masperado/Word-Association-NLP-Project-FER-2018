package hr.fer.takelab.robocijacije.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fran on 1/11/2018.
 */

public class WordListRequest {

    @SerializedName("word")
    private String word;
    @SerializedName("skip_list")
    private String[] skip_list;

    public void setWord(String word) {
        this.word = word;
    }

    public void setSkip_list(String... skip_list) {
        this.skip_list = skip_list;
    }

    public WordListRequest(String word, String... skip_list){
        this.word = word;
        this.skip_list = skip_list;
    }

    public String getWord() {
        return word;
    }

    public String[] getSkip_list() {
        return skip_list;
    }
}
