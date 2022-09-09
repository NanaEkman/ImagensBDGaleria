package com.android.imagensbdgaleria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnCarregar;
    private ListView listaImagens;

    SQLiteDatabase bd;

    ImagensAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCarregar = findViewById( R.id.btnCarregar );
        listaImagens = findViewById( R.id.listaImagens );

        btnCarregar.setOnClickListener( new EscutadorBotao() );
        listaImagens.setOnItemClickListener( new EscutadorCliqueLista() );

        bd = openOrCreateDatabase( "imagens", MODE_PRIVATE, null);

        bd.execSQL( "CREATE TABLE IF NOT EXISTS tabImagens ( img BLOB )" );

        Cursor cursor = bd.rawQuery( "SELECT _rowid_ _id, img FROM tabimagens", null );
        adapter = new ImagensAdapter( this, cursor );
        listaImagens.setAdapter(adapter);

    }

    private class EscutadorBotao implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Intent i = new Intent();

            i.setType("image/*");
            i.setAction( Intent.ACTION_GET_CONTENT );

            startActivityForResult( Intent.createChooser(i, "Selecione a Imagem"), 1);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
                byte[] imgBytes = outputStream.toByteArray();

                ContentValues cv = new ContentValues();
                cv.put("img", imgBytes);
                bd.insert( "tabimagens", null, cv );

                Cursor cursor = bd.rawQuery( "SELECT _rowid_ _id, img FROM tabimagens", null );
                adapter.changeCursor(cursor);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private class EscutadorCliqueLista implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent = new Intent( getApplicationContext(), ExibeActivity.class);
            Cursor c = (Cursor) adapter.getItem(i);

            byte[] imgBytes = c.getBlob( c.getColumnIndex( "img" ) );
            intent.putExtra("img", imgBytes);

            startActivity(intent);

        }
    }

}