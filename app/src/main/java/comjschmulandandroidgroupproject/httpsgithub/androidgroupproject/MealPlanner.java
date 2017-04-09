


package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Meal;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.MealPlan;


public class MealPlanner extends AppCompatActivity {

    ArrayList<MealPlan> mealplan;
    AppDBHelper helper;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_meal_planner);
                    ListView theList = (ListView) findViewById(R.id.theList);
                    Button button = (Button) findViewById(R.id.mealsubmitbutton);
                    Button exit = (Button) findViewById(R.id.exitbutton);
                   final EditText editText = (EditText) findViewById(R.id.editText);
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    mealplan = new ArrayList<MealPlan>();
                    theList = (ListView) findViewById(R.id.theList);
                   final MealPlanAdapter adapter=new MealPlanAdapter(this);
                    theList.setAdapter(adapter);

                    //controls what gets changed in list
                    adapter.notifyDataSetChanged();



                    try{
                        NutritionQuery thread =
                                new NutritionQuery("https://service.livestrong.com/service/food/foods/?query=mango");
                        thread.execute();
                    }catch(Exception e){
                        Log.d("errorforecast", e.toString());
                    }

                    theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           Intent intent=new Intent(MealPlanner.this, MealActivity.class);
                           startActivityForResult(intent, 5);
                            }
                        });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
MealPlan mealPP=new MealPlan(editText.getText().toString());

                                  //insert returns true or false to make sure it inserts in DB then add to the arraylist
                            if(helper.insertMealPlan(mealPP)){
                                mealplan.add(mealPP);
                                adapter.notifyDataSetChanged();
                            }
                            editText.setText("");
                        }
                    });
//My Toast

                    Context context = getApplicationContext();
                    CharSequence text;
                    int duration = Toast.LENGTH_SHORT;



                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"Enter Name of Meal Plan",Toast.LENGTH_SHORT).show();
                        }
                    });
//Snackbar

                    Snackbar.make(findViewById(android.R.id.content), "Make your meal plan", Snackbar.LENGTH_LONG)
                            .show();

//Custom Dialogue


                    exit.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            //Intent myIntent = new Intent(view.getContext(), agones.class);
                            //startActivityForResult(myIntent, 0);


                    AlertDialog.Builder builder = new AlertDialog.Builder(MealPlanner.this);
// 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage(R.string.Dialogue) //Add a dialog message to strings.xml

                            .setTitle(R.string.Dialogue_Title)
                            .setPositiveButton(R.string.Dialogue_Positive, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button

                                    Intent resultIntent = new Intent(  );
                                    resultIntent.putExtra("Response", "My information to share");
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();//you don't have to set it up to pass it between activities because it finishes and goes back to the previous activity

                                }
                            })
                            .setNegativeButton(R.string.Dialogue_Negative, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            })
                            .show();
                        }

                    });
                }










                private class NutritionQuery extends AsyncTask<String, Integer, String[]> {


                    private URL url=null;
                    ProgressBar progressBar;


                    String in = "in";

                    public NutritionQuery(String url) {
                        progressBar=(ProgressBar)findViewById(R.id.progressBar);
                        progressBar.setMax(100);
                        try {
                            this.url = new URL(url);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected String[] doInBackground(String... args) {
                        String[] entries = new String[4];
                        boolean skiptheRest = false;
                        publishProgress(1);
                        try {

                            HttpURLConnection conn = null;
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestProperty("Accept-Encoding", "");
                            conn.setReadTimeout(100000);
                            conn.setConnectTimeout(150000);
                            conn.setRequestMethod("GET");
                            conn.setDoInput(true);
                            conn.connect();
                            publishProgress(25);
                            JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                            reader.beginObject();
                            while (reader.hasNext()) {
                                String name = reader.nextName();
                                if (name.equals("query")) {
                                    Log.d("MEALPLANNER-API-QUERY", reader.nextString());
                                } else if (name.equals("foods")) {
                                    reader.beginArray();
                                    while (reader.hasNext()) {
                                        reader.beginObject();
                                        while (reader.hasNext()) {
                                            String currObj = reader.nextName();
                                            if (currObj.equals("cals") && !skiptheRest) {
                                                skiptheRest = true;
                                                double calories = reader.nextDouble();
                                                Log.d("MEALPLANNE-API-CALORIES", calories + "");
                                                publishProgress(50);
                                            } else {

                                                reader.skipValue();

                                            }
                                        }
                                        reader.endObject();
                                        publishProgress(65);
                                    }
                                    reader.endArray();
                                } else {
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
                    protected void onProgressUpdate(Integer... prog) {
                           progressBar.setProgress(prog[0]);
                         progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPostExecute(String[] res) {
                        super.onPostExecute(res);
                         progressBar.setVisibility(View.INVISIBLE);
                    }
            }

    private class MealPlanAdapter extends ArrayAdapter<MealPlan>{

        public MealPlanAdapter (Context ctx) {
            super(ctx, 0);
        }
        public	int getCount(){return mealplan.size();
        }
        public MealPlan getItem(int position){
            return mealplan.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            //Just specifying the chat window is going to use what layout for each item????
            LayoutInflater inflater = MealPlanner.this.getLayoutInflater();
            View result = null ;

                result = inflater.inflate(R.layout.food_picker_row, null);
                TextView  fooditem= (TextView) result.findViewById(R.id.tp_foodName);
                fooditem.setText(getItem(position).getPlanName()); // get the string at position
                return result;
        }



    }


}









