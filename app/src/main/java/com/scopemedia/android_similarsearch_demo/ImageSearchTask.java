package com.scopemedia.android_similarsearch_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.scopemedia.android_similarsearch_demo.MainActivity.APPLICATION_ID;
import static com.scopemedia.android_similarsearch_demo.MainActivity.CLIENT_ID;
import static com.scopemedia.android_similarsearch_demo.MainActivity.CLIENT_SECRET;

/**
 * @author Maikel Rehl on 3/8/2017.
 */
class ImageSearchTask extends AsyncTask<String, Void, ImageResult> {
    private static final String SEARCH_URL = "https://api.scopemedia.com/search-service/api/v1/"
            + "search/similar?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ImageSearchCallback callback;
    private boolean error = false;

    ImageSearchTask(ImageSearchCallback callback) {
        this.callback = callback;
        if (this.callback == null) {
            throw new IllegalArgumentException("Please set ImageSearchCallback");
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.start();
    }

    @Override
    protected ImageResult doInBackground(String... params) {
        try {
            String encodedImage = encodeImage(params[0]);
            JSONObject bodyParams = new JSONObject();
            bodyParams.put("appId", APPLICATION_ID);
            bodyParams.put("encodedMediaFile", encodedImage);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, bodyParams.toString());
            Request request = new Request.Builder()
                    .url(SEARCH_URL)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();

            if (response.code() >= 400) {
                error = true;
            }
            else {
                Gson gson = new Gson();
                return gson.fromJson(response.body().string(), ImageResult.class);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            error = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ImageResult imageResult) {
        super.onPostExecute(imageResult);

        if (error)
            callback.error("Sorry something went wrong");
        else {
            callback.result(imageResult);
        }
    }

    private String encodeImage(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }
}
