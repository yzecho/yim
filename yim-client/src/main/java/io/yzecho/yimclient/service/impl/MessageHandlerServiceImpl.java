package io.yzecho.yimclient.service.impl;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import io.yzecho.yimclient.client.YimClient;
import io.yzecho.yimclient.config.InitConfiguration;
import io.yzecho.yimclient.pojo.ClientInfo;
import io.yzecho.yimclient.service.MessageHandlerService;
import io.yzecho.yimclient.service.RouteRequestService;
import io.yzecho.yimclient.vo.OnlineUsersVO;
import io.yzecho.yimclient.vo.PrivateVO;
import io.yzecho.yimcommon.constant.MessageConstant;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.enums.SystemCommandEnumType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yzecho
 * @desc
 * @date 09/12/2019 21:56
 */
@Slf4j
@Service
public class MessageHandlerServiceImpl implements MessageHandlerService {

    @Autowired
    private InitConfiguration configuration;

    @Autowired
    private YimClient yimClient;

    @Autowired
    private RouteRequestService routeRequestService;

    @Autowired
    private ClientInfo clientInfo;

    @Override
    public void sendMsg(String msg) throws IOException {
        String[] totalMsg = msg.split("::");
        if (totalMsg.length > 1) {
            PrivateVO privateVO = new PrivateVO();
            privateVO.setUserId(configuration.getUserId());
            privateVO.setReceivedUserId(Integer.parseInt(totalMsg[0]));
            privateVO.setContent(totalMsg[1]);

            privateChat(privateVO);
        } else {
            Chat chat = new Chat(MessageConstant.CHAT, System.currentTimeMillis(), configuration.getUserId(), msg);
            yimClient.sendMessage(chat);
        }
    }

    @Override
    public void privateChat(PrivateVO privateVO) throws IOException {
        routeRequestService.sendPrivateMsg(privateVO);
    }

    @Override
    public boolean innerCommand(String msg) throws Exception {
        if (msg.startsWith(":")) {
            Map<String, String> allStatusCode = SystemCommandEnumType.getAllStatusCode();

            if (msg.split(" ").length > 1 && SystemCommandEnumType.EMOJI.getCommandType().trim().equals(msg.split(" ")[0])) {
                emojiCommand(msg);
            } else if (SystemCommandEnumType.QUIT.getCommandType().trim().equals(msg)) {
                shutdown();
            } else if (SystemCommandEnumType.ALL.getCommandType().trim().equals(msg)) {
                printAllCommand(allStatusCode);
            } else if (SystemCommandEnumType.ONLINE_USER.getCommandType().trim().equals(msg)) {
                printOnlineUsers();
            } else if (SystemCommandEnumType.INFO.getCommandType().trim().equals(msg)) {
                clientInfoCommand(msg);
            } else {
                log.error("您输入的指令暂无，请重新输入");
                printAllCommand(allStatusCode);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void emojiCommand(String msg) {
        if (msg.split(" ").length <= 1) {
            log.info("incorrect command, :emoji [option]");
            return;
        }
        String value = msg.split(" ")[1];
        if (value != null) {
            int index = Integer.parseInt(value);
            List<Emoji> allEmoji = (List<Emoji>) EmojiManager.getAll();
            allEmoji = allEmoji.subList(5 * index, 5 * index + 10);

            allEmoji.forEach(emoji -> log.info(EmojiParser.parseToAliases(emoji.getUnicode()) + "----->" + emoji.getUnicode()));
        }
    }

    @Override
    public void clientInfoCommand(String msg) {
        log.info("----------------------------");
        log.info("Client Info:");
        log.info("LoginTime:{}", clientInfo.getInfo().getStartDate());
        log.info("ServiceInfo:{}", clientInfo.getInfo().getServiceInfo());
        log.info("UserInfo[userId:{} username:{}]", clientInfo.getInfo().getUserId(), clientInfo.getInfo().getUsername());
        log.info("----------------------------");
    }

    private void printAllCommand(Map<String, String> allStatusCode) {
        log.info("----------------------------");
        allStatusCode.forEach((key, value) -> log.info(key + "----->" + value));
        log.info("----------------------------");
    }

    private void printOnlineUsers() throws Exception {
        List<OnlineUsersVO.DataBodyBean> onlineUsers = routeRequestService.onlineUsers();

        log.info("----------------------------");
        onlineUsers.forEach(onlineUser -> log.info("userId={}=====username={}", onlineUser.getUserId(), onlineUser.getUsername()));
        log.info("----------------------------");
    }


    private void shutdown() {
        log.info("系统关闭中...");

        yimClient.clear();
        System.exit(0);
    }
}
