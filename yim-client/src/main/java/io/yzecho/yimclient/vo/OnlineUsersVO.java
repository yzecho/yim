package io.yzecho.yimclient.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 09/12/2019 22:01
 */
@Data
public class OnlineUsersVO {

    private String code;
    private String message;
    private List<DataBodyBean> dataBody;

    @Data
    public static class DataBodyBean {
        private Integer userId;
        private String username;
    }
}
