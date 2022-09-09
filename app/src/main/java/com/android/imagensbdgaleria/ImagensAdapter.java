package com.android.imagensbdgaleria;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

public class ImagensAdapter  extends CursorAdapter {

    public ImagensAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.item_lista, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView img = view.findViewById( R.id.imgExibe );
        byte[] imgBytes = cursor.getBlob( cursor.getColumnIndex( "img" ) );
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        img.setImageBitmap( imgBitmap );

    }
}