package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.FoodEaten;

public class FoodTracker extends AppCompatActivity {
    private final String ACTIVITY_NAME = "FoodTracker";
    private ArrayList<String> foodArray;
    private ArrayList<FoodEaten> foodObjArr;
    private Button addItemButton;
    private EditText calorieInput, foodInput, dateInput, timeInput;
    private ProgressBar progressBar;
    private TextView loadingText;
    private ListView foodList;
    private FoodAdapter foodAdapter;
    private AppDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_tracker);
        setTitle("Food Tracker");
        Log.i(ACTIVITY_NAME, "OnCreate called");
        dbHelper = new AppDBHelper(getApplicationContext());

        foodArray = new ArrayList<>();
        foodObjArr = new ArrayList<>();

        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "addItem clicked");
                FoodEaten food = new FoodEaten(getFoodName(), getCalories(), getDateFromInputs());
                if(dbHelper.insertFoodEaten(food)){
                    Log.i(ACTIVITY_NAME, "Insert successful");
                    clearInputs();
                } else {
                    Log.i(ACTIVITY_NAME, "Insert Failed");
                }
                foodObjArr = dbHelper.getAllFoodEaten();
                foodAdapter.notifyDataSetChanged();
            }
        });

        calorieInput = (EditText) findViewById(R.id.calorieInput);
        foodInput = (EditText) findViewById(R.id.foodInput);
        dateInput = (EditText) findViewById(R.id.dateInput);
        timeInput = (EditText) findViewById(R.id.timeInput);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingText = (TextView) findViewById(R.id.loadingText);
        foodList = (ListView) findViewById(R.id.foodList);

        calorieInput.setVisibility(View.INVISIBLE);
        foodInput.setVisibility(View.INVISIBLE);
        dateInput.setVisibility(View.INVISIBLE);
        timeInput.setVisibility(View.INVISIBLE);
        addItemButton.setVisibility(View.INVISIBLE);
        foodList.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        foodAdapter = new FoodAdapter(this);
        foodList.setAdapter(foodAdapter);

        LoadFoodHistory history = new LoadFoodHistory();
        history.execute();




    }

    private Date getDateFromInputs() {
        long dateLong = 0;

        String dateTime = dateInput.getText() + " " + timeInput.getText();

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        try {
            dateLong = df.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date(dateLong);
    }

    private String getFoodName(){
        return foodInput.getText().toString();
    }

    private int getCalories(){
        return Integer.parseInt(calorieInput.getText().toString());
    }

    private void clearInputs(){
        Log.i(ACTIVITY_NAME, "clearInputs() called");
        foodInput.setText("");
        calorieInput.setText("");
        dateInput.setText("");
        timeInput.setText("");
    }

    private class LoadFoodHistory extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Log.i(ACTIVITY_NAME, "loading food history");
            foodObjArr = dbHelper.getAllFoodEaten();
            foodAdapter.notifyDataSetChanged();

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //component visibility
            progressBar.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);

            calorieInput.setVisibility(View.VISIBLE);
            foodInput.setVisibility(View.VISIBLE);
            addItemButton.setVisibility(View.VISIBLE);
            foodList.setVisibility(View.VISIBLE);
            timeInput.setVisibility(View.VISIBLE);
            dateInput.setVisibility(View.VISIBLE);
        }
    }

    private class FoodAdapter extends ArrayAdapter<FoodEaten> {

        public FoodAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return foodObjArr.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = FoodTracker.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.food_eaten_item, null);

            TextView foodDate = (TextView) result.findViewById(R.id.food_date);
            foodDate.setText(getItem(position).toString()); // get the string at position
            return result;
        }

        @Nullable
        @Override
        public FoodEaten getItem(int position) {
            return foodObjArr.get(position);
        }

        public long getItemId(int position) {
            return foodObjArr.get(position).getId();
        }
    }
}
