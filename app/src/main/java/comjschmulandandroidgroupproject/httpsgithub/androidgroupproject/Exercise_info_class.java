package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Exercise_info_class extends AppCompatActivity {
    private TextView exerciseInfoView;
    protected TextView exerciseNameView;

    protected String passedName;
    protected String passedInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info_class);

        Exercise exercise = new Exercise();

        passedName = getIntent().getStringExtra(exercise.getExerciseName());

        passedInfo = getIntent().getStringExtra(exercise.getExerciseInfo());

        exerciseInfoView = (TextView) findViewById(R.id.textViewExerciseInfo);

       exerciseNameView = (TextView) findViewById(R.id.textViewExerciseName);

        exerciseNameView.setText(passedName);

        exerciseInfoView.setText(passedInfo);

    }
}
