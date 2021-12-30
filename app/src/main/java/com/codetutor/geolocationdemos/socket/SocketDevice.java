package com.codetutor.geolocationdemos.socket;

import java.io.IOException;
import java.net.Socket;

public class SocketDevice{

    private static final SocketDevice INSTANCE = new SocketDevice();

    private Socket socket;
    String serverIpAddress = "192.168.121.131";
    int serverPortNumber = 59090;
    //Private constructor prevents instantiating and subclassing
    private SocketDevice()  {
        // instanciates the socket ...
        try {
            socket = new Socket(serverIpAddress,serverPortNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Static 'instance' method
    public static SocketDevice getInstance( ) {
        return INSTANCE;
    }


    public   Socket getSocket(){
        return socket;
    }

    public void close(){
        // ...
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ...
    }

    // ...
}