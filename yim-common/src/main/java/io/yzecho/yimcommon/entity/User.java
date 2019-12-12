package io.yzecho.yimcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yzecho
 * @desc 用户基本信息
 * @date 25/11/2019 11:17
 */
@Data
@AllArgsConstructor
public class User implements Serializable {
    private Integer userId;
    private String username;
}
