package com.zhiyu.service;

import com.zhiyu.domain.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Long countComments();

    Comment saveComment(Comment comment);
}
