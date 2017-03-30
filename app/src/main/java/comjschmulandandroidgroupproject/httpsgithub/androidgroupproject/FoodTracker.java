package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FoodTracker extends AppCompatActivity {

    Button addItemButton;
    EditText calorieInput, foodInput, dateInput, timeInput;
    ProgressBar progressBar;
    TextView loadingText;
    ListView foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_tracker);
        setTitle("Food Tracker");

        addItemButton = (Button) findViewById(R.id.addItemButton);
        calorieInput = (EditText) findViewById(R.id.calorieInput);
        foodInput = (EditText) findViewById(R.id.foodInput);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadingText = (TextView) findViewById(R.id.loadingText);
        foodList = (ListView) findViewById(R.id.foodList);

        calorieInput.setVisibility(View.INVISIBLE);
        foodInput.setVisibility(View.INVISIBLE);
        addItemButton.setVisibility(View.INVISIBLE);
        foodList.setVisibility(View.INVISIBLE);


        LoadFoodHistory history = new LoadFoodHistory();
        history.execute();


    }

    private class LoadFoodHistory extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
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
        }
    }
}