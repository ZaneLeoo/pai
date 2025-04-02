package com.github.paicoding.module.comment.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.comment.entity.Comment;
import com.github.paicoding.module.comment.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zane Leo
 * @date 2025/3/27 10:00
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public Response<String> addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
        return Response.success("添加评论成功!");
    }
}
