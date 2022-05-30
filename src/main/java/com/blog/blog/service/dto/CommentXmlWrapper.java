package com.blog.blog.service.dto;

import com.blog.blog.domain.Comment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "comments")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentXmlWrapper {

    @XmlElement(name = "comment")
    List<CommentXmlDto> comments;

    public CommentXmlWrapper() {
        comments = new ArrayList<>();
    }

    public CommentXmlWrapper(List<Comment> comments) {
        this.comments = new ArrayList<>();
        for (Comment comment : comments) {
            try {
                this.comments.add(new CommentXmlDto(
                        comment.getId(),
                        comment.getContent(),
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(String.valueOf(comment.getCreateTime())),
                        comment.getPost().getId(),
                        comment.getUser().getId()
                ));
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    public Object addComments(List<Comment> comments) throws DatatypeConfigurationException {
        for (Comment comment : comments) {
            this.comments.add(new CommentXmlDto(
                comment.getId(),
                comment.getContent(),
                DatatypeFactory.newInstance().newXMLGregorianCalendar(String.valueOf(comment.getCreateTime())),
                comment.getPost().getId(),
                comment.getUser().getId()
            ));
        }
        return this;
    }

    public List<CommentXmlDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentXmlDto> comments) {
        this.comments = comments;
    }
}
