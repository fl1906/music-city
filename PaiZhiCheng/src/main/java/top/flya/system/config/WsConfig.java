package top.flya.system.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * <p>
 * WebSocket配置类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-18 16:41
 */

@Data
@Component
public class WsConfig {
    /**
     * 端口号
     */
    @Value("${wx.server.port}")
    private Integer port ;

    /**
     * host
     */
    @Value("${wx.server.host}")
    private String host ;
}
