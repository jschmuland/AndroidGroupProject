package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Kathleen McNulty on 06/04/2017.
 */

public class MealActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_layout);
        Intent callingIntent = getIntent();
        int parentId = callingIntent.getIntExtra("parentID", -1);
        Log.d("MEALACTIVITY", "Intent Meal Activity parent ID " + parentId);
        MealPlannerFragment mpfrag = new MealPlannerFragment();
        Bundle args = new Bundle();
        args.putInt("parentID", parentId);
        mpfrag.setArguments(args);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction frag=fm.beginTransaction();
        frag.add(R.id.frame2, mpfrag);
        frag.commit();


    }
}



