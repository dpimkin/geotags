package com.dpimkin.myapp.web.tier.geo;


import com.dpimkin.myapp.web.tier.Endpoint;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(Endpoint.NOTIFY_WS)
public class NotifyServer {

    private static List<Session> allSessions = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen( final Session session ) {
        allSessions.add( session );

    }

    @OnClose
    public void onClose(Session session, CloseReason reason ) {
        allSessions.remove( session );

    }

    @OnMessage
    public String receiveMessage( String message ) { return "ACCEPTED;";}

    public static List<Session> getSessions() {
        return allSessions;
    }





}
