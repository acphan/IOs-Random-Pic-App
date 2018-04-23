package com.acpha.photoapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.acpha.photoapp.Models.Favorites.FavoritesContract;
import com.acpha.photoapp.Models.Favorites.FavoritesDbHelper;
import com.acpha.photoapp.Models.Photo.Photo;
import com.acpha.photoapp.Models.Photo.PhotoData;
import com.acpha.photoapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andy on 12/29/2016.
 */

public class FavoritesActivity extends AppCompatActivity {
    ArrayList<String> urls;

    @BindView(R.id.favorites_recycler_view) RecyclerView favoritesRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        //String[] projection = {FavoritesContract.FavoriteEntry.COLUMN_NAME_URL};
        urls = new ArrayList<>();
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
            FavoritesContract.FavoriteEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null);
        c.moveToFirst();
        if(c != null && c.getCount() > 0) {
            do{
                urls.add(c.getString(1));
            }while(c.moveToNext());
        }else{
            urls = new ArrayList<>();
        }

        favoritesRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        favoritesRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FavoriteAdapter(this, urls);
        favoritesRecyclerView.setAdapter(mAdapter);
        //Toast.makeText(this, urls.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorites, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_clear_favorites){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.clear_favorites_message);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    FavoritesDbHelper dbHelper = new FavoritesDbHelper(getApplicationContext());
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL(FavoritesDbHelper.getSqlDeleteEntries());
                    db.execSQL(FavoritesDbHelper.getSqlCreateEntries());
                    urls.clear();
                    mAdapter.notifyDataSetChanged();

                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
