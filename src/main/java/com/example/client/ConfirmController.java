package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

public class ConfirmController {

    @FXML
    public Button Cancel;
    @FXML
    public Button Ok;

    public void onMouseClickedCancel() {
        Stage stage = (Stage) Cancel.getScene().getWindow();
        stage.close();
    }

    public void onMouseClickedOk(){
        Stage stage = (Stage) Ok.getScene().getWindow();
        stage.close();
        Data data = Data.getData();
        data.Logout();
        data.stage.setTitle("Đăng nhập");
        data.scene.setRoot(data.fxmlLoaderLogin.getRoot());
        data.runGetData = false;
        data.locationName = "NULL";
    }

    public void onMouseExitedCancel() {
        Cancel.setEffect(null);
    }

    public void onMouseExitedOk() {
        Ok.setEffect(null);
    }

    public void onMouseMovedCancel() {
        Cancel.setEffect(new DropShadow());
    }

    public void onMouseMovedOk() {
        Ok.setEffect(new DropShadow());
    }
}
