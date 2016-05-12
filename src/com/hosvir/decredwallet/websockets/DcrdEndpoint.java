package com.hosvir.decredwallet.websockets;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * 
 * @author Troy
 * 
 * Currently testing web sockets. Ignore this...
 * 
 * NOTE: Certificates have to be manually added to the KeyStore.
 * 		 Check if there are anyways to manually include the certificate.
 *
 */

@ClientEndpoint
public class DcrdEndpoint {
	
	public static void main(String[] args){
		new DcrdEndpoint("wss://127.0.0.1:19109/ws");
	}
	
	public DcrdEndpoint(String uri){
		try{			
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, URI.create(uri));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        //this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        //this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
       // if (this.messageHandler != null) {
        //    this.messageHandler.handleMessage(message);
        //}
    	
    	System.out.println("Message: " + message);
    }

}
