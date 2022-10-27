package contacts;

import com.middleware.Medium;

public class Main {
    public static void main(String[] args){
        Contacts contacts = new Contacts();
        Medium middleware = new Medium();

        middleware.newRemoteObjects(contacts);
        middleware.start(8001);
    }
}
