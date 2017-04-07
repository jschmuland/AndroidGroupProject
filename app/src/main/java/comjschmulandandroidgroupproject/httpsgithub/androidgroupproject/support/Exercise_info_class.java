package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.AppDBHelper;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.Exercise;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.ExerciseRecords;


public class Exercise_info_class extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ExerciseRecords Info Activity";
    protected TextView exerciseInfoView, exerciseNameView, exerciseTimeView, totalCalories, textViewDate;
    protected Button exerciseDataBase;
    protected String currentDateTimeString, passedName, passedInfo, dateSubstring;
    protected double timeInExercise, calPerMinute, totalCaloriesDouble;
    protected Context ctx;
    protected ProgressBar bar;
    boolean isTablet;
    protected ListView listView;
    final ArrayList<ExerciseRecords> exerciseObjArray = new ArrayList<>();
    ExerciseAdapter exerciseAdapter;
    AppDBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info_class);
        ctx = this;
        Exercise exercise = new Exercise();
        exerciseAdapter = new ExerciseAdapter(this);

        /*--------------------receiving values from the ExerciseRecords Class-----------*/
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
        bar.setMax(500);
        bar.setVisibility(View.VISIBLE);

        //getting Sleep array in Async Task
        ExerciseQuery sq = new ExerciseQuery();
        sq.execute(this);
        isTablet = (findViewById(R.id.exerciseFrameLayout) != null); //find out if this is a phone or tablet

        /**-----------------LISTVIEW UPDATE----------------------*/
        listView = (ListView) findViewById(R.id.listViewExercise);


        final ExerciseAdapter messageAdapter = new ExerciseAdapter(this);
        listView.setAdapter(messageAdapter);


       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ExerciseRecords exerciseObj = messageAdapter.getItem(position);

                Bundle bun = new Bundle();
                bun.putInt("ID", exerciseObj.getId());//l is the database ID of selected item
                bun.putString("DATE", exerciseObj.getDate());
                bun.putString("NAME", exerciseObj.getExerciseName());
                bun.putDouble("CALORIES", exerciseObj.getCalories());
                bun.putDouble("DURATION", exerciseObj.getDuration());



                if(isTablet) {

                    ExerciseFragment frag = new ExerciseFragment();
                    frag.setArguments(bun);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.exerciseFrameLayout, frag).addToBackStack("ID").commit();

                } else //isPhone
                {
                    Intent intent = new Intent(Exercise_info_class.this, ExerciseMessageDetails.class);
                    intent.putExtra("ID", exerciseObj.getId()); //pass the Database ID to next activity
                    intent.putExtra("DATE", exerciseObj.getDate());
                    intent.putExtra("NAME", exerciseObj.getExerciseName());
                    intent.putExtra("CALORIES", exerciseObj.getCalories());
                    intent.putExtra("DURATION", exerciseObj.getDuration());
                    startActivityForResult(intent,1,bun);
                }

            }
        });


        /*--------------------Set values to the TextViews-------------------*/
        exerciseNameView.setText(passedName);
        exerciseInfoView.setText("calories per minute: " + passedInfo);
        exerciseTimeView.setText("Time spent in activity: " + timeInExercise + " minutes");
        totalCalories.setText("You burned: " + calculateCalories(calPerMinute, timeInExercise) + " Calories");


        /*----------------------DATE----------------------*/
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast.makeText(getApplicationContext(), currentDateTimeString, Toast.LENGTH_LONG).show();
        dateSubstring = currentDateTimeString.substring(0, 11);
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
                                ExerciseRecords setCurrentExercise = new ExerciseRecords(passedName, timeInExercise, dateSubstring, totalCaloriesDouble);

                                ExerciseInsert ei = new ExerciseInsert();
                                ei.execute(setCurrentExercise);

                                exerciseObjArray.add(setCurrentExercise);
                                messageAdapter.notifyDataSetChanged();//update the listview

                                Snackbar.make(v, "Saving on Database", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ExerciseRecords frag = new ExerciseRecords();
                                Snackbar.make(v, "You canceled the Action", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                        });

                secondBuilder.show();
            }
        });


    }//end onCreate

    protected class ExerciseQuery extends AsyncTask<Context,Integer,String>{

        @Override
        protected String doInBackground(Context...args){
            AppDBHelper dbHelper = new AppDBHelper(args[0]);
            exerciseObjArray.addAll(dbHelper.getAllExerciseRecords());
            Collections.reverse(exerciseObjArray);

            dbHelper.close();
            return "done";
        }

    }

    /**
     * Method used to return the total calories burned in exercise
     *
     * @param calPerMinute
     * @param timeInExercise
     * @return totalCalories
     */
    public double calculateCalories(double calPerMinute, double timeInExercise) {

        totalCaloriesDouble = calPerMinute * timeInExercise;
        return totalCaloriesDouble;
    }//end calculateCalories method

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }//end onCreateOptionsMenu

    private class ExerciseInsert extends AsyncTask<ExerciseRecords,Integer,String>{
        @Override
        protected String doInBackground(ExerciseRecords...args){
            dbHelper = new AppDBHelper(ctx);
            dbHelper.insertExerciseSession(args[0]);//update the database
            dbHelper.close();
            return "done";
        }

        protected void onPostExecute(String args){
            //notify user of constraints
            Context ct = getApplicationContext();
            CharSequence text = "Exercise added to the Database";
            int d2 = Toast.LENGTH_LONG;
            Toast t = Toast.makeText(ct,text,d2);
            t.show();
        }
    }//end ExerciseInsert


    private class ExerciseAdapter extends ArrayAdapter<ExerciseRecords> {
        public ExerciseAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return exerciseObjArray.size();
        }

        @Override
        public ExerciseRecords getItem(int position){
            return exerciseObjArray.get(position);
        }

        // get view, set items, return view
        public View getView(int position, View convertView, ViewGroup parent){

            ExerciseRecords tempExercise = getItem(position);

            LayoutInflater inflater = Exercise_info_class.this.getLayoutInflater();
            View resultView = inflater.inflate(R.layout.exercise_row_layout, null);

            TextView viewDate = (TextView)resultView.findViewById(R.id.exerciseRowDate);
            viewDate.setText(tempExercise.getDate());

            TextView viewName = (TextView) resultView.findViewById(R.id.exerciseRowName);
            viewName.setText(tempExercise.getExerciseName()); // get the string at position

            TextView viewDuration = (TextView) resultView.findViewById(R.id.exerciseRowDuration);
            String tempDuration = String.valueOf(tempExercise.getDuration());
            viewDuration.setText(tempDuration + " minutes"); // get the string at position

            TextView viewCalories = (TextView) resultView.findViewById(R.id.exerciseRowCalories);
            String tempCalories = String.valueOf(tempExercise.getCalories());
            viewCalories.setText(tempCalories + " calories"); // get the string at position

            return resultView;
        }
    }//end ExerciseAdapter



}//end Class
