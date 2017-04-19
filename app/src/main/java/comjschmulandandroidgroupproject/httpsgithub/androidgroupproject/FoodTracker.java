package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.FoodEaten;

/**
 * FoodTracker is the activity that tracks a users food consumption.
 * It displays the food consumed in a ListView and users can use the ToolBar
 * to navigate to other activities
 */

public class FoodTracker extends AppCompatActivity {
    //Activity name
    private final String ACTIVITY_NAME = "FoodTracker";
    //food eaten list
    private ArrayList<FoodEaten> foodObjArr;
    //button for adding food
    private Button addItemButton;
    //Text fields for food input, calorie input, date input and time input
    private EditText calorieInput, foodInput, dateInput, timeInput;
    //progress bar for loading food eaten history
    private ProgressBar progressBar;
    //loading text is the text view that shows the loading text
    private TextView loadingText;
    //list view for showing all the food eaten
    private ListView foodList;
    //adapter object for populating list view
    private FoodAdapter foodAdapter;
    //database helper
    private AppDBHelper dbHelper;
    //calendar object for time inputs
    private Calendar calendar;
    //String to store the food description - probably should not be a class variable
    private String description;

    //onCreate sets up the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_tracker);
        setTitle("Food Tracker");
        Log.i(ACTIVITY_NAME, "OnCreate called");

        //toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        //instantiate db helper
        dbHelper = new AppDBHelper(getApplicationContext());

        //get calendar instance
        calendar = Calendar.getInstance();

        //instantiate food lise
        foodObjArr = new ArrayList<>();

        //setting up action for addItem
        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "addItem clicked");
                //validating inputs
                if(!validateInputs()){
                    return;
                }

                //insert logic
                FoodEaten food = new FoodEaten(getFoodName(), getCalories(), getDateFromInputs(), getDescription());
                if(dbHelper.insertFoodEaten(food)){
                    Log.i(ACTIVITY_NAME, "Insert successful");
                    clearInputs();
                } else {
                    Log.i(ACTIVITY_NAME, "Insert Failed");
                }
                foodObjArr = dbHelper.getAllFoodEaten();
                foodAdapter.notifyDataSetChanged();

                //success snackbar
                makeSnackBar(R.string.successToast);
            }
        });

        //setting up calorie input for later use
        calorieInput = (EditText) findViewById(R.id.calorieInput);

        //setting up action for food input
        foodInput = (EditText) findViewById(R.id.foodInput);
        foodInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodTracker.this, FoodPicker.class);
                startActivityForResult(intent, 5);
            }
        });

        //setting up action for date input
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

        //setting up action for time input
        timeInput = (EditText) findViewById(R.id.timeInput);
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                timePicker(hour, minute).show();
            }
        });

        //setting up progress bar and loading text for later use
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingText = (TextView) findViewById(R.id.loadingText);

        //setting up list view and on click action
        foodList = (ListView) findViewById(R.id.foodList);
        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodEaten food = foodAdapter.getItem(position);
                showDetailsAlert(food, position);
            }
        });

        //setting visibility of widgets
        calorieInput.setVisibility(View.INVISIBLE);
        foodInput.setVisibility(View.INVISIBLE);
        dateInput.setVisibility(View.INVISIBLE);
        timeInput.setVisibility(View.INVISIBLE);
        addItemButton.setVisibility(View.INVISIBLE);
        foodList.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //setting up adapter with listview
        foodAdapter = new FoodAdapter(this);
        foodList.setAdapter(foodAdapter);

        //execute async task to load food history from db
        LoadFoodHistory history = new LoadFoodHistory();
        history.execute();

    }

    //sets up the toolbar options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ft_toolbar, menu);
        MenuItem foodItem = (MenuItem) menu.findItem(R.id.action_foodtracker);
        foodItem.setVisible(false);
        return true;
    }//end onCreateOptionsMenu

    //handles selection of toolbar items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()){
            //exercise activity
            case (R.id.action_exercise):
                intent = new Intent(FoodTracker.this, Exercise.class);
                startActivity(intent);
                return true;
            //meal planner activity
            case (R.id.action_mealplanner):
                intent = new Intent(FoodTracker.this, MealPlanner.class);
                startActivity(intent);
                return true;
            //sleep activity
            case(R.id.action_sleep):
                intent = new Intent(FoodTracker.this, SleepTracker.class);
                startActivity(intent);
                return true;
            //home activity
            case(R.id.action_home):
                finish();
                return true;
            //help dialog
            case(R.id.action_help):
                Log.i(ACTIVITY_NAME, "help");
                createHelpDialog();
                return true;
        }

        return false;

    }

    //creates the alert dialog for food item details and deletion
    public void showDetailsAlert(FoodEaten food, int position){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.food_details_layout, null);

        final FoodEaten foodFinal = food;
        final int pos = position;

        //setting food name to text view
        TextView foodText = (TextView) dialogView.findViewById(R.id.FoodNameValue);
        foodText.setText(food.getFoodName());

        //setting calories to text view
        TextView caloriesText = (TextView) dialogView.findViewById(R.id.CaloriesValue);
        caloriesText.setText(String.valueOf(food.getCalories()));

        //setting description to text view
        TextView descriptionText = (TextView) dialogView.findViewById(R.id.DescriptionValue);
        descriptionText.setText(food.getDescription());

        builder2.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteRow(foodFinal, pos);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
        });
        AlertDialog dialog2 = builder2.create();
        dialog2.setTitle(R.string.foodDetails);
        dialog2.getWindow().setLayout(400, 800);
        dialog2.show();
    }

    //creates the dialog for the help menu option
    public void createHelpDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.food_tracker_help_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder2.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog2 = builder2.create();
        dialog2.setTitle(R.string.foodtracker_help_title);
        dialog2.show();
    }

    //when returning to this activity from the FoodPicker set the food name and calories inputs
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 5) {
            Bundle bundle = data.getExtras();

            foodInput.setText(bundle.get("FOOD").toString());
            calorieInput.setText(String.valueOf(bundle.get("CALORIES")));
            description = bundle.get("DESCRIPTION").toString();
            Log.i(ACTIVITY_NAME, description);
        }
    }

    //deleteRow deletes the entry from the db and the array list
    private void deleteRow(FoodEaten food, int position){
        if(dbHelper.deleteFoodEaten(food)) {
            Log.i(ACTIVITY_NAME, "DELETED");
            foodObjArr.remove(position);
            foodAdapter.notifyDataSetChanged();
        }
    }

    // validates the inputs and displays messages for invalid input
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

    //makes a snackbar to show a message
    private void makeSnackBar(int message){
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), getText(message).toString(), Snackbar.LENGTH_SHORT);
        snack.show();
    }

    //makes a toast to show a message
    private void makeToast(int message) {
        Toast toast = Toast.makeText(FoodTracker.this, message, Toast.LENGTH_SHORT); //this is the ListActivity
        toast.show();
    }

    //creates the date picker dialog
    private DatePickerDialog datePicker(int year, int month, int day){

        DatePickerDialog.OnDateSetListener picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateInput.setText(year + "/" + (month+1) + "/" + dayOfMonth);
            }
        };
        return new DatePickerDialog(FoodTracker.this, picker, year, month, day);
    }

    //creates the time picker dialog
    private TimePickerDialog timePicker(int hour, int minute){

        TimePickerDialog.OnTimeSetListener picker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeInput.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute));
            }
        };
        return new TimePickerDialog(FoodTracker.this, picker, hour, minute, false);
    }

    //creates a Date object with the date info from the inputs
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

    //returns the food name from the inputs
    private String getFoodName(){
        if(foodInput.getText().toString().trim().equals("")){
            return null;
        }
        return foodInput.getText().toString();

    }

    //returns the description
    private String getDescription(){
        return description;
    }

    //gets the calories from the inputs as an integer
    private int getCalories(){
        if(calorieInput.getText().toString().trim().equals("")){
            return -1;
        }
        return Integer.parseInt(calorieInput.getText().toString());
    }

    //clears the input fields
    private void clearInputs(){
        Log.i(ACTIVITY_NAME, "clearInputs() called");
        foodInput.setText("");
        calorieInput.setText("");
        dateInput.setText("");
        timeInput.setText("");
    }

    //AsyncTask for loading the food history from the db
    private class LoadFoodHistory extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            Log.i(ACTIVITY_NAME, "loading food history");
            foodObjArr = dbHelper.getAllFoodEaten();
            foodAdapter.notifyDataSetChanged();

            try {
                //this is to simulate loading as the db is too fast with few records
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        //after it finishes it hides the progress bar
        // and loading text and makes the rest of the UI visible
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

    //FoodAdapter is the adapter for the food object array list
    private class FoodAdapter extends ArrayAdapter<FoodEaten> {

        public FoodAdapter(Context ctx) {
            super(ctx, 0);
        }

        //gets the count of the list
        @Override
        public int getCount() {
            return foodObjArr.size();
        }

        //inflates the list view
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = FoodTracker.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.food_eaten_item, null);

            TextView foodDate = (TextView) result.findViewById(R.id.food_date);
            foodDate.setText(getItem(position).toString()); // get the string at position
            return result;
        }

        //gets the position of the food
        @Nullable
        @Override
        public FoodEaten getItem(int position) {
            return foodObjArr.get(position);
        }

        //get the id of the object
        public long getItemId(int position) {
            return foodObjArr.get(position).getId();
        }
    }
}
