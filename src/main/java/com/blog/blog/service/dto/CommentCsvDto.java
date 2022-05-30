package com.blog.blog.service.dto;

import com.opencsv.bean.CsvBindByPosition;
import org.springframework.core.SpringVersion;

import java.io.PrintStream;
import java.time.Instant;
import java.util.Objects;

public class CommentCsvDto {
    @CsvBindByPosition(position = 0)
    private Long id;
    @CsvBindByPosition(position = 1)
    private String Content;
    @CsvBindByPosition(position = 2)
    private Instant CreateTime;
    @CsvBindByPosition(position = 3)
    private Long PostId;
    @CsvBindByPosition(position = 4)
    private Long UserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentCsvDto that = (CommentCsvDto) o;
        return id.equals(that.id) && Content.equals(that.Content) && CreateTime.equals(that.CreateTime) && PostId.equals(that.PostId) && UserId.equals(that.UserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Content, CreateTime, PostId, UserId);
    }

    public CommentCsvDto() {
    }

    public CommentCsvDto(Long id, String content, Instant createTime, Long postId, Long userId) {
        this.id = id;
        Content = content;
        CreateTime = createTime;
        PostId = postId;
        UserId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Instant getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Instant createTime) {
        CreateTime = createTime;
    }

    public Long getPostId() {
        return PostId;
    }

    public void setPostId(Long postId) {
        PostId = postId;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }
}
