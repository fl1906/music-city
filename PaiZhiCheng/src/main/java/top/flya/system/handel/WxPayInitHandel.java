package top.flya.system.handel;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.impl.client.CloseableHttpClient;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: 风离
 * @Date: 2022/06/03/23:16
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class WxPayInitHandel {


    private String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
        "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCpUtNYFC/SVijj\n" +
        "mOsW2vVIewgnOfB2YuUW8Q0LUbLtN8jfPJrv2BziCWxVciqNtMKO2ONerRtedzar\n" +
        "D4poTVgDzICefGL8Mxj0PUIGH9dslc9GSsPjc3iynrW0IMFmrx1ItI//7wbWJTCf\n" +
        "eNWkQa9WmywFUcMQy/oVsWITq7RaoCdYLVQ6w7CrusQ1DOOJ1OoCVA8HB+zqJGKW\n" +
        "X6dLTTKF14kYNABPHf7lPd8Zv2VIvxLMxqZuSDKZouQjaciXLI4LGdQje9N9j1JE\n" +
        "cInKIzQnI3AfKTd0TdWR1F+j8QAQCBrHuOLO6WQEQ+N82BZX7KFn+0SVzzH0QjpL\n" +
        "K+vjHSBZAgMBAAECggEBAKA6qZZC3BIdyGm/7k9dehlRm6CLGnrdEM7J4r8gW8JR\n" +
        "NLvTPQbUKljX8/VTqOMZ97Z3lYmlJC4bf9cWSLJ05mIJ5niTWpQvwmB1i4ICJbgy\n" +
        "d8ebvo0BW2kj+OxwxrNl6L9BZrcZOQ3yeXWfQgRCyCqbgmeyPHYroAdhKV9V78CF\n" +
        "HSHwZOMVPUo/y44w6ig5Fw6pKdGXSzZwjGUyZNpaj4IY3LYWWffuigHJduMzBut2\n" +
        "v4JJC2TPC9hB6MiR/sVqpKxtnM466BB8pv7hlZ9TJFzdfD5k1UZuheUgUiunajiC\n" +
        "FeHOEjtSsTWKsAmTg26+Bw5r1apdL3QsLwqH58IVPaUCgYEA1k//XKqWIWG73OKc\n" +
        "mOH5gjzb3Aq4c6yw5TGPhEkPn7D+7auKuwzuoUrzASeopzp0te7Ow299fNToFWIs\n" +
        "wEXi4g0gezO7UwpJ6wWNr0iL/wPgYx3jglrPQ+Pvmfoaj4N0mK/pfmXHF8RdIs/M\n" +
        "p2kn2oQLrJgdeGb4FZM0byX7UcsCgYEAykKJotProJqDogbqsNEIJpBfFlBvxFgb\n" +
        "IPAp33tBqBh9DiKIOOtISTjPZQIAkzkC8SMRK+HZbF1p2/20bjvTOsi8PKxm/sqS\n" +
        "v/oPLDfni8pkmhfNU6rr0iNMagpoT9kks4qk9h2dRij1fKJ6pa4TBahJx5wQ3Yu0\n" +
        "vMEmy8JdwesCgYEAoSGAg7GWMv8Cei6/QosUR4FuZGCDEiWS0p+Sogk0gAJZiWRi\n" +
        "aARvHkH1traUrTbcLTWhq3sVxFdnLzyjHOTukrr/4uGgQ+0GanfAcTuAVnoZqSv9\n" +
        "tDKGhyrHKOPMOH7DmVEZovju2cW/qL7Hxk7fsgF5rYipD6+Lct08nRzXekUCgYEA\n" +
        "s2muWYeOnhox5coo6MujZUHvdwXG/u4AsokXO6xEI24FkEJFf+gFaR5BqiHKjM2n\n" +
        "tGsc0kY27Y83VfOI17etuZlSkKeFfUIIRs70Io88j53q+11dv3gAU5kIMZAl056U\n" +
        "lcbIaaD/X7r5d6NRFCKDsSMEv1HLDBrfKghT967kKB0CgYBnynlp2NRtBgtLNURS\n" +
        "c2xQ7NrlhSiWyLKii/h55PRfxps07w/eqsNALgAqBVDvZt8TcD1IW8/FYv+ZcvMm\n" +
        "GUA4MNQonauQbLDbkwEHc8MWIpPMJnQYf9YAFlf73qknlikepc+s0mkNkYaZYQYZ\n" +
        "Q3uWw4iuk4iyxAzWDWrIRPoiRA==\n" +
        "-----END PRIVATE KEY-----\n";



    @Value("${wx.merchantId}")
    private String merchantId;

    @Value("${wx.merchantSerialNumber}")
    private String merchantSerialNumber;

    @Value("${wx.api3}")
    private String api3;

    public CloseableHttpClient setup() throws IOException {
        log.info("createOrder()方法执行前");
        CloseableHttpClient httpClient=null;

        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil
                .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));

        log.info(""+merchantId+"--"+merchantSerialNumber+"--"+merchantPrivateKey+"---"+api3);

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(merchantId, new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),api3.getBytes("utf-8"));


        //获取微信支付平台证书证书序列号
        BigInteger serialNumber = verifier.getValidCertificate().getSerialNumber();
        //转成16进制
        String serialnumber =  serialNumber.toString(16);
        //获取微信支付平台证书
        X509Certificate validCertificate = verifier.getValidCertificate();
        //获取微信支付平台证书公钥
        PublicKey publicKey = validCertificate.getPublicKey();
        //转成字符串
        String prikeyStr = Base64.encodeBase64String(publicKey.getEncoded());//Base64:package
        System.out.println("prikeyStr:"+prikeyStr);




        // 初始化httpClient
        httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build();

        return httpClient;
    }

    public void after(CloseableHttpClient httpClient) throws IOException {
        log.info("createOrder()方法执行后");
        httpClient.close();
    }

}
