package calculator;

import

public class Main {
    public static void main(String[] args){
        Calculadora contacts = new Contacts();
        Medium middleware = new Medium();

        middleware.newRemoteObjects(contacts);
        middleware.start(8001);
    }
}
