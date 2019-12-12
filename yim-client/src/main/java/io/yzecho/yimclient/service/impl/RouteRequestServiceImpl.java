package io.yzecho.yimclient.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.yzecho.yimclient.service.RouteRequestService;
import io.yzecho.yimclient.vo.OnlineUsersVO;
import io.yzecho.yimclient.vo.PrivateVO;
import io.yzecho.yimcommon.constant.BasicConstant;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 09/12/2019 22:08
 */
@Service
@Slf4j
public class RouteRequestServiceImpl implements RouteRequestService {

    @Value("${yim.route.online.url}")
    private String onlineUserUrl;

    @Value("${yim.route.private.url}")
    private String privateUrl;

    @Autowired
    private OkHttpClient client;

    @Override
    public List<OnlineUsersVO.DataBodyBean> onlineUsers() throws Exception {

        JSONObject jsonObject = new JSONObject();
        RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, jsonObject.toString());

        Request request = new Request.Builder()
                .url(onlineUserUrl)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }


        ResponseBody body = response.body();
        OnlineUsersVO onlineUsersResVO;
        try {
            String json = body.string();
            onlineUsersResVO = JSON.parseObject(json, OnlineUsersVO.class);

        } finally {
            body.close();
        }

        return onlineUsersResVO.getDataBody();
    }

    @Override
    public void sendPrivateMsg(PrivateVO privateVO) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", privateVO.getUserId());
        jsonObject.put("receivedUserId", privateVO.getReceivedUserId());
        jsonObject.put("content", privateVO.getContent());

        RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, jsonObject.toString());

        Request request = new Request.Builder()
                .url(privateUrl)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
    }
}
