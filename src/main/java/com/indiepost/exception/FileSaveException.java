package com.indiepost.exception;

import java.io.IOException;

/**
 * Created by jake on 8/20/16.
 */
public class FileSaveException extends IOException {
    public FileSaveException(String message) {
        super(message);
    }

    public FileSaveException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
