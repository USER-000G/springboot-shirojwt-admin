package com.have.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.have.util.Constants.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @ClassName User
 * @Description TODO
 * @Author G
 * @Date 2019/5/24 14:43
 * @Version 1.0
 **/
@Data
@Component
public class User {

    private Long id;
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "昵称不能为空")
    private String nickname;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date reg_time;

}
