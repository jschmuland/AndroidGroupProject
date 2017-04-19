package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Food;

/**
 * AppAPIHelper is the helper class for accessing the Livestron API
 */

public class AppAPIHelper {
    //ACTIVITY_NAME for debugging, queryURL is the URL for the API
    private final String ACTIVITY_NAME = "AppAPIHelper";
    private String queryUrl = "https://service.livestrong.com/service/food/foods/?query=";

    //constructor
    public AppAPIHelper(){
    }

    //makeQuery(String foodName) takes a food name and returns the API query
    public String makeQuery(String foodName){
        return queryUrl + foodName;
    }

    //openConnection(String query) takes a query and opens a HttpURLConnection,
    // throws exceptions if the URL is not good or IOException if it doesn't connect
    public HttpURLConnection openConnection(String query){
        URL url = null;

        try {
            url = new URL(query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "");
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
        } catch (IOException e) {
            Log.e(ACTIVITY_NAME, "Error with URL connection");
        }
        return conn;
    }

    //readFood takes a JsonReader object and returns Food objects with the data from the JSON,
    //throws IOException if there are any errors with reading
    public Food readFood(JsonReader reader) throws IOException {
        String name = "";
        String foodName = "";
        int calories = 0;
        String description = "";

        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if(name.equals("item_title")) {
                foodName = reader.nextString();
            } else if (name.equals("cals")) {
                calories = reader.nextInt();
            } else if (name.equals("item_desc")){
                description = reader.nextString();
            } else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Food(foodName, calories, description);
    }

    //makeFoodArray(JsonReader reader) takes a JsonReader and reads food arrays from the JSON
    // and returns an ArrayList<Food>
    public ArrayList<Food> makeFoodArray(JsonReader reader) throws IOException {
        ArrayList<Food> foodList = new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext()){
            foodList.add(readFood(reader));
        }
        reader.endArray();

        return foodList;
    }

    //searchFood(HttpURLConnection conn) takes in a HttpURLConnection and queries the API
    // for JSON results
    public ArrayList<Food> searchFood(HttpURLConnection conn) {
        ArrayList<Food> foodList = null;
        JsonReader reader;
        String name = "";

        try {
            reader = new JsonReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            reader.beginObject();
            while (reader.hasNext()) {
                name = reader.nextName();
                if(name.equals("foods")){
                   foodList = makeFoodArray(reader);
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foodList;
    }

}
