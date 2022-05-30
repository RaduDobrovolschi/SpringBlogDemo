package com.blog.blog.service.dto;

import org.apache.juli.logging.Log;

public class PostDto {
    private String content;
    private String title;
    private Long userId;
    private String status;

    public boolean noEmptyFields(){
        return (content != null && title != null && userId != null && status != null);
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PostDto() {
    }

    public PostDto(String content, String title, Long userId, String status) {
        this.content = content;
        this.title = title;
        this.userId = userId;
        this.status = status;
    }
}
