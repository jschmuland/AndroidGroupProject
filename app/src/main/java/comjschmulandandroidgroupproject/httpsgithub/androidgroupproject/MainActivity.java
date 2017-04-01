package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar calorieBar = (ProgressBar)findViewById(R.id.progressBar);
        calorieBar.setProgress(69);

        View sleepbtn = findViewById(R.id.mainSleepBtn);
        sleepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("StartActivity","User cliked Weather Button");
                Intent intent = new Intent(MainActivity.this, SleepTracker.class);
                startActivity(intent);
            }
        });

        View excercisebtn = findViewById(R.id.mainExcerciseBtn);
        excercisebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("StartActivity","User cliked the Exercise Button");
                Intent intent = new Intent(MainActivity.this, Exercise.class);
                startActivity(intent);
            }
        });

        View foodbtn = findViewById(R.id.mainFoodBtn);
        foodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("StartActivity","User cliked foodtracker Button");
                Intent intent = new Intent(MainActivity.this, FoodTracker.class);
                startActivity(intent);
            }
        });

        View mealbtn = findViewById(R.id.mainMealBtn);
        mealbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("StartActivity","User cliked MealPlanner Button");
                Intent intent = new Intent(MainActivity.this, MealPlanner.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
