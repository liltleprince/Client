package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoosePlaceController implements Initializable {
    @FXML
    public Button Add;
    @FXML
    public Button Cancel;
    @FXML
    private ListView<String> listView;
    Data data = Data.getData();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.getItems().addAll(data.locationString);
    }

    public void onKeyPressedAdd(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) onMouseClickAdd();
    }

    public void onMouseClickAdd() {
        if(listView.getSelectionModel().getSelectedItem() != null) data.setLocationName(listView.getSelectionModel().getSelectedItem());
        Stage stage = (Stage) this.Cancel.getScene().getWindow();
        stage.close();
    }

    public void onMouseExitedAdd() {
        Add.setEffect(null);
    }

    public void onMouseMovedAdd() {
        Add.setEffect(new DropShadow());
    }

    public void onMousePressedCancel(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) onMouseClickedCancel();
    }

    public void onMouseClickedCancel() {
        Stage stage = (Stage) this.Cancel.getScene().getWindow();
        stage.close();
    }

    public void onMouseExitedCancel() {
        Cancel.setEffect(null);
    }

    public void onMouseMovedCancel() {
        Cancel.setEffect(new DropShadow());
    }
}