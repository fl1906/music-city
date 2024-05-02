package top.flya.system.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: 风离
 * @Date: 2022/06/04/14:06
 * @Description:
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Signature;
import java.util.Base64;

@Slf4j
@Component
public class CreateSign {


    /**
     * 作用：使用字段appId、timeStamp、nonceStr、package计算得出的签名值
     * 场景：根据微信统一下单接口返回的 prepay_id 生成调启支付所需的签名值
     * @param appId
     * @param timestamp
     * @param nonceStr
     * @param pack package
     * @return
     * @throws Exception
     */
    public String getSign(String appId, long timestamp, String nonceStr, String pack) throws Exception{
        String message = buildMessage(appId, timestamp, nonceStr, pack);
        log.info("message: \n"+message);
        log.info("======end======");
        String paySign= sign(message.getBytes("utf-8"));
        return paySign;
    }

    private String buildMessage(String appId, long timestamp, String nonceStr, String pack) {
        return   appId + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + pack + "\n";
    }
    private String sign(byte[] message) throws Exception{
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(MyPrivateKey.getPrivateKey("apiclient_key.pem"));
        sign.update(message);

        return Base64.getEncoder().encodeToString(sign.sign());
    }


}
