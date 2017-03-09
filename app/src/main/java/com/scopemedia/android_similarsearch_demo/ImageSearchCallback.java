package com.scopemedia.android_similarsearch_demo;

/**
 * @author Maikel Rehl on 3/8/2017.
 */

interface ImageSearchCallback {
    void start();
    void result(ImageResult imageResult);
    void error(String error);
}
