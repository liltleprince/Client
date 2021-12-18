package com.example.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.*;

import java.io.*;
import java.net.Socket;

public class Data {
    private static final Data data = new Data();
    public boolean Connected = false, DisConnect = true, Succeed = false, changeLocation = false, runGetData = false;
    public static Client client = new Client();
    public String[] locationString, sensorString, sensorRegisterString, sensorTypeValue;
    public String locationName = "NULL", TypeId = "NULL", TypeName, TypeValue;
    public Scene scene;
    public Stage stage;
    public Socket socket;
    public FXMLLoader fxmlLoaderWeather,fxmlLoaderLogin,fxmlLoaderChart,fxmlLoaderCreateAccount;
    JSONObject account = new JSONObject(), newAccount = new JSONObject(), locationId = new JSONObject(), fileSize, removeSensor = new JSONObject(), addSensor = new JSONObject(), infoSensor, lastAccount;
    JSONArray location, sensor, sensorRegister, dataInfoSensor, infoSensorNow, cookie;

    public void start(Stage stage) throws IOException {
        fxmlLoaderWeather = new FXMLLoader(Main.class.getResource("Weather.fxml"));
        fxmlLoaderLogin = new FXMLLoader(Main.class.getResource("Login.fxml"));
        fxmlLoaderChart = new FXMLLoader(Main.class.getResource("Chart.fxml"));
        fxmlLoaderCreateAccount = new FXMLLoader(Main.class.getResource("CreateAccount.fxml"));
        fxmlLoaderWeather.load();
        fxmlLoaderCreateAccount.load();
        fxmlLoaderLogin.load();
        fxmlLoaderChart.load();

        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private Data(){}

    public static Data getData(){
        return data;
    }

    public String VI_EN(String sensorName){
        return switch (sensorName) {
            case "Temperature" -> "Nhiệt độ";
            case "Humidity" -> "Độ ẩm";
            case "Amount of rain" -> "Lượng mưa";
            case "Foresight" -> "Tầm nhìn xa";
            case "Wind" -> "Sức gió";
            case "Cloud cover" -> "Mây che phủ";
            case "Possibility Of Rain" -> "Khả năng có mưa";
            case "Lightning" -> "Sấm sét";
            case "Nhiệt độ" -> "Temperature";
            case "Độ ẩm" -> "Humidity";
            case "Lượng mưa" -> "Amount of rain";
            case "Tầm nhìn xa" -> "Foresight";
            case "Sức gió" -> "Wind";
            case "Mây che phủ" -> "Cloud cover";
            case "Khả năng có mưa" -> "Possibility Of Rain";
            case "Sấm sét" -> "Lightning";
            default -> "";
        };
    }

    public void SetAccount(String Username, String Password){
        account.put("Username",Username);
        account.put("Password",Password);
    }

    public JSONObject getAccount(){
        return this.account;
    }

    public String getAccountToString(){
        return this.account.toString();
    }

    public void SetNewAccount(String Username, String Password){
        newAccount.put("Username",Username);
        newAccount.put("Password",Password);
    }

    public void setFileSize(String fileSize){
        this.fileSize = new JSONObject(fileSize);
    }

    public long getFileSize(){
        return Long.parseLong(fileSize.get("Filesize").toString());
    }

    public void setLocation(String location){
        this.location = new JSONArray(location);
        locationString = new String[this.location.length()];
        for(int i=0; i<this.location.length(); i++){
            JSONObject jsonObject = this.location.getJSONObject(i);
            locationString[i] = jsonObject.get("locationName").toString();
        }
    }

    public void setSensor(String sensor){
        this.sensor = new JSONArray(sensor);
        sensorString = new String[this.sensor.length()];
        for(int i=0; i<this.sensor.length(); i++){
            JSONObject jsonObject = this.sensor.getJSONObject(i);
            jsonObject.put("add",false);
            sensorString[i] = jsonObject.get("TypeName").toString();
        }
    }

    public void setSensorRegister(String sensorRegister){
        this.sensorRegister = new JSONArray(sensorRegister);
        sensorRegisterString = new String[this.sensorRegister.length()];
        sensorTypeValue = new String[this.sensorRegister.length()];
        for(int i=0; i<this.sensorRegister.length(); i++){
            JSONObject jsonObject = this.sensorRegister.getJSONObject(i);
            sensorRegisterString[i] = jsonObject.get("TypeName").toString();
            sensorTypeValue[i] = jsonObject.get("TypeValue").toString();
        }
    }

    public void setDataInfoSensor(String dataInfoSensor){
        this.dataInfoSensor = new JSONArray(dataInfoSensor);
    }

    public void setInfoSensorNow(String infoSensorNow){
        this.infoSensorNow = new JSONArray(infoSensorNow);
    }

    public void setLocationName(String locationName){
        if (locationName.equals(this.locationName)) return;
        this.locationName = locationName;
        this.changeLocation = true;
        for(int i=0; i<location.length(); i++){
            JSONObject jsonObject = this.location.getJSONObject(i);
            if(locationName.equals(jsonObject.get("locationName").toString())){
                setLocationId(jsonObject.get("locationID").toString());
                break;
            }
        }
        client.getSensorRegister();
        File file = new File("cookie.json");
        JSONObject object = new JSONObject();
        object.put("Username",account.get("Username").toString());
        object.put("LocationName",locationName);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            boolean check = true;
            for (int i=0; i<cookie.length(); i++)
                if (cookie.getJSONObject(i).get("Username").toString().equals(account.get("Username").toString())) {
                    check = false;
                    cookie.getJSONObject(i).put("LocationName",locationName);
                }
            if (check) cookie.put(object);
            bufferedWriter.write(cookie.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getLocationName(){
        File file = new File("cookie.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            StringBuilder json = new StringBuilder();
            while (line != null){
                json.append(line);
                line = reader.readLine();
            }
            if (json.length() == 0) json.append("[]");
            cookie = new JSONArray(json.toString());
            for (int i=0; i<cookie.length(); i++)
                if (cookie.getJSONObject(i).get("Username").toString().equals(account.get("Username").toString())) {
                    setLocationName(cookie.getJSONObject(i).get("LocationName").toString());
                }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLocationId(String locationId){
        this.locationId.put("LocationId",locationId);
    }

    public void setTypeId(String TypeName){
        this.TypeName = TypeName;
        for (int i=0; i<sensorRegister.length(); i++){
            JSONObject jsonObject = sensorRegister.getJSONObject(i);
            if (TypeName.equals(jsonObject.get("TypeName"))){
                TypeId = jsonObject.get("TypeID").toString();
                TypeValue = jsonObject.get("TypeValue").toString();
                break;
            }
        }
    }

    public String getNewAccountToString(){
        return this.newAccount.toString();
    }

    public void setSucceed(boolean succeed){
        this.Succeed = succeed;
    }

    public void setConnected(){
        this.Connected = true;
        this.DisConnect = false;
    }

    public void setDisConnect(){
        this.DisConnect = true;
        this.Connected = false;
    }

    public void setAddSensor(String addSensor){
        this.addSensor.put("LocationId",locationId.get("LocationId").toString());
        this.addSensor.put("TypeId",addSensor);
    }

    public void setRemoveSensor(String addSensor){
        this.removeSensor.put("LocationId",locationId.get("LocationId").toString());
        this.removeSensor.put("TypeId",addSensor);
    }

    public String getAddSensor(){
        return addSensor.toString();
    }

    public String getRemoveSensor(){
        return removeSensor.toString();
    }

    public void Connect(){
        client.Connect();
    }

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    public boolean checkLastAccount(){
        File file = new File("lastAccount.json");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            StringBuilder json = new StringBuilder();
            while (line != null){
                json.append(line);
                line = reader.readLine();
            }
            this.lastAccount = new JSONObject(json.toString());
            return !lastAccount.get("Username").equals("NULL");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLastUsername(){
        return lastAccount.get("Username").toString();
    }

    public void LogoutAccount(){
        File file = new File("lastAccount.json");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Username","NULL");
            jsonObject.put("Password","NULL");
            writer.write(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoginAccount(){
        File file = new File("lastAccount.json");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Username",account.get("Username"));
            jsonObject.put("Password",account.get("Password"));
            writer.write(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastPassword(){
        return lastAccount.get("Password").toString();
    }

    public void Login(){
        client.Login();
    }

    public void CreateAccount(){
        client.CreateAccount();
    }

    public void getLocation(){
        client.getLocation();
    }

    public void getSensor(){
        client.getSensor();
    }

    public void Logout(){
        client.Logout();
    }

    public void removeSensor(String sensorName){
        for(int i=0; i<sensorRegister.length(); i++){
            JSONObject jsonObject = this.sensorRegister.getJSONObject(i);
            if(sensorName.equals(jsonObject.get("TypeName").toString())){
                setRemoveSensor(jsonObject.get("TypeID").toString());
                sensorRegister.remove(i);
            }
        }
        sensorRegisterString = new String[this.sensorRegister.length()];
        sensorTypeValue = new String[this.sensorRegister.length()];
        for(int i=0; i<this.sensorRegister.length(); i++){
            JSONObject jsonObject = this.sensorRegister.getJSONObject(i);
            sensorRegisterString[i] = jsonObject.get("TypeName").toString();
            sensorTypeValue[i] = jsonObject.get("TypeValue").toString();
        }
        client.removeSensor();
    }

    public void addSensor(String sensorName){
        for(int i=0; i<sensor.length(); i++){
            JSONObject jsonObject = this.sensor.getJSONObject(i);
            if(sensorName.equals(jsonObject.get("TypeName").toString())){
                setAddSensor(jsonObject.get("TypeID").toString());
                sensorRegister.put(jsonObject);
            }
        }
        sensorRegisterString = new String[this.sensorRegister.length()];
        sensorTypeValue = new String[this.sensorRegister.length()];
        for(int i=0; i<this.sensorRegister.length(); i++){
            JSONObject jsonObject = this.sensorRegister.getJSONObject(i);
            sensorRegisterString[i] = jsonObject.get("TypeName").toString();
            sensorTypeValue[i] = jsonObject.get("TypeValue").toString();
        }
        client.addSensor();
    }

    public void getInfoSensor(String TypeName, String Date){
        infoSensor = new JSONObject();
        infoSensor.put("LocationId",locationId.get("LocationId"));
        infoSensor.put("TypeId",TypeId);
        infoSensor.put("Date",Date);
        infoSensor.put("TypeTime",TypeName);
        client.getInfoSensor();
    }

    public void getInfoSensorNow(){
        client.getInfoSensorNow();
    }

    public void quit(){
        client.quit();
    }
}
