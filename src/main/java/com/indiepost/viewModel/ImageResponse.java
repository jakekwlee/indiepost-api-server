package com.indiepost.viewModel;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public class ImageResponse {

    private List<ImageMeta> files;

    public List<ImageMeta> getFiles() {
        return files;
    }

    public void setFiles(List<ImageMeta> files) {
        this.files = files;
    }
}
