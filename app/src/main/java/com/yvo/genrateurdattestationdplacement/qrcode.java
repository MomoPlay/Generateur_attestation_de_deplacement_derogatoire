package com.yvo.genrateurdattestationdplacement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class qrcode extends AppCompatActivity {

    Context context = qrcode.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        ImageView ivQrcode = findViewById(R.id.ivQrCode);

        ivQrcode.setImageBitmap(getImageBitmap(context.getFileStreamPath("qrcode.png").toURI()));
    }

    private Bitmap getImageBitmap(URI uri) {
        Bitmap bm = null;
        try {
            URL aURL = uri.toURL();
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("", "Error getting bitmap", e);
        }
        return bm;
    }

    public void onShowPdfClick(View v)
    {
        Intent intent = new Intent(this, attestationViewer.class);
        startActivity(intent);
    }

    public void onBackClicked(View v)
    {
        finish();
    }
}
