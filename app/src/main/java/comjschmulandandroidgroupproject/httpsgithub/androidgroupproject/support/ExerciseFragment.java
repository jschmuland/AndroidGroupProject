package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;

/**
 * Created by carlo on 4/5/2017.
 */

public class ExerciseFragment extends Fragment {

    protected static final String ACTIVITY_NAME = "Exercise Fragment";
    Context ctx;
    protected String exerciseName, date, tempDuration, tempCalories, tempID;
    protected double exerciseDuration, calories;
    protected int keyID;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bun = getArguments();

        keyID = bun.getInt("_ID");
        exerciseName = bun.getString("NAME");
        date = bun.getString("DATE");
        exerciseDuration = bun.getDouble("DURATION");
        tempDuration = String.valueOf(exerciseDuration);
        calories = bun.getDouble("CALORIES");
        tempCalories = String.valueOf(calories);
        tempID = String.valueOf(keyID);
    }//end onCreate


    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        this.ctx = ctx;
    }//end onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState){
        super.onCreateView(inflater,containter,savedInstanceState);
        View gui = inflater.inflate(R.layout.exercise_message_fragment,null);

        TextView exerciseIdView = (TextView) gui.findViewById(R.id.exerciseRowIDFragment);
        exerciseIdView.setText("Exercise ID: " + tempID);

        TextView exerciseTextName = (TextView) gui.findViewById(R.id.exerciseRowNameFragment);
        exerciseTextName.setText("Exercise Name: " + exerciseName);

        TextView exerciseTextDate = (TextView) gui.findViewById(R.id.exerciseRowDateFragment);
        exerciseTextDate.setText("Exercise Date: " + date);

        TextView exerciseTextDuration = (TextView) gui.findViewById(R.id.exerciseRowDurationFragment);
        exerciseTextDuration.setText("Exercise Total Time: " + tempDuration);

        TextView exerciseTextCalories = (TextView) gui.findViewById(R.id.exerciseRowCaloriesFragment);
        exerciseTextCalories.setText("Exercise Calories Burned: " + tempCalories);


        return gui;
    }

}//end class
