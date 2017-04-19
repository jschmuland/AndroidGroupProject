package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;

/**
 * Created by carlo on 4/6/2017.
 */

public class ExerciseMessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_fragment_holder);

        ExerciseFragment frag = new ExerciseFragment();

        frag.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().replace(R.id.exerciseFrameHolder, frag).commit();
    }
}
