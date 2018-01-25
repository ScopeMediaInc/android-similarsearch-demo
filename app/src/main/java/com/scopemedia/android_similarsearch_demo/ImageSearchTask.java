package com.scopemedia.android_similarsearch_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.scopemedia.api.client.ScopeCheckBuilder;
import com.scopemedia.api.client.ScopeCheckClient;
import com.scopemedia.api.request.SimilarImageRequest;
import com.scopemedia.api.response.MediaResponse;



import static com.scopemedia.android_similarsearch_demo.MainActivity.CLIENT_ID;
import static com.scopemedia.android_similarsearch_demo.MainActivity.CLIENT_SECRET;


/**
 * @author Maikel Rehl on 3/8/2017.
 */
class ImageSearchTask extends AsyncTask<String, Void, MediaResponse> {
    private static ScopeCheckClient client;

    private ImageSearchCallback callback;
    private boolean error = false;

    static {
        client = new ScopeCheckBuilder(CLIENT_ID, CLIENT_SECRET).build();
    }
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
    protected MediaResponse doInBackground(String... params) {
        try {
            //@TODO need to reduce the image to make sure long side less than 500px, otherwise will receive timeout error
            String encodedImage = encodeImageInBase64(params[0]);
            SimilarImageRequest request = new SimilarImageRequest();
            request.setMediaAsBase64(encodedImage);
            MediaResponse response = client.getSimilarImages(request).performSync();
            if (response == null || response.getCode() != 200) {
                error = true;
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
            MediaResponse errRes = new MediaResponse();
            errRes.setCode(-1);
            errRes.setMessage(e.getMessage());
            return errRes;
        }
    }

    @Override
    protected void onPostExecute(MediaResponse response) {
        super.onPostExecute(response);

        if (error)
            callback.error(response.getMessage());
        else {
            callback.result(response);
        }
    }

    private String encodeImageInBase64(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }
}
