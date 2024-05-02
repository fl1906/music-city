package top.flya.system.domain.bo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: 风离
 * @Date: 2022/06/04/2:59
 * @Description:
 */
@Data
public class Resource {
    private String original_type;
    private String algorithm;
    private String ciphertext;
    private String associated_data;
    private String nonce;

}

