package com.example.auth1;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AirtableApiService {
    private static final String BASE_URL = "https://api.airtable.com/v0/appmrsQYtC08bqPZC/Auctionbase";
    private static final String API_KEY = "keyMYAJt2ewDCdjgN";
    private static final String BASE_ID = "appmrsQYtC08bqPZC";

    private OkHttpClient client;

    public AirtableApiService() {
        client = new OkHttpClient();
    }

    public String makeApiRequest(String endpoint) throws IOException {
        String url = BASE_URL + BASE_ID + "/" + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
