package com.example.client;

public class Data {
    private static final Data data = new Data();
    private Data(){}

    public static Data getData(){
        return data;
    }
}
