package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.ExerciseRecords;

/**
 * Created by carlo on 4/5/2017.
 */

public class ExerciseFragment extends Fragment {

    protected static final String ACTIVITY_NAME = "Exercise Fragment";
    Context ctx;
    protected String exerciseName, date, tempDuration, tempCalories, tempID, tempArrayId;
    protected double exerciseDuration, calories;
    protected int arrayID, dbKey;
    protected ExerciseRecords exerciseRecords;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bun = getArguments();

        dbKey = bun.getInt("_ID");
        tempID = String.valueOf(dbKey);

        arrayID = bun.getInt("arrayPosition");
        tempArrayId = String.valueOf(arrayID);

        exerciseName = bun.getString("NAME");
        date = bun.getString("DATE");
        exerciseDuration = bun.getDouble("DURATION");
        tempDuration = String.valueOf(exerciseDuration);
        calories = bun.getDouble("CALORIES");
        tempCalories = String.valueOf(calories);

        exerciseRecords = new ExerciseRecords();


    }//end onCreate


    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        this.ctx = ctx;
    }//end onAttach

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View gui = inflater.inflate(R.layout.exercise_message_fragment,null);

        TextView exerciseIdView = (TextView) gui.findViewById(R.id.exerciseRowIDFragment);
        exerciseIdView.setText("Exercise ID: " + tempArrayId);

        TextView exerciseTextName = (TextView) gui.findViewById(R.id.exerciseRowNameFragment);
        exerciseTextName.setText("Exercise Name: " + exerciseName);

        TextView exerciseTextDate = (TextView) gui.findViewById(R.id.exerciseRowDateFragment);
        exerciseTextDate.setText("Exercise Date: " + date);

        TextView exerciseTextDuration = (TextView) gui.findViewById(R.id.exerciseRowDurationFragment);
        exerciseTextDuration.setText("Exercise Total Time: " + tempDuration);

        TextView exerciseTextCalories = (TextView) gui.findViewById(R.id.exerciseRowCaloriesFragment);
        exerciseTextCalories.setText("Exercise Calories Burned: " + tempCalories);

        Button buttonDelete = (Button) gui.findViewById(R.id.exerciseDeleteButton);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity().getClass() == Exercise_info_class.class) {

                    ((Exercise_info_class)getActivity()).deleteExerciseFromDb(arrayID, dbKey);
                    getActivity().getFragmentManager().beginTransaction().remove(ExerciseFragment.this).commit();
                } else {
                    Intent i = new Intent(getActivity(), Exercise_info_class.class);
                    i.putExtra("_ID", dbKey);
                    i.putExtra("arrayPosition", arrayID);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    getActivity().finish();
                }

            }
        });


        return gui;
    }

}//end class
