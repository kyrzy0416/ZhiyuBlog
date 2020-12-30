package com.zhiyu.dao;

import com.zhiyu.domain.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    List<Comment> findByBlogIdAndParentCommentNull(Long id, Sort sort);

}
