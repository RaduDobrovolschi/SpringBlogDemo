package com.blog.blog.service.dto;

import com.blog.blog.domain.Comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

public class CommentDto {

    @Size(max = 256)
    private String content;
    private Long userId;
    private Long postId;

    public CommentDto() {
    }

    public CommentDto content(String content){
        this.content = content;
        return this;
    }

    public CommentDto userId(Long id){
        this.userId = id;
        return this;
    }

    public CommentDto postId(Long id){
        this.postId = id;
        return this;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "content='" + content + '\'' +
                ", userId=" + userId +
                ", postId=" + postId +
                '}';
    }

    public CommentDto(Comment comment){
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.userId = comment.getUser().getId();
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
