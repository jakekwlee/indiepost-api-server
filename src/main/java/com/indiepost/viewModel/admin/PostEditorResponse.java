package com.indiepost.viewModel.admin;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 11/19/16.
 */
public class PostEditorResponse {
    private Long id;

    private String title;

    private String content;

    private String excerpt;

    private String displayName;

    private Date createdAt;

    private Date modifiedAt;

    private Date publishedAt;

    private String featuredImage;

    private String status;

    private String postType;

    private Long authorId;

    private Long categoryId;

    private List<String> tags;


}
