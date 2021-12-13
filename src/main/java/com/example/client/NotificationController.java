package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class NotificationController {
    @FXML
    public Button Ok;

    public void onMouseClickedOk(MouseEvent mouseEvent) {
        Stage stage = (Stage) this.Ok.getScene().getWindow();
        stage.close();
        Data data = Data.getData();
        Stage stage1 = data.stage;
        stage1.setTitle("Đăng nhập");
        stage1.setScene(data.Login);
    }

    public void onMouseExitedOk(MouseEvent mouseEvent) {
        Ok.setEffect(null);

    }

    public void onMouseMovedOk(MouseEvent mouseEvent) {
        Ok.setEffect(new DropShadow());
    }
}