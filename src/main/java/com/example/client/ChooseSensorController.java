package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChooseSensorController implements Initializable {
    @FXML
    public Button Save;
    @FXML
    public Button Cancel;
    @FXML
    private ListView<String> listView;
    ArrayList<String> listSensor = new ArrayList<String>();
    Data data = Data.getData();

    public void onMouseClickedAdd() {
        if(listView.getSelectionModel().getSelectedItem() != null){
            data.addSensor(data.VI_EN(listView.getSelectionModel().getSelectedItem()));
            data.changeLocation = true;
        }

        Stage stage = (Stage) this.Cancel.getScene().getWindow();
        stage.close();
    }

    public void onMouseClickedCancel() {
        Stage stage = (Stage) Cancel.getScene().getWindow();
        stage.close();
    }

    public void onMouseEnteredAdd() {
        Save.setEffect(new DropShadow());
    }

    public void oMouseEnteredCancel() {
        Cancel.setEffect(new DropShadow());
    }

    public void onMouseExitedAdd() {
        Save.setEffect(null);
    }

    public void onMouseExitedCancel() {
        Cancel.setEffect(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i=0; i<data.sensor.length(); i++){
            boolean check = true;
            for (int j=0; j<data.sensorRegister.length(); j++)
                if (data.sensor.getJSONObject(i).get("TypeID") == data.sensorRegister.getJSONObject(j).get("TypeID")) check = false;
            if (check) listSensor.add(data.VI_EN(data.sensor.getJSONObject(i).get("TypeName").toString()));
        }
        listView.getItems().addAll(listSensor);
    }
}