package com.example.mylyrics;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HuggingFaceAPI {
    @Headers({
            "Authorization: Bearer your_token"
    })
    @POST("models/facebook/bart-large-cnn")
    Call<List<SummaryResponse.Summary>> getSummary(@Body TextInput input);

    class TextInput {
        public String inputs;

        public TextInput(String inputs) {
            this.inputs = inputs;
        }
    }
}
