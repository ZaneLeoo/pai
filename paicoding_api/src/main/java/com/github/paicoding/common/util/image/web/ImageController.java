package com.github.paicoding.common.util.image.web;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.common.util.image.entity.ImageDTO;
import com.github.paicoding.common.util.image.service.ImageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author Zane Leo
 * @date 2025/3/26 22:13
 */
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Resource
    private ImageService imageService;

    @PostMapping("/upload")
    public Response<ImageDTO> upload(@RequestParam MultipartFile file) throws IOException {
        String link = imageService.upload(file);
        return Response.success(new ImageDTO(link));
    }

    /**
     * Froala 要求返回的是 link 字段的JSON字段
     * @param file 上传的图片
     * @return {link:"link"}
     */
    @PostMapping("/uploadForFroala")
    public Map<String, String> uploadForFroala(@RequestParam MultipartFile file) throws IOException {
        String link = imageService.upload(file);
        return Map.of("link", link);
    }
}
