package io.yzecho.yimclient.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author yzecho
 * @desc
 * @date 11/12/2019 21:59
 */
@Component
public class ClientInfo {

    private Info info = new Info();

    public Info getInfo() {
        return info;
    }

    public ClientInfo saveUserInfo(Integer userId, String userName) {
        info.setUserId(userId);
        info.setUsername(userName);
        return this;
    }


    public ClientInfo saveServiceInfo(String serviceInfo) {
        info.setServiceInfo(serviceInfo);
        return this;
    }

    public ClientInfo saveStartDate() {
        info.setStartDate(LocalDateTime.now());
        return this;
    }

    @Data
    public static class Info {
        private String username;
        private Integer userId;
        private String serviceInfo;
        private LocalDateTime startDate;
    }
}
