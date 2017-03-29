package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


public class Exercise extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    public static String exerciseName;
    public static String exerciseCal;

    protected static final String ACTIVITY_NAME = "Exercise";
    ListView list;

    String[] exercisesListArray;
    String[] exercisesInfoArray;
    EditText setTimeInExercise;
    Double timeInExercise;

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseCal() {
        return exerciseCal;
    }

    public void setExerciseCal(String exerciseCal) {
        this.exerciseCal = exerciseCal;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        list = (ListView) findViewById(R.id.exerciseListView);
        exercisesListArray = getResources().getStringArray(R.array.exercises_array);
        exercisesInfoArray = getResources().getStringArray(R.array.exercises_calories_array);


        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter exercisesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exercisesListArray);
        exercisesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(exercisesArrayAdapter);



    }//end onCreate Method


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), exercisesListArray[position], Toast.LENGTH_LONG).show();

        String exerciseInfo = "Cal/min: " + exercisesInfoArray[position];


        setExerciseName(exercisesListArray[position]);
        setExerciseCal(exercisesInfoArray[position]);


        //temp string array to collect just one item
        String[] tempExercise = {exercisesListArray[position], "", exerciseInfo};

        //adapter object
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tempExercise);
        //display the exercise in the list view
        list.setAdapter(adapter);

        list.setOnItemClickListener(onListClick);

    }//end onItemSelected

    /**
     *
     */
    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            setTimeInExercise = (EditText) findViewById(R.id.timeEditText);

             try {
             timeInExercise = Double.valueOf(setTimeInExercise.getText().toString());
            } catch (NumberFormatException e){
              timeInExercise = 0.00;
             }

            Intent i = new Intent(Exercise.this, Exercise_info_class.class);
            i.putExtra(exerciseCal, getExerciseCal());
            i.putExtra(exerciseName, getExerciseName());
            i.putExtra("setTime", timeInExercise);
            startActivity(i);
        }
    };

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }//end onNothingSelected

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
        return true;
    }//end onCreateOptionsMenu

}//end Exercise Class
