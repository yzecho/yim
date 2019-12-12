package io.yzecho.yimroute.vo;

import lombok.Data;

/**
 * @author yzecho
 * @desc
 * @date 09/12/2019 23:14
 */
@Data
public class PrivateVO {
    private Integer userId;
    private Integer receivedUserId;
    private String content;
}
