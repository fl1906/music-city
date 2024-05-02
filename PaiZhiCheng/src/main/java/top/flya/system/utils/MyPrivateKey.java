package top.flya.system.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: 风离
 * @Date: 2022/06/04/14:57
 * @Description:
 */

import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.PrivateKey;

public class MyPrivateKey {
    /**
     * 获取私钥。
     *  这是个静态方法，可以直接用类名调用
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     *
     * 完全不需要修改，注意此方法也是去掉了头部和尾部，注意文件路径名
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {

       ClassPathResource resource = new ClassPathResource(filename);

        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                resource.getInputStream());

        return merchantPrivateKey;
    }
}


