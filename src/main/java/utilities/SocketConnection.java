/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Gijs
 */
public class SocketConnection {
    
    private WebSocketClient client;
    
    public String SendRequest(int bsn) throws InterruptedException, URISyntaxException, JSONException {
        
        final String ret = "";
        
        //145.93.105.25
        client = new WebSocketClient(new URI("ws://145.93.104.233:8080/RekeningAdministratieOverheid/RekeningAdministratieSocket"), new Draft_17()) {        
            
            @Override
            public void onMessage(String message) {
                System.out.println("ontvangen : " + message);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("opened connection");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("closed connection");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }

        };
        
        client.connect();
        JSONObject msg = new JSONObject();
        msg.put("bsn", bsn);
        client.send(msg.toString());
        
        return ret;
        
    }
}
