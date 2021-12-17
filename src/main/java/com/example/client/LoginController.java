package com.example.client;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
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
        LoginError.setText("");
    }

    public void onLoginButtonClick() {
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
        Stage stage = (Stage) Login.getScene().getWindow();
        Scene scene = Login.getScene();
        scene.setRoot(data.fxmlLoaderWeather.getRoot());
        //stage.setResizable(false);
        stage.setTitle("Thời Tiết");
        stage.setScene(scene);
        stage.show();
        WeatherController weatherController = data.fxmlLoaderWeather.getController();
        weatherController.start();
        clear();
        data.runGetData = true;
    }

    public void clear(){
        LoginError.setText("");
        username.setText("");
        pw.setText("");
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
        data.scene.setRoot(data.fxmlLoaderCreateAccount.getRoot());
        data.stage.setTitle("Đăng ký");
        clear();
    }

    public void onUsernameKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) onLoginButtonClick();
        if (keyEvent.getCode() == TAB) onPwMouseClicked();
        if (username.getText().length() != 0) onUsernameMouseClicked();
    }

    public void onPwKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) onLoginButtonClick();
        if (pw.getText().length() != 0) onPwMouseClicked();
    }

    public void onLoginKeyPressed(KeyEvent keyEvent) {
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