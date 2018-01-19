package com.scopemedia.android_similarsearch_demo;

import com.scopemedia.api.response.MediaResponse;

/**
 * @author Maikel Rehl on 3/8/2017.
 */

interface ImageSearchCallback {
    void start();
    void result(MediaResponse imageResult);
    void error(String error);
}
