package io.yzecho.yimserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzecho
 * @desc
 * @date 12/12/2019 12:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomProtocol {
    private static final long serialVersionUID = 4671171056588401542L;
    private Integer id;
    private String content;
}
