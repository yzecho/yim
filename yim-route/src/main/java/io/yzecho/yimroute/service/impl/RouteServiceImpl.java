package io.yzecho.yimroute.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.yzecho.yimcommon.constant.BasicConstant;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimcommon.entity.User;
import io.yzecho.yimroute.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 17:21
 */
@Service
@Slf4j
public class RouteServiceImpl implements RouteService {

    private final static Map<Integer, User> USER_INFO_MAP = new ConcurrentHashMap<>(64);

    private OkHttpClient okHttpClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public RouteServiceImpl(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public void sendMessage(String url, Chat chat) throws IOException {
        JSONObject json = new JSONObject();
        json.put("command", chat.getCommand());
        json.put("time", chat.getTime());
        json.put("userId", chat.getUserId());
        json.put("content", chat.getContent());

        RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, json.toString());

        // 传给server端api转发消息
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
    }

    @Override
    public boolean saveAndCheckUserLoginStatus(Integer userId) {
        Long add = redisTemplate.opsForSet().add(BasicConstant.LOGIN_STATUS_PREFIX, userId.toString());
        return add != 0;
    }

    @Override
    public void removeLoginStatus(Integer userId) {
        redisTemplate.opsForSet().remove(BasicConstant.LOGIN_STATUS_PREFIX, userId.toString());
    }

    @Override
    public User loadUserByUserId(Integer userId) {
        User user = USER_INFO_MAP.get(userId);
        if (user != null) {
            return user;
        }
        String sendUsername = redisTemplate.opsForValue().get(BasicConstant.ACCOUNT_PREFIX + userId);
        if (sendUsername != null) {
            user = new User(userId, sendUsername);
            USER_INFO_MAP.put(userId, user);
        }
        return user;
    }

    @Override
    public Set<User> onlineUser() {
        Set<User> users = null;
        Set<String> members = redisTemplate.opsForSet().members(BasicConstant.LOGIN_STATUS_PREFIX);

        for (String member : members) {
            if (users == null) {
                users = new HashSet<>(64);
            }
            User user = loadUserByUserId(Integer.valueOf(member));
            users.add(user);
        }
        return users;
    }
}
