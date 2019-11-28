package io.yzecho.yimcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yzecho
 * @desc 通讯基本信息
 * @date 25/11/2019 11:17
 */
@Data
@AllArgsConstructor
public class Chat implements Serializable {

    private String command;
    private Long time;
    private Integer userId;
    private String content;
}
