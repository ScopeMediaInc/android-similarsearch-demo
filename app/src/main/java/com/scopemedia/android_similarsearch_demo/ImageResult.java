package com.scopemedia.android_similarsearch_demo;

import java.io.Serializable;

/**
 * @author Maikel Rehl on 3/8/2017.
 */

class ImageResult implements Serializable {
    private String result;
    private String errorMessage;
    private int requestId;
    private int page;
    private int size;
    private int[] mediaIds;
    private String[] medias;

    String getResult() {
        return result;
    }

    String getErrorMessage() {
        return errorMessage;
    }

    int getRequestId() {
        return requestId;
    }

    int getPage() {
        return page;
    }

    int getSize() {
        return size;
    }

    int[] getMediaIds() {
        return mediaIds;
    }

    String[] getMedias() {
        return medias;
    }
}
