package contacts;

import java.util.LinkedList;
import org.json.JSONObject;
import com.middleware.annotations.*;
import com.middleware.lifecycle_management.Strategy;

@RequestMap(router = "/contact")
public class Contacts {
    LinkedList<Contact> contacts = new LinkedList<Contact>();

    public Contacts(){}

    @Lifecycle(strategy = Strategy.STATIC)
    @Pool(maximum = 100)
    @Post(router = "/add")
    public JSONObject addContact(JSONObject json) throws Throwable {
        JSONObject response = new JSONObject();
        Contact temp = new Contact();

        if(json.has("name")){
            if(json.has("number")){
                if((searchContact(json).getFloat("code") == 0.0) || (searchContact(json).getFloat("code") == 1.0)){
                    temp.setName(json.getString("name"));
                    temp.setNumber(json.getString("number"));
                    contacts.add(temp);
                    response.put("DONE: ", "Contact \"" + temp.getName() + "\" is now in your list.");
                }
                else{
                    response.put("ERROR: ", "Contact with that name or number already exists in your list.");
                }
            }
            else{
                response.put("ERROR: ", "Contact number is unknown.");
            }
        }
        else{
            response.put("ERROR: ", "Contact name is unknown.");
        }

        return response;
    }

    @Lifecycle(strategy = Strategy.OPTIMIZED_STATIC)
    @Get(router = "/search")
    public JSONObject searchContact(JSONObject json) throws Throwable {
        JSONObject response = new JSONObject();

        if(contacts.size() == 0){
            response.put("code: ", 0.0);
            return response;
        }

        for(int i = 0; i < contacts.size(); i++){
            if(json.getString("name") == contacts.get(i).getName()){
                response.put("code: ", 2.0);
                response.put("index: ", i);
                return response;
            }
            else if(json.getString("number") == contacts.get(i).getNumber()){
                response.put("code: ", 2.0);
                response.put("index: ", i);
                return response;
            }
        }

        response.put("code: ", 1.0);
        return response;
    }

    @Lifecycle(strategy = Strategy.PER_REQUEST)
    @Delete(router = "/mul")
    public JSONObject deleteContact(JSONObject json) throws Throwable {
        JSONObject response = new JSONObject();
        JSONObject contact = searchContact(json);

        if(contact.has("index")){
            contacts.remove((int)contact.getFloat("index"));
            response.put("DONE: ", "Contact successfully removed.");
        }
        else{
            response.put("ERROR: ", "Contact not found in your list.");
        }

        return response;
    }
}
