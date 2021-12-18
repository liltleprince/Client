package com.example.client;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static javafx.scene.input.KeyCode.TAB;

public class LoginController {
    private final Data data = Data.getData();
    @FXML
    public TextField username;
    @FXML
    public TextField pw;
    @FXML
    public Button Login;
    @FXML
    public Label LoginError;
    @FXML
    public Label CreateAccount;
    @FXML
    public Label usernameText;
    @FXML
    public Label pwText;
    @FXML
    public Label NoAccountText;

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                        if (data.DisConnect) data.Connect();
                    }));

    public void start() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        Stage stage = (Stage) this.Login.getScene().getWindow();
        data.setStage(stage);
        LoginError.setText("");
        Scene scene = this.Login.getScene();
        data.setLogin(scene);
    }

    public void onLoginButtonClick() throws Exception {
        //username hoặc password rỗng
        if ((username.getText().length() == 0)||(pw.getText().length() == 0)) {
            if (username.getText().length() == 0) {
                username.setStyle("-fx-border-color : red ");
                new animatefx.animation.Shake(username).play();
            }
            if (pw.getText().length() == 0) {
                pw.setStyle("-fx-border-color : red ");
                new animatefx.animation.Shake(pw).play();
            }
            return;
        }

        //username chứa khoảng trắng
        if (username.getText().indexOf(' ') != -1){
            LoginError.setText("Tài khoản không thể chứa dấu cách");
            LoginError.setTextFill(Color.RED);
            return;
        }

        //mất kết nối tới Server
        if (data.DisConnect){
            LoginError.setText("Không thể kết nối tới Broker");
            LoginError.setTextFill(Color.RED);
            return;
        }

        //gửi yêu cầu đăng nhập
        data.SetAccount(username.getText(),pw.getText());
        LoginError.setText("Đang xác thực...");
        LoginError.setTextFill(Color.GREEN);
        data.Login();

        //không thể kết nối tới Broker
        if (data.DisConnect){
            LoginError.setText("Không thể kết nối tới Broker");
            LoginError.setTextFill(Color.RED);
            return;
        }

        //Account sai
        if (!data.Succeed){
            LoginError.setText("Tài khoản không chính xác");
            LoginError.setTextFill(Color.RED);
            return;
        }

        //Đăng nhập thành công
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Weather.fxml"));
        Stage stage = (Stage) Login.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(),600,400);
        //stage.setResizable(false);
        stage.setTitle("Thời Tiết");
        stage.setScene(scene);
        stage.show();
        WeatherController weatherController = fxmlLoader.getController();
        weatherController.start();
        LoginError.setText("");
    }

    public void onUsernameMouseClicked() {
        username.setStyle("-fx-border-color : none");
        //LoginError.setText("");
    }

    public void onPwMouseClicked() {
        pw.setStyle("-fx-border-color : none");
        //LoginError.setText("");
    }

    public void onCreateAccountClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CreateAccount.fxml"));
            Stage stage = (Stage) this.Login.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Đăng ký");
            stage.setResizable(false);
            stage.setScene(scene);
            // Hide this current window (if this is what you want)
            // ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onUsernameKeyPressed(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER) onLoginButtonClick();
        if (keyEvent.getCode() == TAB) onPwMouseClicked();
        if (username.getText().length() != 0) onUsernameMouseClicked();
    }

    public void onPwKeyPressed(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER) onLoginButtonClick();
        if (pw.getText().length() != 0) onPwMouseClicked();
    }

    public void onLoginKeyPressed(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.UP) onPwMouseClicked();
        if (keyEvent.getCode() == KeyCode.UP) onPwMouseClicked();
        if (keyEvent.getCode() == KeyCode.ENTER) onLoginButtonClick();
        if (keyEvent.getCode() == KeyCode.TAB) onMouseExitedLogin();
        if (keyEvent.getCode() == KeyCode.TAB) onUsernameMouseClicked();
        if (keyEvent.getCode() == KeyCode.UP) onMouseExitedLogin();
    }

    public void onMouseMovedLogin() {
        Login.setEffect(new DropShadow());
    }

    public void onMouseExitedLogin() {
        Login.setEffect(null);
    }

    public void onKeyTypeLogin()  {

    }

    public void onMouseExitedCreateAccount() {
        CreateAccount.setUnderline(false);
    }

    public void onMouseMovedCreateAccount() {
        CreateAccount.setUnderline(true);
    }
}