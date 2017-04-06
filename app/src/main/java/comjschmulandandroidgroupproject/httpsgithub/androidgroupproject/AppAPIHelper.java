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

public class AppAPIHelper {
    private final String ACTIVITY_NAME = "AppAPIHelper";
    private String queryUrl = "https://service.livestrong.com/service/food/foods/?query=";

    public AppAPIHelper(){

    }

    public String makeQuery(String foodName){
        return queryUrl + foodName;
    }

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

    public Food readFood(JsonReader reader) throws IOException {
        String name = "";
        String foodName = "";
        int calories = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if(name.equals("item_title")) {
                foodName = reader.nextString();
            } else if (name.equals("cals")){
                calories = reader.nextInt();
            } else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Food(foodName, calories);
    }

    public ArrayList<Food> makeFoodArray(JsonReader reader) throws IOException {
        ArrayList<Food> foodList = new ArrayList<>();
        reader.beginArray();
        while(reader.hasNext()){
            foodList.add(readFood(reader));
        }
        reader.endArray();

        return foodList;
    }

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
