package com.blog.blog.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentDto {

    @Size(max = 256)
    private String content;
    private Long userId;
    private Long postId;

    public CommentDto() {
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "content='" + content + '\'' +
                ", userId=" + userId +
                ", postId=" + postId +
                '}';
    }

    public CommentDto(String content, Long userId, Long postId) {
        this.content = content;
        this.userId = userId;
        this.postId = postId;
    }

    public boolean noEmptyFields(){
        return (content != null && userId != null && postId != null);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
