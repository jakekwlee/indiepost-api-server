package com.indiepost.viewModel;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public class ImageResponse {

    private List<ImageMetaInformation> files;

    public List<ImageMetaInformation> getFiles() {
        return files;
    }

    public void setFiles(List<ImageMetaInformation> files) {
        this.files = files;
    }
}
