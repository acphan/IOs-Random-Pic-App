package com.acpha.photoapp;

import com.acpha.photoapp.Models.Photo.PhotoData;
import com.acpha.photoapp.Models.Search.SearchData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Andy on 12/29/2016.
 */

public interface UnsplashService {
    @GET("photos/?client_id=7a351404b1d49701eaea4c1a8eff9ba0e0ef7d36b94513dc3fbf5093b900a47b")
    Call<List<PhotoData>> listPhotos(@Query("client_id") String client_id, @Query("page") int page);

    @GET("search/photos/?")
    Call<SearchData> searchPhotos(@Query("client_id") String client_id, @Query("query") String query, @Query("page") int page);

    //@GET("search/")

}
