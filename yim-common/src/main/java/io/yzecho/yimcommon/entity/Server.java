package io.yzecho.yimcommon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzecho
 * @desc 服务端基本信息
 * @date 25/11/2019 11:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server implements Serializable {

    private String ip;
    private Integer nettyPort;
    private Integer httpPort;

}
