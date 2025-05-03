package com.github.paicoding.module.admin.tag.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.module.admin.common.vo.PageParam;
import com.github.paicoding.module.admin.tag.service.TagSettingService;
import com.github.paicoding.module.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签后台管理 Controller
 */
@RestController
@RequestMapping("admin/tag")
@RequiredArgsConstructor
public class TagSettingController {

    private final TagSettingService tagSettingService;

    /**
     * 获取标签列表（分页）
     */
    @GetMapping("/list")
    public Response<Page<Tag>> listTags(PageParam pageParam) {
        Page<Tag> page = tagSettingService.getTagPage(pageParam);
        return Response.success(page);
    }

    /**
     * 添加标签
     */
    @PostMapping("/add")
    public Response<String> addTag(@RequestBody Tag tag) {
        // 可以在这里添加必要的输入验证
        boolean success = tagSettingService.addTag(tag);
        return success ? Response.success("添加成功") : Response.error("添加失败");
    }

    /**
     * 更新标签
     */
    @PostMapping("/update")
    public Response<String> updateTag(@RequestBody Tag tag) {
        // 可以在这里添加必要的输入验证（例如，检查ID是否存在）
        boolean success = tagSettingService.updateTag(tag);
        return success ? Response.success("更新成功") : Response.error("更新失败");
    }

    /**
     * 删除标签 (单个或批量)
     */
    @PostMapping("/delete")
    public Response<String> deleteTag(@RequestBody List<Long> ids) {
        boolean success = tagSettingService.deleteTag(ids);
        return success ? Response.success("删除成功") : Response.error("删除失败");
    }
} 