package com.acpha.photoapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.acpha.photoapp.Models.Favorites.FavoritesContract;
import com.acpha.photoapp.Models.Favorites.FavoritesDbHelper;
import com.acpha.photoapp.Models.Photo.Photo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andy on 12/29/2016.
 */

public class PhotoDetailActivity extends AppCompatActivity {
    Photo photo;
    Bundle bundle;
    @BindView(R.id.detail_image) ImageView detailImage;
    boolean cropped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        cropped = false;

        if(bundle != null) {
            photo = (Photo) bundle.getSerializable("photo");
            String large = photo.getLarge();
            Picasso.with(this).load(large).centerInside()
                    .resize(size.x,size.y)
                    .into(detailImage);
        }

        detailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photo != null) {
                    String imageUrl = photo.getUrl();
                    String large = photo.getLarge();
                    if(cropped) {
                        Picasso.with(getApplicationContext()).load(large)
                                .resize(size.x, size.y)
                                .centerInside()
                                .into(detailImage);
                        cropped = false;
                    }else{
//                        Toast.makeText(getApplicationContext(), Integer.toString(detailImage.getWidth())+" "
//                                +Integer.toString(detailImage.getHeight()),
//                                Toast.LENGTH_LONG).show();
                        Picasso.with(getApplicationContext()).load(large)
                                .resize(detailImage.getWidth(),detailImage.getHeight())
                                .centerCrop()
                                .into(detailImage);
                        cropped = true;
                    }
            }
        }});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.detail_favorites){
            //get writable db
            FavoritesDbHelper dbHelper = new FavoritesDbHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //create ContentValues to add row to db
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoriteEntry.COLUMN_NAME_URL, photo.getLarge());
            Toast.makeText(this, "Url saved to favorites.", Toast.LENGTH_LONG).show();
            //insert, returns primary key
            long newRowId = db.insert(FavoritesContract.FavoriteEntry.TABLE_NAME, null, values);
        }
        return super.onOptionsItemSelected(item);
    }
}
