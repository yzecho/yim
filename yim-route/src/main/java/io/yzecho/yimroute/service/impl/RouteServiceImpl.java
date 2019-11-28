package io.yzecho.yimroute.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.yzecho.yimcommon.constant.BasicConstant;
import io.yzecho.yimcommon.entity.Chat;
import io.yzecho.yimroute.service.RouteService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 17:21
 */
@Service
public class RouteServiceImpl implements RouteService {

    private OkHttpClient okHttpClient;

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

        // 调用server端api转发消息
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
    }
}
