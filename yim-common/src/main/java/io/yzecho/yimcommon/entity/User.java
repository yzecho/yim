package io.yzecho.yimcommon.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yzecho
 * @desc 用户基本信息
 * @date 25/11/2019 11:17
 */
@Data
public class User implements Serializable {
    private int userId;
    private String username;
}
