package com.vict.controller.test;


import com.vict.bean.ws.dto.MessageDTO;
import com.vict.framework.bean.R;
import com.vict.framework.web.ApiPrePath;
import com.vict.ws.BuserWebSocketServer;
import com.vict.ws.CustomerWebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@ApiPrePath
@Api(value="测试接口", tags={"测试接口"}, hidden = true)
@Slf4j
@RestController
@RequestMapping("/test")
public class WsTestController {
    private static final String SEAGGER_TAGS = "测试接口-ws";

    public static List<Integer> list = new CopyOnWriteArrayList<>();
    public static List<Integer> list2 = new CopyOnWriteArrayList<>();
    public static List<Integer> appList = new CopyOnWriteArrayList<>();

    static int count = 0;
    static int count2 = 0;
    static int appCount = 0;

    @ApiOperation(tags = SEAGGER_TAGS, value = "test", httpMethod = "POST")
    @PostMapping("/testws")
    public R testws(){

        new Thread(() -> {
            for(int i = 0 ; i < 10 ; i ++){
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                list.add(count);

                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setType(MessageDTO.MessageType.testMessage);
                BuserWebSocketServer.pushMessageDTO(1L, messageDTO);
                count++;
            }
        }).start();

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "test", httpMethod = "POST")
    @PostMapping("/testws2")
    public R testws2(){

        new Thread(() -> {
            for(int i = 0 ; i < 15 ; i ++){
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                list2.add(count2);

                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setType(MessageDTO.MessageType.testMessage2);
                BuserWebSocketServer.pushMessageDTO(1L, messageDTO);
                count2++;
            }
        }).start();

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "test", httpMethod = "POST")
    @PostMapping("/appTestws")
    public R appTestws(){

        new Thread(() -> {
            for(int i = 0 ; i < 15 ; i ++){
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                appList.add(appCount);

                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setType(MessageDTO.MessageType.appTestMessage);
                CustomerWebSocketServer.pushMessage(1879739185864962048L, MessageDTO.MessageType.appTestMessage);
                appCount++;
            }
        }).start();

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "test", httpMethod = "POST")
    @PostMapping("/unread")
    public R<List<Integer>> unread(){
        List<Integer> listVO = list.stream().collect(Collectors.toList());
        list.removeAll(listVO);
        return R.ok(listVO);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "test", httpMethod = "POST")
    @PostMapping("/unread2")
    public R<List<Integer>> unread2(){
        List<Integer> listVO = list2.stream().collect(Collectors.toList());
        list2.removeAll(listVO);
        return R.ok(listVO);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "appUnread", httpMethod = "POST")
    @PostMapping("/appUnread")
    public R<List<Integer>> appUnread(){
        List<Integer> listVO = appList.stream().collect(Collectors.toList());
        appList.removeAll(listVO);
        return R.ok(listVO);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "测试主动断开链接", httpMethod = "POST")
    @PostMapping("/testwsClose")
    public R testwsClose(){
        BuserWebSocketServer.keyToken_session.forEach((keyToken, session) -> {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "测试主动断开链接", httpMethod = "POST")
    @PostMapping("/apptestwsClose")
    public R apptestwsClose(){
        CustomerWebSocketServer.keyAToken_session.forEach((keyToken, session) -> {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return R.ok();
    }
}
