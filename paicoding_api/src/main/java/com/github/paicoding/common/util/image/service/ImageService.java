package com.github.paicoding.common.util.image.service;

import com.github.paicoding.common.entity.Response;
import com.github.paicoding.common.util.image.entity.ImageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Zane Leo
 * @date 2025/3/26 21:471
 */
@Service
public interface ImageService {

    String upload(MultipartFile file) throws IOException;
}
