package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Exercise_info_class extends AppCompatActivity {

    protected TextView exerciseInfoView;
    protected TextView exerciseNameView;
    protected TextView exerciseTimeView;
    protected TextView totalCalories;

    protected String passedName;
    protected String passedInfo;
    protected double timeInExercise;
    protected double calPerMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info_class);

        Exercise exercise = new Exercise();

        passedName = getIntent().getStringExtra(exercise.getExerciseName());
        passedInfo = getIntent().getStringExtra(exercise.getExerciseCal());
        timeInExercise = getIntent().getDoubleExtra("setTime", timeInExercise);
        calPerMinute = Double.parseDouble(passedInfo);

        exerciseInfoView = (TextView) findViewById(R.id.textViewExerciseInfo);
        exerciseNameView = (TextView) findViewById(R.id.textViewExerciseName);
        exerciseTimeView = (TextView) findViewById(R.id.textViewTimeInExercise);
        totalCalories = (TextView) findViewById(R.id.textViewTotalCalories);

        exerciseNameView.setText(passedName);
        exerciseInfoView.setText("calories per minute: "+ passedInfo);
        exerciseTimeView.setText("Time spent in activity: " + timeInExercise + " minutes");
        totalCalories.setText("You burned: " + calculateCalories(calPerMinute, timeInExercise) + " Calories");
    }//end onCreate

    public double calculateCalories(double calPerMinute, double timeInExercise) {

        return calPerMinute * timeInExercise;
    }

}//end Class
