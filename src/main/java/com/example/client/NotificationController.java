package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

public class NotificationController {
    @FXML
    public Button Ok;

    public void onMouseClickedOk() {
        Stage stage = (Stage) this.Ok.getScene().getWindow();
        stage.close();
        Data data = Data.getData();
        data.stage.setTitle("Đăng nhập");
        data.scene.setRoot(data.fxmlLoaderLogin.getRoot());
    }

    public void onMouseExitedOk() {
        Ok.setEffect(null);

    }

    public void onMouseMovedOk() {
        Ok.setEffect(new DropShadow());
    }
}
