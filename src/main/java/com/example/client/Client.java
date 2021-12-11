package com.example.client;

import java.io.*;
import java.net.Socket;

public class Client {
    public final static String SERVER_IP = "127.0.0.1";
    public final static int SERVER_PORT = 8000;
    public final static int BUFFER_SIZE = 1024;
    public final static byte[] BUFFER = new byte[BUFFER_SIZE];
    public static Socket socket;
    public static BufferedReader is;
    public static BufferedWriter os;
    public static Data data;
    public static DataInputStream recvFile;
    public static long len;
    public static String line;
    boolean connecting = false;

    public Client(){
        data = Data.getData();
    }

    public void Connect() {
        if (!connecting) System.out.println("Connecting to Server...");
        try {
            connecting = true;
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected: " + socket);
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            recvFile = new DataInputStream(socket.getInputStream());
            data.setConnected();
            data.setSocket(socket);
            connecting = false;
            //HELO Broker
            HELOBroker();
        } catch (IOException e) {
            data.setDisConnect();
        }
    }

    void HELOBroker(){
        sendMessage("CLIENT HELO Broker");
        readMessage("200 Helo Client");
    }

    public void Login(){
        sendMessage("SIGN IN");
        if (!readMessage("220 Sign in OK")) return;

        sendMessage("ACCOUNT");
        if (!readMessage("221 Account OK")) return;

        sendMessage(data.getAccountToString());
        readMessage("222 Login Success");
    }

    public void CreateAccount(){
        sendMessage("SIGN UP");
        if (!readMessage("210 Sign up OK")) return;

        sendMessage("ACCOUNT");
        if (!readMessage("211 Account OK")) return;

        sendMessage(data.getNewAccountToString());
        readMessage("212 Register Success");
    }

    public void Logout(){
        sendMessage("SIGN OUT");

        readMessage("230 Sign out OK");
    }

    public void getLocation(){
        sendMessage("GET LOCATION");

        if(!readMessage("240 GET Location OK")) return;

        readFile("location.json");
    }

    public void getSensor(){
        sendMessage("GET TYPE");

        if(!readMessage("241 GET Type Sensor OK")) return;

        readFile("sensor.json");
    }

    public void getSensorRegister(){
        sendMessage("GET INFO REGISTER");

        if(!readMessage("242 Info Register OK")) return;

        sendMessage(data.locationId.toString());

        readFile("sensorRegister.json");
    }

    public void addSensor(){
        sendMessage("ADD REGISTER");

        if(!readMessage("243 Add register OK")) return;

        sendMessage(data.getAddSensor());
    }

    public void removeSensor(){
        sendMessage("DELETE REGISTER");

        if(!readMessage("244 Delete register OK")) return;

        sendMessage(data.getRemoveSensor());
    }

    public void getInfoSensor(){
        sendMessage("GET INFO SENSOR");

        if(!readMessage("251 Get Info Sensor OK")) return;

        sendMessage(data.infoSensor.toString());

        readMessage("");
        //readFile("infoSensor.json");
    }

    public long readFileSize(){
        try {
            socket.setSoTimeout(3000);
            int n = recvFile.read(BUFFER,0,BUFFER_SIZE);
            if (n == -1){
                data.setDisConnect();
                System.out.println("DisConnect to Broker");
                data.setSucceed(false);
                return -1;
            }
            byte[] bytes = new byte[n];
            System.arraycopy(BUFFER, 0, bytes, 0, n);
            System.out.println("Broker: "+ new String(bytes));

            data.setFileSize(new String(bytes));
            return data.getFileSize();
        } catch (IOException e) {
            System.out.println("Time out connect to Server");
            return -1;
        }
    }

    public void readFile(String filename) {
        try {
            long fileSize = readFileSize();
            if (fileSize == -1) return;
            len = 0;
            File file = new File(filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (len < fileSize){
                socket.setSoTimeout(3000);
                int n = recvFile.read(BUFFER,0,BUFFER_SIZE);
                fileOutputStream.write(BUFFER,0,n);
                len += n;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            switch (filename) {
                case "location.json" -> data.setLocation(stringBuilder.toString());
                case "sensor.json" -> data.setSensor(stringBuilder.toString());
                case "sensorRegister.json" -> data.setSensorRegister(stringBuilder.toString());
            }
            data.setSucceed(true);
        } catch (IOException e){
            data.setSucceed(false);
            System.out.println("Error");
        }
    }

    public boolean readMessage(String message){
        try {
            socket.setSoTimeout(3000);
            int n = recvFile.read(BUFFER,0,BUFFER_SIZE);
            if (n == -1){
                data.setDisConnect();
                System.out.println("DisConnect to Broker");
                data.setSucceed(false);
                return false;
            }
            BUFFER[n] = '\0';
            byte[] bytes = new byte[n];
            System.arraycopy(BUFFER, 0, bytes, 0, n);
            System.out.println("Broker: "+ new String(bytes));

            if (message.equals(new String(bytes))){
                data.setSucceed(true);
                return true;
            }

            data.setSucceed(false);
            return false;
        } catch (IOException e) {
            System.out.println("Time out connect to Server");
            return false;
        }
    }

    public void sendMessage(String message){
        char[] chars = message.toCharArray();
        try {
            os.write(chars,0,chars.length);
            os.flush();
            System.out.println("Send to Broker: "+message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't connect to Broker");
        }
    }

    public void quit(){
        sendMessage("QUIT");

        readMessage("999 bye");
    }

}