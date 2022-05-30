package com.blog.blog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="post_status")
public class PostStatus implements Serializable {
    @Id
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostStatus that = (PostStatus) o;
        return status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return "PostStatus{" +
                "status='" + status + '\'' +
                '}';
    }

    public PostStatus() {
    }

    public PostStatus(String status) {
        this.status = status;
    }
}
