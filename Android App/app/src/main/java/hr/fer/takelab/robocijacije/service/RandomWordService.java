package hr.fer.takelab.robocijacije.service;

import hr.fer.takelab.robocijacije.model.RandomWord;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Fran on 12/8/2017.
 */

public interface RandomWordService{
    @GET("{relative_path}")
    Call<RandomWord> getWord(@Path("relative_path") String relativePath);

//    @GET("randomword")
//    Call<RandomWord> getWord();
}
