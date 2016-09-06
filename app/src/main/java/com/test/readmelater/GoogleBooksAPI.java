package com.test.readmelater;

import com.test.readmelater.googleApiModels.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by audreyeso on 9/5/16.
 */
public interface GoogleBooksAPI {

    @GET("volumes")
    Call<Example> getBookDescription(@Query("q") String isbnNum);
}
