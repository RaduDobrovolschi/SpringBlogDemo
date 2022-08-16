package com.blog.blog.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name="comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "content", nullable = false, length = 256)
    private String content;

    @Column(name = "create_time")
    private Instant createTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    public Comment(){
    }

    public Comment(Long id, @NotNull @Size(max = 256) String content, Instant createTime, User user, Post post) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.user = user;
        this.post = post;
    }

    public Comment(@NotNull @Size(max = 256) String content, Instant createTime, User user, Post post) {
        this.content = content;
        this.createTime = createTime;
        this.user = user;
        this.post = post;
    }

    public Comment content(String content){
        this.content = content;
        return this;
    }

    public Comment createTime(Instant createTime){
        this.createTime = createTime;
        return this;
    }
    public Post getPost(){return post;}

    public void setPost(Post post){this.post = post;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id) && content.equals(comment.content) && createTime.equals(comment.createTime) && user.equals(comment.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createTime, user);
    }
}
