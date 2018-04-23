package com.acpha.photoapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.acpha.photoapp.Models.Favorites.FavoritesDbHelper;
import com.acpha.photoapp.Models.Photo.Photo;
import com.acpha.photoapp.Models.Photo.PhotoData;
import com.acpha.photoapp.Models.Search.SearchData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andy on 12/29/2016.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.photo_recycler_view) RecyclerView photoRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Photo> photos;

    @BindString(R.string.application_id) String application_id;
    @BindString(R.string.url_photos) String url_photos;
    @BindString(R.string.url_base) String url_base;

    int randPageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        photos = new ArrayList<>();
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchPhotos(intent.getStringExtra("query"), 1);
        }else {
            getPhotos();
        }
        //clear db
        SQLiteDatabase db = new FavoritesDbHelper(this).getWritableDatabase();
        //db.execSQL(FavoritesDbHelper.getSqlDeleteEntries());
        db.execSQL(FavoritesDbHelper.getSqlCreateEntries());
    }
    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction()) &&
                intent.hasExtra("query")) {
            searchPhotos(intent.getStringExtra("query"), 1);
        }else {
            getPhotos();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.main_favorites){
            //Toast.makeText(this,"FavoritesActivity",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.main_search){
            //Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
            onSearchRequested();
        }
        if(item.getItemId() == R.id.main_random){
            randPageNumber++;
            //Toast.makeText(this, "Page: "+randPageNumber, Toast.LENGTH_SHORT).show();
            if(!getPhotos()){
                randPageNumber = 1;
                getPhotos();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    protected boolean getPhotos(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url_base)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UnsplashService service = retrofit.create(UnsplashService.class);
        Call<List<PhotoData>> data = service.listPhotos(application_id, randPageNumber);
        boolean success = true;
        try {
            data.enqueue(new Callback<List<PhotoData>>() {
                @Override
                public void onResponse(Call<List<PhotoData>> call, Response<List<PhotoData>> response) {
                    List<PhotoData> photoData = response.body();
                    if(photoData != null){
                        photos.clear();
                    }
                    for(PhotoData data: photoData){
                        photos.add(
                                new Photo(
                                        data.getId(),
                                        data.getUser().getName(),
                                        data.getUser().getUsername(),
                                        data.getUrls().getThumb(),
                                        data.getUrls().getFull()
                                ));
                        //Log.e("PhotoData: ", data.getUser().getUsername());
                    }
                    for(Photo p : photos){
                        Log.e("Photo: ", p.getSmall());
                    }
                    photoRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    photoRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new PhotoAdapter(getApplicationContext(), photos);
                    photoRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onFailure(Call<List<PhotoData>> call, Throwable t) {
                    //Picasso.with(getApplicationContext()).load("https://images.unsplash.com/profile-fb-1461218156-c196eaad09a4.jpg").into(picture);
                }
            });
        }catch(Exception ex){
            success = false;
        }
        return success;
    }
    protected void searchPhotos(String query,int pages){
        photos.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url_base)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UnsplashService service = retrofit.create(UnsplashService.class);
        Call<SearchData> data = service.searchPhotos(application_id,query,pages);
        try {
            data.enqueue(new Callback<SearchData>() {
                @Override
                public void onResponse(Call<SearchData> call, Response<SearchData> response) {
                    SearchData searchData = response.body();
                    List<PhotoData> photoData = searchData.getResults();
                    for(PhotoData data: photoData){
                        photos.add(
                                new Photo(
                                        data.getId(),
                                        data.getUser().getName(),
                                        data.getUser().getUsername(),
                                        data.getUrls().getThumb(),
                                        data.getUrls().getFull()
                                ));
                        //Log.e("PhotoData: ", data.getUser().getUsername());
                    }
                    for(Photo p : photos){
                        Log.e("Photo: ", p.getSmall());
                    }
                    photoRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    photoRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new PhotoAdapter(getApplicationContext(), photos);
                    photoRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onFailure(Call<SearchData> call, Throwable t) {
                    //Picasso.with(getApplicationContext()).load("https://images.unsplash.com/profile-fb-1461218156-c196eaad09a4.jpg").into(picture);
                }
            });
        }catch(Exception ex){}
    }
}
