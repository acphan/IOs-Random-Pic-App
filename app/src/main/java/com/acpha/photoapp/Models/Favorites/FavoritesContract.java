package com.acpha.photoapp.Models.Favorites;

import android.provider.BaseColumns;

/**
 * Created by Andy on 12/29/2016.
 */

public final class FavoritesContract {
    private FavoritesContract(){}
    public static class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_URL = "url";
    }

}
