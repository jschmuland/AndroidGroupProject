package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.FoodEaten;

public class FoodTracker extends AppCompatActivity {
    private final String ACTIVITY_NAME = "FoodTracker";
    private ArrayList<FoodEaten> foodObjArr;
    private Button addItemButton;
    private EditText calorieInput, foodInput, dateInput, timeInput;
    private ProgressBar progressBar;
    private TextView loadingText;
    private ListView foodList;
    private FoodAdapter foodAdapter;
    private AppDBHelper dbHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_tracker);
        setTitle("Food Tracker");
        Log.i(ACTIVITY_NAME, "OnCreate called");
        dbHelper = new AppDBHelper(getApplicationContext());

        calendar = Calendar.getInstance();

        foodObjArr = new ArrayList<>();

        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "addItem clicked");

                if(!validateInputs()){
                    return;
                }

                FoodEaten food = new FoodEaten(getFoodName(), getCalories(), getDateFromInputs());
                if(dbHelper.insertFoodEaten(food)){
                    Log.i(ACTIVITY_NAME, "Insert successful");
                    clearInputs();
                } else {
                    Log.i(ACTIVITY_NAME, "Insert Failed");
                }
                foodObjArr = dbHelper.getAllFoodEaten();
                foodAdapter.notifyDataSetChanged();
                
                makeSnackBar(R.string.successToast);
            }
        });

        calorieInput = (EditText) findViewById(R.id.calorieInput);
        foodInput = (EditText) findViewById(R.id.foodInput);
        foodInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodTracker.this, FoodPicker.class);
                startActivityForResult(intent, 5);
            }
        });

        dateInput = (EditText) findViewById(R.id.dateInput);
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePicker(year, month, day).show();
            }
        });

        timeInput = (EditText) findViewById(R.id.timeInput);
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                timePicker(hour, minute).show();
            }
        });

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

    private boolean validateInputs(){
        Toast toast;
        if(getFoodName() == null){
            makeToast(R.string.enter_food);
            return false;
        } else if (getCalories() == -1) {
            makeToast(R.string.enter_calories);
            return false;
        } else if (getDateFromInputs() == null) {
            makeToast(R.string.enter_date);
            return false;
        }
        return true;
    }

    private void makeSnackBar(int message){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), getText(message).toString(), Snackbar.LENGTH_SHORT);
        snack.show();
    }

    private void makeToast(int message) {
        Toast toast = Toast.makeText(FoodTracker.this, message, Toast.LENGTH_SHORT); //this is the ListActivity
        toast.show();
    }

    private DatePickerDialog datePicker(int year, int month, int day){

        DatePickerDialog.OnDateSetListener picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateInput.setText(year + "/" + (month+1) + "/" + dayOfMonth);
            }
        };
        return new DatePickerDialog(FoodTracker.this, picker, year, month, day);
    }

    private TimePickerDialog timePicker(int hour, int minute){

        TimePickerDialog.OnTimeSetListener picker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeInput.setText((hourOfDay == 0 ? "00" : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute));
            }
        };
        return new TimePickerDialog(FoodTracker.this, picker, hour, minute, false);
    }

    private Date getDateFromInputs() {
        if(dateInput.getText().toString().trim().equals("") || timeInput.getText().toString().trim().equals("")){
            return null;
        }
        long dateLong = 0;

        String dateTime = dateInput.getText() + " " + timeInput.getText();

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            dateLong = df.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date(dateLong);
    }

    private String getFoodName(){
        if(foodInput.getText().toString().trim().equals("")){
            return null;
        }
        return foodInput.getText().toString();

    }

    private int getCalories(){
        if(calorieInput.getText().toString().trim().equals("")){
            return -1;
        }
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
