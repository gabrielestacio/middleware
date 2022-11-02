package calculadora;

import org.json.*;
import com.middleware.annotations.RequestMap;
import com.middleware.lifecycle_management.Strategy;
import com.middleware.annotations.Lifecycle;
import com.middleware.annotations.Pool;
import com.middleware.annotations.*;

@RequestMap(router = "/calculator")
public class Calculadora {
	private float memoria = 0;
	
	public Calculadora() {}	
	
    @Lifecycle(strategy = Strategy.STATIC)
    @Post(router = "/soma")
    public JSONObject soma(JSONObject json) throws Throwable {
        float num1 = json.getFloat("var1");
        float num2 = json.getFloat("var2");

        JSONObject response = new JSONObject();
        response.put("response", num1 + num2);

        return response;
    }

    @Lifecycle(strategy = Strategy.OPTIMIZED_STATIC)
    @Delete(router = "/sub")
    public JSONObject sub(JSONObject json) throws Throwable {
        float num1 = json.getFloat("var1");
        float num2 = json.getFloat("var2");

        JSONObject response = new JSONObject();
        response.put("response", num1 - num2);

        return response;
    }

    @Lifecycle(strategy = Strategy.OPTIMIZED_PER_REQUEST)
    @Pool(maximum = 50)
    @Get(router = "/div")
    public JSONObject div(JSONObject json) throws Throwable {
        float num1 = json.getFloat("var1");
        float num2 = json.getFloat("var2");

        JSONObject response = new JSONObject();
        response.put("response", num1 / num2);

        return response;
    }

    @Lifecycle(strategy = Strategy.PER_REQUEST)
    @Put(router = "/mult")
    public JSONObject mult(JSONObject json) throws Throwable {
        float num1 = json.getFloat("var1");
        float num2 = json.getFloat("var2");
 
        JSONObject response = new JSONObject();
        response.put("response", num1 * num2);

        return response;
    }
    
    @Lifecycle(strategy = Strategy.STATIC)
    @Patch(router = "/somaMem")
    public JSONObject somaMem(JSONObject json) throws Throwable{
    	float num1 = json.getFloat("var1");
    	
    	JSONObject response = new JSONObject();
        response.put("response", memoria + num1);

        return response;
    }
    
    @Lifecycle(strategy = Strategy.OPTIMIZED_STATIC)
    @Delete(router = "/subMem")
    public JSONObject subMem(JSONObject json) throws Throwable{
    	float num1 = json.getFloat("var1");
    	
    	JSONObject response = new JSONObject();
        response.put("response", memoria - num1);

        return response;
    }
    
    @Lifecycle(strategy = Strategy.OPTIMIZED_PER_REQUEST)
    @Pool(maximum = 50)
    @Get(router = "/divMem")
    public JSONObject divMem(JSONObject json) throws Throwable{
    	float num1 = json.getFloat("var1");
    	
    	JSONObject response = new JSONObject();
        response.put("response", memoria / num1);

        return response;
    }
    
    @Lifecycle(strategy = Strategy.PER_REQUEST)
    @Put(router = "/multMem")
    public JSONObject multMem(JSONObject json) throws Throwable{
    	float num1 = json.getFloat("var1");
    	
    	JSONObject response = new JSONObject();
        response.put("response", memoria * num1);

        return response;
    }
}
