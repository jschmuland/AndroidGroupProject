package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kathleen McNulty on 06/04/2017.
 */

public class MealActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_layout);

        MealPlannerFragment mpfrag = new MealPlannerFragment();

        FragmentManager fm=getFragmentManager();
        FragmentTransaction frag=fm.beginTransaction();
        frag.add(R.id.frame2, mpfrag);
        frag.commit();

    }}



