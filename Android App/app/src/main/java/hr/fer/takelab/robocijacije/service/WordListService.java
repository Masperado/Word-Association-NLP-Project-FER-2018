package hr.fer.takelab.robocijacije.service;

import hr.fer.takelab.robocijacije.model.WordList;
import hr.fer.takelab.robocijacije.model.WordListRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Fran on 12/14/2017.
 */

public interface WordListService {

    @POST("/cro/wordlist")
    Call<WordList> getResponse(@Body WordListRequest wordListRequest);
}
