package com.example.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Data data = Data.getData();
        data.start(stage);
        Scene scene = new Scene(data.fxmlLoaderLogin.getRoot(),600,400);
        stage.setResizable(false);
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        data.setScene(scene);
        LoginController loginController = data.fxmlLoaderLogin.getController();
        loginController.start();
        stage.show();
        stage.setOnCloseRequest( event -> Data.getData().quit());
    }

    public static void main(String[] args) {
        launch();
    }
}