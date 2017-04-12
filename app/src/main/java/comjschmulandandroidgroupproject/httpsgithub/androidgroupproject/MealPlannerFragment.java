package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Meal;


public class MealPlannerFragment extends Fragment {


ArrayList<String> meal=new ArrayList<String>();

     EditText editTextmeal;
    ListView theListmeal;
    Button button1;




    MealPlannerFragment.MealActivityAdapter adapter1;

    public MealPlannerFragment() {
        // Required empty public constructor
    }
    public void onActivityCreated(Bundle savedTest){
        super.onActivityCreated(savedTest);
        adapter1 = new MealPlannerFragment.MealActivityAdapter(getActivity());
        button1= (Button)getActivity().findViewById(R.id.submitmealbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                meal.add(editTextmeal.getText().toString());
                adapter1.notifyDataSetChanged();

                editTextmeal.setText("");
            }
        });

        editTextmeal= (EditText)getActivity().findViewById(R.id.editTextMeal);
        theListmeal= (ListView)getActivity().findViewById(R.id.theListMeal);

        // button1= (Button)rootView.findViewById(R.id.submitmealbutton);
        theListmeal.setAdapter(adapter1);
        //controls what gets changed in list
        // adapter1.notifyDataSetChanged();
        theListmeal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), FoodPicker.class);
                startActivityForResult(intent, 5);
            }
        });
setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        return inflater.inflate(R.layout.meals_layout, container, false);


    }

@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

      super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.ft_toolbar, menu);
      // getActivity().getMenuInflater().inflate(R.menu.ft_toolbar, menu);
       MenuItem foodItem = (MenuItem) menu.findItem(R.id.action_mealplanner);
        foodItem.setVisible(false);


    }//end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()){
            case (R.id.action_exercise):
                intent = new Intent(getActivity(), Exercise.class);
                startActivity(intent);
                return true;
            case (R.id.action_mealplanner):
                intent = new Intent(getActivity(), FoodTracker.class);
                startActivity(intent);
                return true;
            case(R.id.action_sleep):
                intent = new Intent(getActivity(), SleepTracker.class);
                startActivity(intent);
                return true;
            case(R.id.action_home):
                getActivity().finish();
                return true;
            case(R.id.action_help):

                createHelpDialog();
                return true;
        }

        return false;

    }


    public void createHelpDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.meal_helplayout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder2.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog2 = builder2.create();
        dialog2.setTitle(R.string.mealplanner_title);
        dialog2.show();
    }
    private class MealActivityAdapter extends ArrayAdapter<String> {

        public MealActivityAdapter (Context ctx) {
            super(ctx, 0);
        }
        public	int getCount(){return meal.size();
        }
        public String getItem(int position){
            return meal.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            //Just specifying the chat window is going to use what layout for each item????
            LayoutInflater inflater =getActivity().getLayoutInflater();
            View result = null ;

            result = inflater.inflate(R.layout.food_picker_row, null);
            TextView mealtext= (TextView) result.findViewById(R.id.tp_foodName);
            mealtext.setText(getItem(position)); // get the string at position
            return result;
        }

}}
