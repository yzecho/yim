package io.yzecho.yimserver.server;

import com.alibaba.fastjson.JSONObject;
import io.yzecho.yimcommon.constant.BasicConstant;
import io.yzecho.yimserver.config.InitConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author yzecho
 * @desc
 * @date 25/11/2019 20:19
 */
@Component
public class ClientProcessor {
    private OkHttpClient okHttpClient;
    private InitConfiguration configuration;

    public ClientProcessor(OkHttpClient okHttpClient, InitConfiguration configuration) {
        this.okHttpClient = okHttpClient;
        this.configuration = configuration;
    }

    /**
     * 客户端下线
     *
     * @param userId
     * @throws IOException
     */
    public void down(Integer userId) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        RequestBody requestBody = RequestBody.create(BasicConstant.MEDIA_TYPE, jsonObject.toString());
        Request request = new Request.Builder()
                .url(configuration.getClearRouteUrl())
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
    }
}
