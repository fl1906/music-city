package top.flya.system.config;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author litong
 * @date 2019/11/6 16:01
 */
@Component
public class ClientCache {

    /**
     * 本地缓存
     */
    public static Map<String, HashMap<UUID, SocketIOClient>> concurrentHashMap = new ConcurrentHashMap<>();


    public Map<String, HashMap<UUID, SocketIOClient>> getConcurrentHashMap()
    {
        return concurrentHashMap;
    }


    /**
     * 存入本地缓存
     *
     * @param userId         用户ID
     * @param sessionId      页面sessionID
     * @param socketIOClient 页面对应的通道连接信息
     */
    public void saveClient(String userId, UUID sessionId, SocketIOClient socketIOClient) {
        HashMap<UUID, SocketIOClient> sessionIdClientCache = concurrentHashMap.get(userId);
        if (sessionIdClientCache == null) {
            sessionIdClientCache = new HashMap<>();
        }
        sessionIdClientCache.put(sessionId, socketIOClient);
        concurrentHashMap.put(userId, sessionIdClientCache);
    }

    /**
     * 根据用户ID获取所有通道信息
     *
     * @param userId
     * @return
     */
    public HashMap<UUID, SocketIOClient> getUserClient(String userId) {
        return concurrentHashMap.get(userId);
    }

    /**
     * 根据用户ID及页面sessionID删除页面链接信息
     *
     * @param userId
     * @param sessionId
     */
    public void deleteSessionClient(String userId, UUID sessionId) {
        concurrentHashMap.remove(userId);
//        concurrentHashMap.get(userId).remove(sessionId);
    }
}
