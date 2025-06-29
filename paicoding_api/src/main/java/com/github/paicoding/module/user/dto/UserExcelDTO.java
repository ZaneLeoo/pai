package com.github.paicoding.module.user.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zane Leo
 * @date 2025/6/17 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExcelDTO {
    @ExcelProperty("姓名")
    private String username;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("性别")
    private String gender;
}

