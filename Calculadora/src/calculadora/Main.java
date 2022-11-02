package calculadora;

import com.middleware.Medium;

public class Main {
    public static void main(String[] args){
        Calculadora calculator = new Calculadora();
        Medium middleware = new Medium();

        middleware.newRemoteObjects(calculator);
        middleware.start(8001);
    }
}
