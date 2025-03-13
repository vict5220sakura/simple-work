package com.vict.ws;

import com.alibaba.fastjson.JSONObject;
import com.vict.bean.ws.dto.MessageDTO;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@ServerEndpoint("/ws/buser/{keyToken}")
public class BuserWebSocketServer {

    /** 线程安全 Map*/
    public static ConcurrentHashMap<String, Session> keyToken_session = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, List<String>> buserId_keyTokenList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> keyToken_buserId = new ConcurrentHashMap<>();

    //==========【websocket接受、推送消息等方法 —— 具体服务节点推送ws消息】=====================================================
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "keyToken") String keyToken) {
        try {
            String[] keyTokenArr = keyToken.split("_");
            String Key = keyTokenArr[0];
            String token = keyTokenArr[1];
            Long buserId = UserContextUtil.findBuserIdByToken(token);

            if(buserId == null){
                session.close();
                throw new RuntimeException("未登录");
            }

            keyToken_session.put(keyToken, session);
            buserId_keyTokenList.putIfAbsent(buserId, new CopyOnWriteArrayList<String>());
            buserId_keyTokenList.get(buserId).add(keyToken);
            keyToken_buserId.put(keyToken, buserId);

            log.info("【系统 WebSocket】有新的连接, keyToken:{}", keyToken);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @OnClose
    public void onClose(@PathParam("keyToken") String keyToken) {
        try {
            String[] keyTokenArr = keyToken.split("_");
            String Key = keyTokenArr[0];
            String token = keyTokenArr[1];

            Long buserId = keyToken_buserId.get(keyToken);

            keyToken_session.remove(keyToken);
            buserId_keyTokenList.putIfAbsent(buserId, new CopyOnWriteArrayList<String>());
            buserId_keyTokenList.get(buserId).remove(keyToken);
            keyToken_buserId.remove(keyToken);

        } catch (Exception e) {
            log.error("", e);
        }
    }

    /** ws推送消息 */
    public static void pushMessage(Long buserId, MessageDTO.MessageType type) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(type);

        pushMessageDTO(buserId, messageDTO);
    }

    public static void pushMessageDTO(Long buserId, MessageDTO messageDTO) {
        buserId_keyTokenList.putIfAbsent(buserId, new CopyOnWriteArrayList<String>());
        List<String> keyTokenList = buserId_keyTokenList.get(buserId);
        for(String keyToken : keyTokenList){
            Session session = keyToken_session.get(keyToken);
            if(session == null || session.isOpen() == false){
                return;
            }
            synchronized (session){
                // messageDTO.setId(IdUtils.snowflakeId());
                String messageVOStr = JSONObject.toJSONString(messageDTO);
                // log.info("【系统 WebSocket】推送单人消息:" + messageVOStr);
                try {
                    session.getBasicRemote().sendText(messageVOStr);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    /** ws接受客户端消息 */
    @OnMessage
    public void onMessage(String message, @PathParam(value = "keyToken") String keyToken) {
        // log.info("【系统 WebSocket】收到客户端消息:" + message);

        String[] keyTokenArr = keyToken.split("_");
        String Key = keyTokenArr[0];
        String token = keyTokenArr[1];

        MessageDTO messageAO = JSONObject.parseObject(message, MessageDTO.class);

        // ping pong 响应
        if(messageAO.getType() == MessageDTO.MessageType.ping){

            Long buserId = keyToken_buserId.get(keyToken);

            MessageDTO messageVO = new MessageDTO();
            messageVO.setType(MessageDTO.MessageType.pong);
            messageVO.setData(messageAO.getData());
            pushMessageDTO(buserId, messageVO);
        }
    }

    /** 配置错误信息处理 */
    @OnError
    public void onError(Session session, Throwable t) {
        log.warn("【系统 WebSocket】消息出现错误");
        log.error(t.getMessage(), t);
    }
}
