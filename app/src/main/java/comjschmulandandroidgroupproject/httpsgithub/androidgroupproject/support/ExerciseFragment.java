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
    protected String exerciseName, date, tempDuration, tempCalories;
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
    }//end onCreate


    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        this.ctx = ctx;
    }//end onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState){
        super.onCreateView(inflater,containter,savedInstanceState);
        View gui = inflater.inflate(R.layout.exercise_row_layout,null);

        TextView exerciseTextName = (TextView) gui.findViewById(R.id.exerciseRowName);
        exerciseTextName.setText(exerciseName);

        TextView exerciseTextDate = (TextView) gui.findViewById(R.id.exerciseRowDate);
        exerciseTextDate.setText(date);

        TextView exerciseTextDuration = (TextView) gui.findViewById(R.id.exerciseRowDuration);
        exerciseTextDuration.setText(tempDuration);

        TextView exerciseTextCalories = (TextView) gui.findViewById(R.id.exerciseRowCalories);
        exerciseTextCalories.setText(tempCalories);


        return gui;
    }

}//end class
