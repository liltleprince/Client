package com.example.client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WeatherController {
    @FXML
    public Text TextLocation;
    @FXML
    public ImageView IconLocation;

    @FXML
    public Pane Pane1;
    @FXML
    public Pane Pane2;
    @FXML
    public Pane Pane3;
    @FXML
    public Pane Pane4;
    @FXML
    public Pane Pane5;
    @FXML
    public Pane Pane6;
    @FXML
    public Pane Pane7;
    @FXML
    public Pane Pane8;

    @FXML
    public ImageView Img1;
    @FXML
    public ImageView Img2;
    @FXML
    public ImageView Img3;
    @FXML
    public ImageView Img4;
    @FXML
    public ImageView Img5;
    @FXML
    public ImageView Img6;
    @FXML
    public ImageView Img7;
    @FXML
    public ImageView Img8;

    @FXML
    public ImageView I1;
    @FXML
    public ImageView I2;
    @FXML
    public ImageView I3;
    @FXML
    public ImageView I4;
    @FXML
    public ImageView I5;
    @FXML
    public ImageView I6;
    @FXML
    public ImageView I7;
    @FXML
    public ImageView I8;

    @FXML
    public ImageView C1;
    @FXML
    public ImageView C2;
    @FXML
    public ImageView C3;
    @FXML
    public ImageView C4;
    @FXML
    public ImageView C5;
    @FXML
    public ImageView C6;
    @FXML
    public ImageView C7;
    @FXML
    public ImageView C8;

    @FXML
    public Label L1;

    @FXML
    public javafx.scene.image.Image Image;
    @FXML
    public Label DayText;
    @FXML
    public Label TimeText;
    @FXML
    public Label LogOut;
    @FXML
    public Label username;

    Pane[] Pane;
    ImageView[] Img, I,C;
    Data data = Data.getData();
    SimpleDateFormat formatDay= new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatTime= new SimpleDateFormat("HH:mm:ss");
    int sensorLength;
    InnerShadow innerShadow = new InnerShadow();
    DropShadow dropShadow = new DropShadow(0,Color.BLACK);

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(100),
                    e -> {
                        Date date = new Date(System.currentTimeMillis());
                        DayText.setText(formatDay.format(date));
                        TimeText.setText(' '+ formatTime.format(date));
                        username.setText(data.getAccount().get("Username").toString());
                        if (data.changeLocation){
                            data.changeLocation = false;
                            TextLocation.setText(data.locationName);
                            RenderSensor();
                        }
                    }));

    public void start() {
        Pane = new Pane[]{Pane1, Pane2, Pane3, Pane4, Pane5, Pane6, Pane7, Pane8};
        C = new ImageView[]{C1,C2,C3,C4,C5,C6,C7,C8};
        I = new ImageView[]{I1,I2,I3,I4,I5,I6,I7,I8};
        Img = new ImageView[]{Img1,Img2,Img3,Img4,Img5,Img6,Img7,Img8};
        for(int i=0; i<8; i++) Pane[i].setVisible(false);
        L1.setText("37\n%");

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        data.getLocation();
        data.getSensor();
        data.locationName = "NULL";
        TextLocation.setText("Chọn địa điểm");
        innerShadow.setColor(Color.color(0.08366899937391281,0.03999999910593033,0.800000011920929));
        dropShadow.setWidth(50);
        dropShadow.setHeight(50);
    }

    public void RenderSensor(){
        sensorLength = data.sensorRegisterString.length;
        for(int i=0; i<sensorLength; i++){
            Pane[i].setVisible(true);
            Img[i].setImage(new Image(Objects.requireNonNull(Main.class.getResource("image/" + data.sensorRegisterString[i] + ".png")).toString()));
            C[i].setVisible(false);
            I[i].setEffect(innerShadow);
            I[i].setVisible(false);
            I[i].setImage(new Image(Objects.requireNonNull(Main.class.getResource("image/chart.png")).toString()));
        }
        if (sensorLength < 8) {
            Pane[sensorLength].setVisible(true);
            Img[sensorLength].setImage(new Image(Objects.requireNonNull(Main.class.getResource("image/add.png")).toString()));
            I[sensorLength].setVisible(true);
            I[sensorLength].setEffect(null);
            I[sensorLength].setImage(new Image(Objects.requireNonNull(Main.class.getResource("image/plus.png")).toString()));
            C[sensorLength].setVisible(false);
        }
        for (int i=sensorLength+1; i<8; i++) Pane[i].setVisible(false);
    }

    public void openChooseLocation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChoosePlace.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Thêm địa điểm");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMouseClickedLogOut() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Confirm.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Đăng xuất");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void Remove(int index){
        data.removeSensor(data.sensorRegisterString[index]);
        RenderSensor();
    }

    public void chart(int index){
        if (index == sensorLength){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChooseSensor.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Thêm cảm biến");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Chart.fxml"));
                Stage stage = data.stage;
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Vẽ biểu đồ");
                stage.setScene(scene);
                ChartController chartController = fxmlLoader.getController();
                //chartController.setScene(username.getScene());
                data.setTypeId(data.sensorRegisterString[index]);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onMouseExitedLogOut() {
        LogOut.setTextFill(Color.BLACK);
    }

    public void onMouseMovedLogOut() {
        LogOut.setTextFill(Color.RED);
    }

    public void onMouseExitedLocation() {
        TextLocation.setFill(Color.BLACK);
    }

    public void onMouseMovedLocation() {
        TextLocation.setFill(Color.BLUE);
    }

    public void onMouseExitedPane1() {
        C1.setVisible(false);
    }

    public void onMouseExitedPane2() {
        C2.setVisible(false);
    }

    public void onMouseExitedPane3() {
        C3.setVisible(false);
    }

    public void onMouseExitedPane4() {
        C4.setVisible(false);
    }

    public void onMouseExitedPane5() {
        C5.setVisible(false);
    }

    public void onMouseExitedPane6() {
        C6.setVisible(false);
    }

    public void onMouseExitedPane7() {
        C7.setVisible(false);
    }

    public void onMouseExitedPane8() {
        C8.setVisible(false);
    }

    public void onMouseEnteredPane1() {
        if (sensorLength == 0) return;
        C1.setVisible(true);
    }

    public void onMouseEnteredPane2() {
        if (sensorLength == 1) return;
        C2.setVisible(true);
    }

    public void onMouseEnteredPane3() {
        if (sensorLength == 2) return;
        C3.setVisible(true);
    }

    public void onMouseEnteredPane4() {
        if (sensorLength == 3) return;
        C4.setVisible(true);
    }

    public void onMouseEnteredPane5() {
        if (sensorLength == 4) return;
        C5.setVisible(true);
    }

    public void onMouseEnteredPane6() {
        if (sensorLength == 5) return;
        C6.setVisible(true);
    }

    public void onMouseEnteredPane7() {
        if (sensorLength == 6) return;
        C7.setVisible(true);
    }

    public void onMouseEnteredPane8() {
        if (sensorLength == 7) return;
        C8.setVisible(true);
    }

    public void onMouseEnteredC1(){
        C1.setEffect(new Glow(1));
    }

    public void onMouseEnteredC2(){
        C2.setEffect(new Glow(1));
    }

    public void onMouseEnteredC3(){
        C3.setEffect(new Glow(1));
    }

    public void onMouseEnteredC4(){
        C4.setEffect(new Glow(1));
    }

    public void onMouseEnteredC5(){
        C5.setEffect(new Glow(1));
    }

    public void onMouseEnteredC6(){
        C6.setEffect(new Glow(1));
    }

    public void onMouseEnteredC7(){
        C7.setEffect(new Glow(1));
    }

    public void onMouseEnteredC8(){
        C8.setEffect(new Glow(1));
    }

    public void onMouseExitedC1(){
        C1.setEffect(null);
    }

    public void onMouseExitedC2(){
        C2.setEffect(null);
    }

    public void onMouseExitedC3(){
        C3.setEffect(null);
    }

    public void onMouseExitedC4(){
        C4.setEffect(null);
    }

    public void onMouseExitedC5(){
        C5.setEffect(null);
    }

    public void onMouseExitedC6(){
        C6.setEffect(null);
    }

    public void onMouseExitedC7(){
        C7.setEffect(null);
    }

    public void onMouseExitedC8(){
        C8.setEffect(null);
    }

    public void onMouseClickedC1(){
        Remove(0);
    }

    public void onMouseClickedC2(){
        Remove(1);
    }

    public void onMouseClickedC3(){
        Remove(2);
    }

    public void onMouseClickedC4(){
        Remove(3);
    }

    public void onMouseClickedC5(){
        Remove(4);
    }

    public void onMouseClickedC6(){
        Remove(5);
    }

    public void onMouseClickedC7(){
        Remove(6);
    }

    public void onMouseClickedC8(){
        Remove(7);
    }

    public void onMouseEnteredI1(){
        if(sensorLength == 0){
            I1.setEffect(dropShadow);
        } else
            I1.setVisible(true);
        Img1.setEffect(new BoxBlur());
    }

    public void onMouseEnteredI2(){
        if(sensorLength == 1){
            I2.setEffect(dropShadow);
        } else
            I2.setVisible(true);
        Img2.setEffect(new BoxBlur());
    }

    public void onMouseEnteredI3(){
        if(sensorLength == 2){
            I3.setEffect(dropShadow);
        } else
            I3.setVisible(true);
        Img3.setEffect(new BoxBlur());
    }

    public void onMouseEnteredI4(){
        if(sensorLength == 3){
            I4.setEffect(dropShadow);
        } else
            I4.setVisible(true);
        Img4.setEffect(new BoxBlur());
    }

    public void onMouseEnteredI5(){
        if(sensorLength == 4){
            I5.setEffect(dropShadow);
        } else
            I5.setVisible(true);
        Img5.setEffect(new BoxBlur());
    }

    public void onMouseEnteredI6(){
        if(sensorLength == 5){
            I6.setEffect(dropShadow);
        } else
            I6.setVisible(true);
        Img6.setEffect(new BoxBlur());
    }

    public void onMouseEnteredI7(){
        if(sensorLength == 6){
            I7.setEffect(dropShadow);
        } else
            I7.setVisible(true);
        Img7.setEffect(new BoxBlur());
    }

    public void onMouseEnteredI8(){
        if(sensorLength == 7){
            I8.setEffect(dropShadow);
        } else
            I8.setVisible(true);
        Img8.setEffect(new BoxBlur());
    }

    public void onMouseExitedI1(){
        if(sensorLength == 0){
            I1.setEffect(null);
        } else
            I1.setVisible(false);
        Img1.setEffect(null);
    }

    public void onMouseExitedI2(){
        if(sensorLength == 1){
            I2.setEffect(null);
        } else
            I2.setVisible(false);
        Img2.setEffect(null);
    }

    public void onMouseExitedI3(){
        if(sensorLength == 2){
            I3.setEffect(null);
        } else
            I3.setVisible(false);
        Img3.setEffect(null);
    }

    public void onMouseExitedI4(){
        if(sensorLength == 3){
            I4.setEffect(null);
        } else
            I4.setVisible(false);
        Img4.setEffect(null);
    }

    public void onMouseExitedI5(){
        if(sensorLength == 4){
            I5.setEffect(null);
        } else
            I5.setVisible(false);
        Img5.setEffect(null);
    }

    public void onMouseExitedI6(){
        if(sensorLength == 5){
            I6.setEffect(null);
        } else
            I6.setVisible(false);
        Img6.setEffect(null);
    }

    public void onMouseExitedI7(){
        if(sensorLength == 6){
            I7.setEffect(null);
        } else
            I7.setVisible(false);
        Img7.setEffect(null);
    }

    public void onMouseExitedI8(){
        if(sensorLength == 7){
            I8.setEffect(null);
        } else
            I8.setVisible(false);
        Img8.setEffect(null);
    }

    public void onMouseClickedI1(){
        chart(0);
    }

    public void onMouseClickedI2(){
        chart(1);
    }

    public void onMouseClickedI3(){
        chart(2);
    }

    public void onMouseClickedI4(){
        chart(3);
    }

    public void onMouseClickedI5(){
        chart(4);
    }

    public void onMouseClickedI6(){
        chart(5);
    }

    public void onMouseClickedI7(){
        chart(6);
    }

    public void onMouseClickedI8(){
        chart(7);
    }
}