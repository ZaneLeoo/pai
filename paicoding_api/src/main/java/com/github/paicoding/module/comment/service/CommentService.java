package com.github.paicoding.module.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.paicoding.module.comment.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 09:58
 */
@Service
public interface CommentService extends IService<Comment> {

    void addComment(Comment comment);

    List<Comment> getCommentByArticleId(Long articleId);
}
