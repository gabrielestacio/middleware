package com.carro;

import com.middleware.Medium;

public class Main {
    public static void main(String[] args){
        Carro car = new Carro();
        Medium middleware = new Medium();

        middleware.newRemoteObjects(car);
        middleware.start(8001);
    }
}
