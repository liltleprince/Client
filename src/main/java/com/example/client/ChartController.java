package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.ResourceBundle;

public class ChartController implements Initializable {

    @FXML
    public DatePicker Date;
    @FXML
    public Label username;
    @FXML
    public ImageView Back;
    @FXML
    public Label Logout;
    @FXML
    public Button Day;
    @FXML
    public Button Month;
    @FXML
    public Button Year;
    @FXML
    public NumberAxis y;
    @FXML
    public NumberAxis x;
    @FXML
    public javafx.scene.chart.StackedAreaChart StackedAreaChart;

    Scene scene;
    Data data = Data.getData();
    XYChart.Series<Integer, Double> chart = new XYChart.Series<>();
    String styleDefault = "-fx-background-color: rgb(237,215,118); -fx-background-radius: 20px;";
    String styleChoose = "-fx-background-color: rgb(30,144,255); -fx-background-radius: 20px;";
    String TypeTime = "NULL";

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void onMouseEnteredLogout(){
        Logout.setTextFill(Color.RED);
    }

    public void onMouseExitedLogout(){
        Logout.setTextFill(Color.BLACK);
    }

    public void onMouseClickedLogout(){
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

    public void onMouseEnteredBack(){
        Back.setEffect(new DropShadow());
    }

    public void onMouseExitedBack(){
        Back.setEffect(null);
    }

    public void onMouseClickedBack(){
        Stage stage = (Stage) Back.getScene().getWindow();
        stage.setScene(scene);
    }

    public void onMouseEnteredDay(){
        Day.setEffect(new DropShadow());
    }

    public void onMouseExitedDay(){
        Day.setEffect(null);
    }

    public void onMouseClickedDay(){
        if (TypeTime.equals("Day")) return;
        TypeTime = "Day";
        Day.setStyle(styleChoose);
        Month.setStyle(styleDefault);
        Year.setStyle(styleDefault);
        chart();
    }

    public void onMouseEnteredMonth(){
        Month.setEffect(new DropShadow());
    }

    public void onMouseExitedMonth(){
        Month.setEffect(null);
    }

    public void onMouseClickedMonth(){
        if (TypeTime.equals("Month")) return;
        TypeTime = "Month";
        Day.setStyle(styleDefault);
        Month.setStyle(styleChoose);
        Year.setStyle(styleDefault);
        chart();
    }

    public void onMouseEnteredYear(){
        Year.setEffect(new DropShadow());
    }

    public void onMouseExitedYear(){
        Year.setEffect(null);
    }

    public void onMouseClickedYear(){
        if (TypeTime.equals("Year")) return;
        TypeTime = "Year";
        Day.setStyle(styleDefault);
        Month.setStyle(styleDefault);
        Year.setStyle(styleChoose);
        chart();
    }

    public void chart(){
        if (TypeTime.equals("NULL") || Date.getValue() == null) return;
        data.getInfoSensor(TypeTime, String.valueOf(Date.getValue()));
        x.setLowerBound(1);
        x.setUpperBound(12);
        x.setTickUnit(1);
        x.setAutoRanging(false);
        chart.getData().add(new XYChart.Data(1,10.5));
        chart.getData().add(new XYChart.Data(2,20));
        chart.getData().add(new XYChart.Data(3,35));
        chart.getData().add(new XYChart.Data(4,50));
        chart.getData().add(new XYChart.Data(5,5));
        chart.getData().add(new XYChart.Data(6,2));
        chart.getData().add(new XYChart.Data(7,1));
        chart.getData().add(new XYChart.Data(8,25));
        chart.getData().add(new XYChart.Data(9,30));
        chart.getData().add(new XYChart.Data(10,100));
        chart.getData().add(new XYChart.Data(11,5));
        chart.getData().add(new XYChart.Data(12,45));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText(data.getAccount().get("Username").toString());
        x.setAutoRanging(false);
        StackedAreaChart.setTitle("");
        StackedAreaChart.getData().add(chart);
        Date.setConverter(new StringConverter<>()
        {
            private final DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        Date.valueProperty().addListener((ov, oldValue, newValue) -> {
            chart();
        });
    }
}