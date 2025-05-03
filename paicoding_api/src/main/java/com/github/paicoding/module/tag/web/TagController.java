package com.github.paicoding.module.tag.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.tag.entity.Tag;
import com.github.paicoding.module.tag.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zane Leo
 * @date 2025/3/27 09:55
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 获取所有的标签信息
     * @return 标签列表
     */
    @GetMapping("/all")
    public Response<List<Tag>> getAllTag() {
        List<Tag> list = tagService.list();
        return Response.success(list);
    }
}


















