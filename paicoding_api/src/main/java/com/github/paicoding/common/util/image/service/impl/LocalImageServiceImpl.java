package com.github.paicoding.common.util.image.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.github.paicoding.common.entity.Response;
import com.github.paicoding.common.util.image.entity.ImageDTO;
import com.github.paicoding.common.util.image.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Zane Leo
 * @date 2025/3/26 21:48
 */
@Service
public class LocalImageServiceImpl implements ImageService {

    @Value("${image.upload}")
    private String BASE_IMAGE_SAVE_URL;

    @Value("${image.base-access-url}")
    private String BASE_IMAGE_ACCESS_URL;

    @Override
    public String upload(MultipartFile file) throws IOException {
        // 1. 获取原始文件名和文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Invalid file name");
        }
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 2. 使用 Hutool 的 IdUtil 生成唯一文件名
        String uniqueFileName = IdUtil.simpleUUID() + fileExtension;

        // 3. 构造保存路径
        String savePath = BASE_IMAGE_SAVE_URL + uniqueFileName;
        File destFile = new File(savePath);

        // 4. 保存文件到本地
        FileUtil.mkParentDirs(destFile);
        FileUtil.writeBytes(file.getBytes(), destFile);

        // 6. 构造图片访问 URL并返回
        return BASE_IMAGE_ACCESS_URL + uniqueFileName;
    }
}
