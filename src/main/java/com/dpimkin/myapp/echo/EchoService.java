package com.dpimkin.myapp.echo;

import com.dpimkin.myapp.web.tier.geo.GeopointDTO;
import com.dpimkin.myapp.web.tier.geo.NotifyServer;
import com.google.gson.Gson;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Observes;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

@Startup
@Singleton
@EchoNotification
public class EchoService {

    @Resource
    ManagedExecutorService mes;

    protected String marshal( GeopointDTO dto )  {
        Gson gson = new Gson();
        return gson.toJson( dto );
    }

    @EchoNotification
    public void onEvent( @Observes GeopointDTO dto ) {
        String json = marshal( dto );
        mes.execute( new EchoBroadcast( NotifyServer.getSessions(), json ));
    }

    public class EchoBroadcast implements Runnable {

        final List<Session> sessions;
        final String json;

        public EchoBroadcast( List<Session> sessions, String json ) {
            this.sessions = sessions;
            this.json = json;
        }

        @Override
        public void run() {
            for ( Session session : sessions ) {
                try {
                    session.getBasicRemote().sendText( json );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }






}
