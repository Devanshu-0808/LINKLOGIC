package com.example.linklocal.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.example.linklocal.Pojo.GrpId;
import com.example.linklocal.Repository.ConnectivityDb;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class WebSocket_Controller {

@Autowired
ConnectivityDb connectionDb;

@Autowired
private SimpMessagingTemplate messagingTemplate;
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        GrpId grpId = new GrpId();
        String sessionId = headerAccessor.getSessionId();
        String username = headerAccessor.getSessionAttributes().get("username").toString();
        String groupId = headerAccessor.getSessionAttributes().get("groupId1").toString();
        grpId.setUsername(username);
        grpId.setGroupId(groupId);
        grpId.setSocketId(sessionId);
        connectionDb.save(grpId);
        System.out.println("Session ID " + sessionId + " connected.");
    }

    @EventListener
    @Transactional
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        connectionDb.deleteBySocketId(sessionId);
        System.out.println("Session ID " + sessionId + " disconnected.");
}
}