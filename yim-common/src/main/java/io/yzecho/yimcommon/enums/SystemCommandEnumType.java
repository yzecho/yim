package io.yzecho.yimcommon.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yzecho
 * @desc
 * @date 09/12/2019 21:16
 */
public enum SystemCommandEnumType {
    /**
     * 获取系统全部命令
     */
    ALL(":allCommand", "获取系统全部命令"),

    /**
     * 获取所有在线用户
     */
    ONLINE_USER(":allOnlineUser", "获取所有在线用户"),

    /**
     * emoji
     */
    EMOJI(":emoji", "emoji表情列表(格式为 :emoji 页数)"),

    /**
     * 获取客户单信息
     */
    INFO(":clientInfo", "获取客户端信息"),

    /**
     * 退出系统
     */
    QUIT(":quit", "退出系统");

    /**
     * 枚举值码
     */
    private final String commandType;

    /**
     * 枚举描述
     */
    private final String desc;

    /**
     * 构建一个
     *
     * @param commandType
     * @param desc
     */
    SystemCommandEnumType(String commandType, String desc) {
        this.commandType = commandType;
        this.desc = desc;
    }

    public String getCommandType() {
        return commandType;
    }

    public String getDesc() {
        return desc;
    }

    public String message() {
        return desc;
    }

    public String code() {
        return commandType;
    }

    /**
     * 获取全部枚举值码
     *
     * @return
     */
    public static Map<String, String> getAllStatusCode() {
        ArrayList<String> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>(16);

        for (SystemCommandEnumType value : values()) {
            list.add(value.code());
            map.put(value.getCommandType(), value.getDesc());
        }
        return map;
    }
}
