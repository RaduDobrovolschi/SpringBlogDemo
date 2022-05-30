package com.blog.blog.service.dto;

import com.opencsv.bean.CsvBindByPosition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;

@XmlRootElement(name = "comment")
@XmlType(propOrder={"id", "content", "createTime", "postId", "userId"})
public class CommentXmlDto {
    private Long id;
    private String Content;
    private XMLGregorianCalendar CreateTime;
    private Long PostId;
    private Long UserId;

    public CommentXmlDto() {
    }

    public CommentXmlDto(Long id, String content, XMLGregorianCalendar createTime, Long postId, Long userId) {
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

    public XMLGregorianCalendar getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(XMLGregorianCalendar createTime) {
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
