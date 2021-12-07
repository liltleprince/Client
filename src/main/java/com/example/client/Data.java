package com.example.client;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.*;

import java.net.Socket;

public class Data {
    private static final Data data = new Data();
    public boolean Connected = false, DisConnect = true, Succeed = false;
    public static Client client = new Client();
    public Scene Login;
    public Stage stage;
    public Socket socket;


    private Data(){}

    public static Data getData(){
        return data;
    }

    public void setSucceed(boolean succeed){
        this.Succeed = succeed;
    }

    public void setConnected(){
        this.Connected = true;
        this.DisConnect = false;
    }

    public void setDisConnect(){
        this.DisConnect = true;
        this.Connected = false;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void Connect(){
        client.Connect();
    }

    public void setLogin(Scene scene){
        this.Login = scene;
    }

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    public void Login(){
        client.Login();
    }

    public void CreateAccount(){
        client.CreateAccount();
    }

    public void getLocation(){
        client.getLocation();
    }

    public void getSensor(){
        client.getSensor();
    }

    public void Logout(){
        client.Logout();
    }

    public void quit(){
        client.quit();
    }
}