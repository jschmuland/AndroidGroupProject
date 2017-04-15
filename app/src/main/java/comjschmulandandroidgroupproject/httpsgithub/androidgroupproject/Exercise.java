package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support.Exercise_info_class;


public class Exercise extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String exerciseName, exerciseCal;
    protected static final String ACTIVITY_NAME = "Exercise";
    protected ListView list;
    protected String[] exercisesListArray, exercisesInfoArray;
    protected EditText setTimeInExercise;
    protected Double timeInExercise;
    protected ImageView exerciseImageView;
    protected String URL;
    boolean isTablet;

    /**
     * @return exerciseName return the exercise name
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * @param exerciseName set the exercise name
     */
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * @return ExerciseCal return the exercise calories / minute
     */
    public String getExerciseCal() {
        return exerciseCal;
    }

    /**
     * @param exerciseCal set the exercise calories / minute
     */
    public void setExerciseCal(String exerciseCal) {
        this.exerciseCal = exerciseCal;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        /*---------------find the objects references-------------*/
        list = (ListView) findViewById(R.id.exerciseListView);
        exerciseImageView = (ImageView) findViewById(R.id.exerciseImageView);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        exercisesListArray = getResources().getStringArray(R.array.exercises_array);
        exercisesInfoArray = getResources().getStringArray(R.array.exercises_calories_array);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter exercisesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exercisesListArray);
        exercisesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(exercisesArrayAdapter);

        isTablet = (findViewById(R.id.exerciseFrameLayout) != null); //find out if this is a phone or tablet

        setTitle(getString(R.string.exerciseTitle));
    }//end onCreate Method


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                URL = "http://icons.iconarchive.com/icons/icons-land/sport/64/Basketball-Ball-icon.png";
                break;
            case 1:
                URL = "http://icons.iconarchive.com/icons/sportsbettingspot/summer-olympics/96/boxing-icon.png";
                break;
            case 2:
                URL = "http://icons.iconarchive.com/icons/sportsbettingspot/summer-olympics/96/cycling-road-icon.png";
                break;
            case 3:
                URL = "http://icons.iconarchive.com/icons/mattrich/adidas/96/Adidas-Shoe-icon.png";
                break;
            case 4:
                URL = "http://icons.iconarchive.com/icons/icons8/windows-8/96/Sports-Swimming-icon.png";
                break;
            case 5:
                URL = "http://icons.iconarchive.com/icons/poseit/football/96/football-icon.png";
                break;

        }//end switch

        GetExerciseImage retrieveExerciseImage = new GetExerciseImage();
        retrieveExerciseImage.execute(URL);

        //Toast message when click on the exercise name
        Toast.makeText(getApplicationContext(), exercisesListArray[position], Toast.LENGTH_LONG).show();

        //create a string with the calories/minute
        String exerciseInfo = getString(R.string.caloriesPerMinute) + " " + exercisesInfoArray[position];


        //set the exercise name and info
        setExerciseName(exercisesListArray[position]);
        setExerciseCal(exercisesInfoArray[position]);

        //temp string array to collect just one item
        String[] tempExercise = {exercisesListArray[position], exerciseInfo, getString(R.string.exerciseClickHere)};

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
            } catch (NumberFormatException e) {
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

    /**
     * Private class created to retrieve and image icon and set it to the Sport bitmap
     */
    private class GetExerciseImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }//end doInBackGround

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            exerciseImageView.setImageBitmap(result);
        }//end onPostExecute

        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

    }//end getExerciseImage class

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ft_toolbar, menu);
        MenuItem exerciseItem = menu.findItem(R.id.action_exercise);
        exerciseItem.setVisible(false);
        return true;
    }//end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case (R.id.action_foodtracker):
                intent = new Intent(Exercise.this, FoodTracker.class);
                startActivity(intent);
                return true;
            case (R.id.action_mealplanner):
                intent = new Intent(Exercise.this, MealPlanner.class);
                startActivity(intent);
                return true;
            case(R.id.action_sleep):
                intent = new Intent(Exercise.this, SleepTracker.class);
                startActivity(intent);
                return true;
            case(R.id.action_home):
                finish();
                return true;
            case(R.id.action_help):
                Log.i(ACTIVITY_NAME, "help");
                createHelpDialog();
                return true;
        }

        return false;

    }//end onOptionSelectedMenu

    public void createHelpDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exercise_help_layout, null);
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
        dialog2.setTitle(R.string.exercise_help_title);
        dialog2.show();
    }//end createHelpDialog

}//end Exercise Class

