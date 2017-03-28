package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


public class Exercise extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    public static String exerciseName;
    public static String exerciseInfo;

    protected static final String ACTIVITY_NAME = "Exercise";
    ListView list;
    // String[] exercisesList = {"Basketball", "Bike", "Boxe", "Running", "Swimming", "Other"};

    String[] exercisesList;

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseInfo() {
        return exerciseInfo;
    }

    public void setExerciseInfo(String exerciseInfo) {
        this.exerciseInfo = exerciseInfo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        list = (ListView) findViewById(R.id.exerciseListView);
        exercisesList = getResources().getStringArray(R.array.exercises_array);


        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter exercisesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exercisesList);
        exercisesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(exercisesArrayAdapter);



    }//end onCreate Method


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), exercisesList[position], Toast.LENGTH_LONG).show();

        String exerciseInfo = "More Info";
        
        switch (position) {
            case 0:
                setExerciseName("Basketball");
                setExerciseInfo("Calories per 30 min = 300");
                break;
            case 1:
                setExerciseName("Bike");
                setExerciseInfo("Calories per 30 min = 310");
                break;
            case 2:
                setExerciseName("Boxe");
                setExerciseInfo("Calories per 30 min = 320");
                break;
            case 3:
                setExerciseName("Running");
                setExerciseInfo("Calories per 30 min = 330");
                break;
            case 4:
                setExerciseName("Swimming");
                setExerciseInfo("Calories per 30 min = 340");
                break;
            case 5:
                setExerciseName("Other");
                setExerciseInfo("Calories per 30 min = ???");
                break;
        }


        //temp string array to collect just one item
        String[] tempExercise = {exercisesList[position], "", exerciseInfo};

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
            Intent i = new Intent(Exercise.this, Exercise_info_class.class);
            i.putExtra(exerciseInfo, getExerciseInfo());
            i.putExtra(exerciseName, getExerciseName());
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
