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
 * Created by carlo on 4/5/2017. Fragment Class for the Exercise Activity
 */
public class ExerciseFragment extends Fragment {

    protected static final String ACTIVITY_NAME = "Exercise Fragment";
    Context ctx;
    protected String exerciseName, date, tempDuration, tempCalories, tempID, tempArrayId;
    protected double exerciseDuration, calories;
    protected int arrayID, dbKey;
    protected ExerciseRecords exerciseRecords;

    /**
     * onCreate method
     * @param savedInstanceState bundle object
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bun = getArguments();            //new bundle to get the arguments

        dbKey = bun.getInt("_ID");              //get the ID from the bundle
        tempID = String.valueOf(dbKey);         //set the ID as a String value

        arrayID = bun.getInt("arrayPosition");  //get the arrayPosition from the bundle
        tempArrayId = String.valueOf(arrayID);  //set the arrayID as a String value

        exerciseName = bun.getString("NAME");   //get the exerciseName from the bundle
        date = bun.getString("DATE");           //get the date from the bundle

        exerciseDuration = bun.getDouble("DURATION");       //get the exerciseDuration from the bundle
        tempDuration = String.valueOf(exerciseDuration);    //set the exerciseDuration as a String value

        calories = bun.getDouble("CALORIES");   //get the calories from the bundle
        tempCalories = String.valueOf(calories);    //set the calories as a String value

        exerciseRecords = new ExerciseRecords(); //new exerciseRecords object

        getActivity().setTitle(getString(R.string.exercise));   //set the activity title

    }//end onCreate

    /**
     *
     * @param ctx context object
     */
    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        this.ctx = ctx;
    }//end onAttach

    /**
     *  onCreateView to create a new view and inflate the message
     * @param inflater object
     * @param container object
     * @param savedInstanceState object
     * @return gui
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);

        //create a new View and inflate the message fragment
        View gui = inflater.inflate(R.layout.exercise_message_fragment,null);

        //create the ExerciseIDView TextView and set a value
        TextView exerciseIdView = (TextView) gui.findViewById(R.id.exerciseRowIDFragment);
        exerciseIdView.setText(getString(R.string.exerciseID) + ": " + tempArrayId);

        //create the ExerciseTextName TextView and set a value
        TextView exerciseTextName = (TextView) gui.findViewById(R.id.exerciseRowNameFragment);
        exerciseTextName.setText(getString(R.string.exerciseName) + ": " + exerciseName);

        //create the ExerciseTextDate TextView and set a value
        TextView exerciseTextDate = (TextView) gui.findViewById(R.id.exerciseRowDateFragment);
        exerciseTextDate.setText(getString(R.string.exerciseDate) + ": " + date);

        //create the ExerciseTextDuration TextView and set a value
        TextView exerciseTextDuration = (TextView) gui.findViewById(R.id.exerciseRowDurationFragment);
        exerciseTextDuration.setText(getString(R.string.exerciseDuration) + ": " + tempDuration + " " + getString(R.string.exerciseMinutes));

        //create the ExerciseTextCalories TextView and set a value
        TextView exerciseTextCalories = (TextView) gui.findViewById(R.id.exerciseRowCaloriesFragment);
        exerciseTextCalories.setText(getString(R.string.exerciseBurn) + ": " + tempCalories + " " + getString(R.string.calories));

        //create a new Button and set the code to delete the message from the DataBase
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
