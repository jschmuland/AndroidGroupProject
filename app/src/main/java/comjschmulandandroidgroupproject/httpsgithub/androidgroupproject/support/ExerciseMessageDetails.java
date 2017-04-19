package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;

/**
 * Created by carlo on 4/5/2017. Message Details Class for the Exercise Activity
 */
public class ExerciseMessageDetails extends AppCompatActivity {

    /**
     * onCreate method
     * @param savedInstanceState bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_fragment_holder);

        //new ExerciseFragment Object for the MessageDetails
        ExerciseFragment frag = new ExerciseFragment();
        frag.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().replace(R.id.exerciseFrameHolder, frag).commit();
    }
}
