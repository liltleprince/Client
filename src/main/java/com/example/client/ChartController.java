package com.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ChartController {

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
    public StackedAreaChart StackedAreaChart;

    boolean start = true;
    Data data = Data.getData();
    XYChart.Series<Integer, Double> chart = new XYChart.Series<>();
    String styleDefault = "-fx-background-color: rgb(237,215,118); -fx-background-radius: 20px;";
    String styleChoose = "-fx-background-color: rgb(30,144,255); -fx-background-radius: 20px;";
    String TypeTime = "NULL";
    
    public void onMouseEnteredLogout(){
        Logout.setTextFill(Color.RED);
    }
    
    public void onMouseExitedLogout(){
        Logout.setTextFill(Color.BLACK);
    }
    
    public void onMouseClickedLogout(){
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Confirm.fxml"));
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
        data.scene.setRoot(data.fxmlLoaderWeather.getRoot());
        data.runGetData = true;
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
        String value , TypeTimeVI;
        switch (TypeTime){
            case "Day" -> value = Date.getValue().getDayOfMonth()+ "/" + Date.getValue().getMonthValue()+ "/" + Date.getValue().getYear();
            case "Month" -> value = Date.getValue().getMonthValue() + "/" + Date.getValue().getYear();
            default -> value = String.valueOf(Date.getValue().getYear());
        }
        switch (TypeTime){
            case "Day" -> TypeTimeVI = "Ngày";
            case "Month" -> TypeTimeVI = "Tháng";
            default -> TypeTimeVI = "Năm";
        }
        chart.getData().clear();
        if (data.dataInfoSensor.length() == 0){
            chart.setName("Xin lỗi! Không có dữ liệu ...");
            return;
        }
        chart.setName("Biểu đồ " + data.VI_EN(data.TypeName) + " " + TypeTimeVI + " " + value);
        x.setLowerBound((Integer) data.dataInfoSensor.getJSONObject(0).get("Time"));
        x.setUpperBound((Integer) data.dataInfoSensor.getJSONObject(data.dataInfoSensor.length()-1).get("Time"));
        x.setTickUnit(1);
        x.setAutoRanging(false);
        for (int i=0; i<data.dataInfoSensor.length(); i++){
            chart.getData().add(new XYChart.Data(data.dataInfoSensor.getJSONObject(i).get("Time"), data.dataInfoSensor.getJSONObject(i).get("Value")));
        }
    }

    public void start(){
        if (!start) return;
        start = false;
        username.setText(data.getAccount().get("Username").toString());
        x.setAutoRanging(false);
        StackedAreaChart.setTitle("");
        chart.setName("");
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
