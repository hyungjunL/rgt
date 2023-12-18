package com.rgt.websocket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Service
@ServerEndpoint(value = "/fortem")
@Slf4j
public class WebSocketServer {
    
    private static Set<Session> CLIENTS = Collections.synchronizedSet(new HashSet<>());
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.toString());

        if (CLIENTS.contains(session)) {
        	log.info(" ######## [ This session is already connected ] ########");
        } else {
            CLIENTS.add(session);
            log.info(" ######## [ This session is new session ] ########");
        }
    }

    @OnClose
    public void onClose(Session session) throws Exception {
        CLIENTS.remove(session);
        log.info(" ######## [ SESSION CLOSED ] ########");
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        if("Y".equals(message)) {
        	BufferedReader reader = null;
        	
        	String os = System.getProperty("os.name").toLowerCase();
        	if(!os.contains("win")) {
        		//os가 window가 아닐때
        		reader = new BufferedReader(
        			new FileReader("/your/test/file/path/sample.txt")
		        );
        	}else {
        		//os가 window일 때
        		reader = new BufferedReader(
    		        new FileReader("C:\\Users\\sample.txt") //회전 하는 장면
            	);
        	}
        	
	        String str = "";
	        while ((str = reader.readLine()) != null) {
	            String a= str.substring(24);
	            log.info("######## SEND MESSAGE : {}", a);
	            
	            for (Session client : CLIENTS) {
		            client.getBasicRemote().sendText(str.substring(24));
	            }
	            Thread.sleep(150);
	        }
	 
	        reader.close();
	    	
        }else {
        	for (Session client : CLIENTS) {
        		client.getBasicRemote().sendText("{\"error\":{\"code\":400,\"message\":\"\"}}");
        	}
        }
    }
}