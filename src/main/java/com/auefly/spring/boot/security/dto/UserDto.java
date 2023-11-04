package com.auefly.spring.boot.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotEmpty(message = "名字不可为空！")
    @Size(min = 3, max = 20, message = "请输入3~20字名字！")
    private String name;

    @NotEmpty(message = "邮箱不可为空！")
    @Email(message = "邮箱格式错误!")
    private String email;

    @NotEmpty(message = "密码不可为空！")
    @Size(min = 6, max = 16, message = "请输入6~16位密码!")
    private String password;
}
