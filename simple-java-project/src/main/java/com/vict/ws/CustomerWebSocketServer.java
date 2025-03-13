package com.vict.ws;

import com.alibaba.fastjson.JSONObject;
import com.vict.bean.ws.dto.MessageDTO;
import com.vict.framework.utils.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@ServerEndpoint("/ws/customer/{keyAToken}")
public class CustomerWebSocketServer {

    /** 线程安全 Map*/
    public static ConcurrentHashMap<String, Session> keyAToken_session = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, List<String>> customerId_keyATokenList = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> keyAToken_customerId = new ConcurrentHashMap<>();

    //==========【websocket接受、推送消息等方法 —— 具体服务节点推送ws消息】=====================================================
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "keyAToken") String keyAToken) {
        try {
            String[] keyATokenArr = keyAToken.split("_");
            String Key = keyATokenArr[0];
            String aToken = keyATokenArr[1];
            Long customerId = UserContextUtil.findAppCustomerIdByAToken(aToken);

            if(customerId == null){
                session.close();
                throw new RuntimeException("未登录");
            }

            keyAToken_session.put(keyAToken, session);
            customerId_keyATokenList.putIfAbsent(customerId, new CopyOnWriteArrayList<String>());
            customerId_keyATokenList.get(customerId).add(keyAToken);
            keyAToken_customerId.put(keyAToken, customerId);

            log.info("【APP系统 WebSocket】有新的连接, keyAToken:{}", keyAToken);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @OnClose
    public void onClose(@PathParam("keyAToken") String keyAToken) {
        try {
            String[] keyATokenArr = keyAToken.split("_");
            String Key = keyATokenArr[0];
            String aToken = keyATokenArr[1];

            Long customerId = keyAToken_customerId.get(keyAToken);

            keyAToken_session.remove(keyAToken);
            customerId_keyATokenList.putIfAbsent(customerId, new CopyOnWriteArrayList<String>());
            customerId_keyATokenList.get(customerId).remove(keyAToken);
            keyAToken_customerId.remove(keyAToken);

            log.info("【APP系统 WebSocket】关闭连接, keyAToken:{}", keyAToken);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /** ws推送消息 */
    public static void pushMessage(Long customerId, MessageDTO.MessageType type) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(type);

        pushMessageDTO(customerId, messageDTO);
    }

    public static void pushMessageDTOByKeyAToken(String keyAToken, MessageDTO messageDTO) {
        Session session = keyAToken_session.get(keyAToken);
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

    public static void pushMessageDTO(Long customerId, MessageDTO messageDTO) {
        customerId_keyATokenList.putIfAbsent(customerId, new CopyOnWriteArrayList<String>());
        List<String> keyATokenList = customerId_keyATokenList.get(customerId);
        for(String keyAToken : keyATokenList){
            Session session = keyAToken_session.get(keyAToken);
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
    public void onMessage(String message, @PathParam(value = "keyAToken") String keyAToken) {
        // log.info("【系统 WebSocket】收到客户端消息:" + message);

        String[] keyATokenArr = keyAToken.split("_");
        String Key = keyATokenArr[0];
        String aToken = keyATokenArr[1];

        MessageDTO messageAO = JSONObject.parseObject(message, MessageDTO.class);

        // ping pong 响应
        if(messageAO.getType() == MessageDTO.MessageType.ping){

            MessageDTO messageVO = new MessageDTO();
            messageVO.setType(MessageDTO.MessageType.pong);
            messageVO.setData(messageAO.getData());
            pushMessageDTOByKeyAToken(keyAToken, messageVO);
        }
    }

    /** 配置错误信息处理 */
    @OnError
    public void onError(Session session, Throwable t) {
        log.warn("【APP系统 WebSocket】消息出现错误");
        log.error(t.getMessage(), t);
    }
}
