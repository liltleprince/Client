package com.example.client;

import java.io.*;

public class CheckConnection{
    public CheckConnection(){
        Data data = Data.getData();
        try {
            new BufferedReader(new InputStreamReader(data.socket.getInputStream()));
            new BufferedWriter(new OutputStreamWriter(data.socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Broker is Down");
            data.setDisConnect();
        }
    }
}