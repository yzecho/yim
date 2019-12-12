package io.yzecho.yimclient.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yzecho
 * @desc
 * @date 12/12/2019 11:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomProtocol implements Serializable {
    private static final long serialVersionUID = 4671171056588401542L;
    private Integer id;
    private String content;
}
