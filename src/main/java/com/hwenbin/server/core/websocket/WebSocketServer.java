package com.hwenbin.server.core.websocket;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author hwb
 * @date 2022/04/11 21:22
 */
@Component
@ServerEndpoint("/websocket/{accountId}")
@Slf4j
public class WebSocketServer {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收 accountId
     */
    private Long accountId;

    /**
     * 用来存放 WebSocketServer 对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 用来存放每个客户端对应的 Session 对象
     */
    private static ConcurrentMap<Long, Session> sessionPool = new ConcurrentHashMap<>();

    /**
     * 存储消息，TODO ： 改为redis（或其他）存储
     */
    private static ConcurrentMap<Long, List<String>> messageMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "accountId") Long accountId) {
        this.session = session;
        this.accountId = accountId;
        webSocketSet.add(this);
        sessionPool.put(accountId, session);
        log.info(accountId + " 已连接 \n 【websocket消息】有新的连接，当前在线人数为：" + webSocketSet.size());
        List<String> messageList = messageMap.get(accountId);
        if (CollUtil.isNotEmpty(messageList)) {
            log.info("【websocket消息】发送存储消息 员工：" + accountId);
            messageList.forEach(message -> sendMessageTo(accountId, message));
            messageList.clear();
        }
    }

    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【websocket消息】 " + this.accountId + " 连接断开，当前在线人数为:" + webSocketSet.size());
    }

    /**
     * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端 " + this.accountId + " 消息:" + message);
    }

    /**
     * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("【websocket消息】-------- onError: 当前在线人数 " + webSocketSet.size()
                + "，连接发生错误 " + this.accountId + "-"+ error.getMessage() + " --------");
        // error.printStackTrace();
    }

    /**
     * 此为广播消息
     */
    public void sendMessageAll(String message) {
        for (WebSocketServer webSocket : webSocketSet) {
            log.info("【websocket消息】广播消息:" + message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 此为单点消息 TODO : 更改消息存储方式
     */
    public void sendMessageTo(Long accountId, String message) {
        log.info("【websocket消息】单点消息 to " + accountId + " :" + message);
        Session session = sessionPool.get(accountId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.info("【websocket消息】员工 " + accountId + " 不在线，存储消息");
            List<String> messageList = messageMap.getOrDefault(accountId, new ArrayList<>());
            messageList.add(message);
            messageMap.put(accountId, messageList);
        }
    }

}
