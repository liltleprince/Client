package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {
    @FXML
    public TextField UserName;
    @FXML
    public Label Notification;
    @FXML
    public PasswordField Password;
    @FXML
    public PasswordField RePassword;
    @FXML
    public Button Register;
    @FXML
    public Button Cancel;
    public Data data = Data.getData();

    public void clear(){
        UserName.setText("");
        Password.setText("");
        RePassword.setText("");
        Notification.setText("");
    }

    public void UsernameClicked() {
        UserName.setStyle("-fx-border-color : none");
        //Notification.setText("");
    }

    public void PasswordClicked() {
        Password.setStyle("-fx-border-color : none");
        //Notification.setText("");
    }

    public void RePasswordClicked() {
        RePassword.setStyle("-fx-border-color : none");
        //Notification.setText("");
    }

    public void UsernameKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.TAB) PasswordClicked();
        else UsernameClicked();
    }

    public void PasswordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.TAB) RePasswordClicked();
        else PasswordClicked();
    }

    public void CancelClicked() {
        data.scene.setRoot(data.fxmlLoaderLogin.getRoot());
        data.stage.setTitle("Đăng nhập");
        clear();
    }

    public void CancelKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.TAB) UsernameClicked();
        if (keyEvent.getCode() == KeyCode.ENTER) CancelClicked();
    }

    public void RegisterClicked(){
        //Username, password, rePassword rỗng
        if ((UserName.getText().length() == 0)||(Password.getText().length() == 0)||(RePassword.getText().length() == 0)) {
            if (UserName.getText().length() == 0) {
                UserName.setStyle("-fx-border-color : red ");
                new animatefx.animation.Shake(UserName).play();
            }
            if (Password.getText().length() == 0) {
                Password.setStyle("-fx-border-color : red ");
                new animatefx.animation.Shake(Password).play();
            }
            if (RePassword.getText().length() == 0) {
                RePassword.setStyle("-fx-border-color : red ");
                new animatefx.animation.Shake(RePassword).play();
            }
            return;
        }

        //username chứa khoảng trắng
        if (UserName.getText().indexOf(' ') != -1){
            Notification.setText("Tài khoản không thể chứa dấu cách");
            Notification.setTextFill(Color.RED);
            return;
        }

        //độ dài password < 5
        if (Password.getText().length() < 5){
            Notification.setText("Mậu khẩu tối thiểu là 5 ký tự");
            Notification.setTextFill(Color.RED);
            return;
        }

        //rePassword sai
        if (!Password.getText().equals(RePassword.getText())){
            Notification.setText("Nhập lại mật khẩu sai");
            Notification.setTextFill(Color.RED);
            return;
        }

        //không thể kết nối tới Broker
        if (data.DisConnect){
            Notification.setText("Không thể kết nối tới Broker");
            Notification.setTextFill(Color.RED);
            return;
        }

        //Gửi yêu cầu tạo tài khoản đến Broker
        data.SetNewAccount(UserName.getText(), Password.getText());
        Notification.setText("Đang xác minh...");
        Notification.setTextFill(Color.GREEN);
        data.CreateAccount();

        //không thể kết nối tới Broker
        if (data.DisConnect){
            Notification.setText("Không thể kết nối tới Broker");
            Notification.setTextFill(Color.RED);
            return;
        }

        //Account sai
        if (!data.Succeed){
            Notification.setText("Tài khoản đã tồn tại");
            Notification.setTextFill(Color.RED);
            return;
        }

        //Đăng ký thành công
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Notification.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Thông báo");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        clear();
        //Notification.setText("Create Account Successfully!");
    }

    public void PaneKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) RegisterClicked();
    }

    public void RePasswordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.TAB) RePasswordClicked();
    }

    public void RegisterKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) RegisterClicked();
    }

    public void onMouseExitedRegister() {
        Register.setEffect(null);
    }

    public void onMouseMovedRegister() {
        Register.setEffect(new DropShadow());
    }

    public void onMouseExitedCancel() {
        Cancel.setEffect(null);
    }

    public void onMouseMovedCancel() {
        Cancel.setEffect(new DropShadow());
    }
}
