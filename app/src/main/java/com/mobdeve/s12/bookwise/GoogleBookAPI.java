package com.mobdeve.s12.bookwise;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GoogleBookAPI {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";

    // Step 1: Set up Retrofit
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to Java Objects
            .build();

    // Step 2: Create API Interface
    /*public interface GoogleBooksService {
        @GET("volumes")
        Call<ApiResponse> getBooks(@Query("q") String query); // Replace with correct API endpoint
    }*/

    public interface GoogleBooksService {
        @GET("volumes")
        Call<ApiResponse> getBooks(@Query("q") String query,
                                   @Query("startIndex") int startIndex, // To support pagination
                                   @Query("maxResults") int maxResults); // Limit the number of results
    }

    private GoogleBooksService service = retrofit.create(GoogleBooksService.class);



    public void fetchBooks(String searchQuery, OnBooksFetchedListener listener) {
        // Set the maxResults limit to 20
        int maxResults = 20;

        // Call the API and set the maxResults and query
        service.getBooks(searchQuery, 0, maxResults).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Convert API response to Bookitem objects
                    ArrayList<Bookitem> bookitems = new ArrayList<>();
                    for (ApiBook apiBook : response.body().getItems()) {
                        Bookitem book = new Bookitem(
                                apiBook.getTitle(),
                                apiBook.getAuthor(),
                                apiBook.getGenres(),
                                apiBook.getSummary(),
                                0, // Initial rating as 0
                                apiBook.getDay(),
                                apiBook.getMonth(),
                                apiBook.getYear(),
                                apiBook.getImageURL()// Assuming you have an imageID or URL from the API
                        );
                        bookitems.add(book);
                    }
                    listener.onBooksFetched(bookitems); // Pass data back to the UI or database
                } else {
                    listener.onError("Error fetching books");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    // Listener to pass data back to the calling activity
    public interface OnBooksFetchedListener {
        void onBooksFetched(List<Bookitem> books);
        void onError(String errorMessage);
    }
}
