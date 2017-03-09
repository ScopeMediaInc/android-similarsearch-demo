package com.scopemedia.android_similarsearch_demo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Maikel Rehl on 3/8/2017.
 */
public class MainActivity extends AppCompatActivity implements ImageSearchCallback {

    // YOUR CONFIGURATION
    public static final String CLIENT_ID = "demo";
    public static final String CLIENT_SECRET = "demotestsecret";
    public static final String APPLICATION_ID = "fashion";

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final int GALLERY_REQUEST_CODE = 20;

    private RecyclerView rView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE
            );
        }
        else
            init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Search...");
        progressDialog.setCanceledOnTouchOutside(false);

        GridLayoutManager lLayout = new GridLayoutManager(MainActivity.this, 3);

        rView = (RecyclerView)findViewById(R.id.resultList);
        rView.setLayoutManager(lLayout);

        Button chooseImageButton = (Button) findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void start() {
        progressDialog.show();
    }

    @Override
    public void result(ImageResult imageResult) {
        if (imageResult == null || imageResult.getMedias() == null) return;
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(MainActivity.this, imageResult.getMedias());
        rView.setAdapter(rcAdapter);
        progressDialog.dismiss();
    }

    @Override
    public void error(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                searchBase64(picturePath, this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this, "Please grant the permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void searchBase64(String path, ImageSearchCallback callback) {
        ImageSearchTask imageSearchTask = new ImageSearchTask(callback);
        imageSearchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path);
    }
}
