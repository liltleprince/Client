package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setResizable(false);
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        LoginController loginController = fxmlLoader.getController();
        loginController.start();
        stage.show();
        Data data = Data.getData();
        stage.setOnCloseRequest( event -> {data.quit();} );
    }

    public static void main(String[] args) {
        launch();
    }
}