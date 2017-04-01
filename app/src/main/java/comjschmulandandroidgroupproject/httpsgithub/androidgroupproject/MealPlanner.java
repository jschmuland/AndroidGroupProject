package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MealPlanner extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);
        ListView theList= (ListView)findViewById(R.id.theList);
        Button button=(Button)findViewById(R.id.mealsubmitbutton);
        EditText editText=(EditText)findViewById(R.id.editText);
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
        //
        try{
            NutritionQuery thread =
                    new NutritionQuery("https://service.livestrong.com/service/food/foods/?query=mango");
            thread.execute();
        }catch(Exception e){
            Log.d("errorforecast", e.toString());
        }

    }

    private class NutritionQuery extends AsyncTask<String, Integer, String[]>
    {
        String min;
        String max;

        String current;
        Bitmap image;
        ProgressBar progressBar;

        URL url, imageURL;
        String iconVal;
        String in = "in";
        public NutritionQuery(String url){
            progressBar=(ProgressBar)findViewById(R.id.progressBar);
            progressBar.setMax(100);
            try{
                this.url = new URL(url);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        }
        @Override
        protected String[] doInBackground(String ... args)
        {
            String[] entries = new String[4];
            boolean skiptheRest = false;
            try {

                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept-Encoding", "");
                conn.setReadTimeout(100000);
                conn.setConnectTimeout(150000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if(name.equals("query")){
                        Log.d("MEALPLANNER-API-QUERY", reader.nextString());
                    }else if(name.equals("foods")){
                        reader.beginArray();
                        while(reader.hasNext()){
                            reader.beginObject();
                            while(reader.hasNext()){
                                String currObj = reader.nextName();
                                if(currObj.equals("cals") && !skiptheRest){
                                    skiptheRest = true;
                                    double calories = reader.nextDouble();
                                    Log.d("MEALPLANNE-API-CALORIES", calories+"");
                                }else{

                                    reader.skipValue();

                                }
                            }
                            reader.endObject();
                        }
                        reader.endArray();
                    }else{
                        reader.skipValue();

                    }

                }
                reader.endObject();
                publishProgress(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return entries;
        }
        @Override
        protected void onProgressUpdate(Integer... prog){
            progressBar.setProgress(prog[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPostExecute(String[] res){
            super.onPostExecute(res);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
