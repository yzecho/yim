package io.yzecho.yimclient.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 10:48
 */
@Component
@Data
public class InitConfiguration {

    @Value("${yim.user.id}")
    private int userId;

    @Value("${yim.user.username}")
    private String username;

    @Value("${yim.route.login.url}")
    private String routeLoginUrl;

    @Value("${yim.route.chat.url}")
    private String routeChatUrl;

    @Value("${yim.route.logout.url}")
    private String routeLogoutUrl;
}
