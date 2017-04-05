package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;


import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Date;



public class Exercise_info_class extends AppCompatActivity {

    protected TextView exerciseInfoView, exerciseNameView, exerciseTimeView, totalCalories, textViewDate;
    protected Button exerciseDataBase;
    protected String currentDateTimeString, passedName, passedInfo;
    protected double timeInExercise, calPerMinute;
    protected Context ctx;
    protected ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info_class);
        ctx=this;
        Exercise exercise = new Exercise();


        /*--------------------receiving values from the Exercise Class-----------*/
        passedName = getIntent().getStringExtra(exercise.getExerciseName());
        passedInfo = getIntent().getStringExtra(exercise.getExerciseCal());
        timeInExercise = getIntent().getDoubleExtra("setTime", timeInExercise);
        calPerMinute = Double.parseDouble(passedInfo);

        /*-----------------find all TextViews references----------------*/
        exerciseInfoView = (TextView) findViewById(R.id.textViewExerciseInfo);
        exerciseNameView = (TextView) findViewById(R.id.textViewExerciseName);
        exerciseTimeView = (TextView) findViewById(R.id.textViewTimeInExercise);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        totalCalories = (TextView) findViewById(R.id.textViewTotalCalories);
        bar = (ProgressBar) findViewById(R.id.progressBarExercise);
        bar.setVisibility(View.VISIBLE);


        /*--------------------Set values to the TextViews-------------------*/
        exerciseNameView.setText(passedName);
        exerciseInfoView.setText("calories per minute: "+ passedInfo);
        exerciseTimeView.setText("Time spent in activity: " + timeInExercise + " minutes");
        totalCalories.setText("You burned: " + calculateCalories(calPerMinute, timeInExercise) + " Calories");


        /*----------------------DATE----------------------*/
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast.makeText(getApplicationContext(), currentDateTimeString, Toast.LENGTH_LONG).show();
        final String dateSubstring = currentDateTimeString.substring(0, 11);
        textViewDate.setText(dateSubstring);


        /*--------------------Set action for the send Button-----------------------*/
        exerciseDataBase = (Button) findViewById(R.id.exerciseButtonSend);
        exerciseDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder secondBuilder = new AlertDialog.Builder(ctx);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                secondBuilder.setView(inflater.inflate(R.layout.exercise_message_dialog, null))

                        // Add action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Snackbar.make(v, "Saving on Database", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Snackbar.make(v, "You did cancel the Action", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });

                secondBuilder.show();
            }
        });



    }//end onCreate

    /**
     * Method used to return the total calories burned in exercise
     * @param calPerMinute
     * @param timeInExercise
     * @return totalCalories
     */
    public double calculateCalories(double calPerMinute, double timeInExercise) {

        double totalCalories = calPerMinute * timeInExercise;
        return  totalCalories;
    }//end calculateCalories method

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }//end onCreateOptionsMenu

}//end Class
