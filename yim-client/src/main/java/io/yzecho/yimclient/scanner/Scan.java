package io.yzecho.yimclient.scanner;

import com.vdurmont.emoji.EmojiParser;
import io.yzecho.yimclient.client.YimClient;
import io.yzecho.yimclient.config.InitConfiguration;
import io.yzecho.yimclient.config.SpringBeanFactory;
import io.yzecho.yimclient.service.MessageHandlerService;
import io.yzecho.yimcommon.constant.MessageConstant;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author yzecho
 * @desc
 * @date 26/11/2019 08:42
 */
@Slf4j
public class Scan implements Runnable {

    private YimClient client;
    private InitConfiguration configuration;

    private MessageHandlerService messageHandlerService;

    public Scan() {
        client = SpringBeanFactory.getBean(YimClient.class);
        configuration = SpringBeanFactory.getBean(InitConfiguration.class);
        messageHandlerService = SpringBeanFactory.getBean(MessageHandlerService.class);
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String msg = scanner.nextLine();
                if (StringUtil.isEmpty(msg)) {
                    log.info("不允许发送空消息");
                    continue;
                }

                //处理系统指令
                if (MessageConstant.LOGOUT.equals(msg)) {
                    client.clear();
                    log.info("下线成功，如需继续通讯，请重新登录");
                    continue;
                } else if (MessageConstant.LOGIN.equals(msg)) {
                    client.start();
                    log.info("登录成功");
                    continue;
                } else if (messageHandlerService.innerCommand(msg)) {
                    continue;
                }

                messageHandlerService.sendMsg(msg);

                log.info("{}:{}", configuration.getUsername(), EmojiParser.parseToUnicode(msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
