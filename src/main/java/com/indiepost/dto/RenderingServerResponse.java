package com.indiepost.dto;

/**
 * Created by jake on 17. 2. 25.
 */
public class RenderingServerResponse {
    private String state;
    private String markup;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }
}
