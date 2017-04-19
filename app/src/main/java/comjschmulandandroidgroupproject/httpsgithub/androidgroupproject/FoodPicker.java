package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import android.widget.Toast;

import java.util.ArrayList;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Food;

/**
 * FoodPicker is the activity that contains the FoodPicker Fragment, it uses an AsyncTask
 * to query the Livestrong API for food data
 */

public class FoodPicker extends AppCompatActivity {
    //activity name
    private final static String ACTIVITY_NAME = "FoodPicker";

    //onCreate sets up the UI, uses fragment manager to load the fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_picker);

        //toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        //fragment and fragment manager and transaction
        FoodPickerFragment fragment = new FoodPickerFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.food_picker_frame_layout, fragment);
        ft.commit();

    }

    //toolbar menu handling
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()){
            //exercise
            case (R.id.action_exercise):
                intent = new Intent(this, Exercise.class);
                startActivity(intent);
                return true;
            //meal planner
            case (R.id.action_mealplanner):
                intent = new Intent(this, MealPlanner.class);
                startActivity(intent);
                return true;
            //sleep tracker
            case(R.id.action_sleep):
                intent = new Intent(this, SleepTracker.class);
                startActivity(intent);
                return true;
            //food tracker
            case (R.id.action_foodtracker):
                intent = new Intent(this, FoodTracker.class);
                startActivity(intent);
                return true;
            //home
            case(R.id.action_home):
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            //help
            case(R.id.action_help):
                Log.i(ACTIVITY_NAME, "help");
                createHelpDialog();
                return true;
        }

        return false;

    }

    //inflates the toolbar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.ft_toolbar, menu);
        return true;
    }//end onCreateOptionsMenu

    //creates the help dialog for food picker
    public void createHelpDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.food_tracker_help_layout, null);
        TextView helpText = (TextView) dialogView.findViewById(R.id.ftHelp);
        helpText.setText(R.string.foodpicker_help_text);
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
        dialog2.setTitle(R.string.foodpicker_help_title);
        dialog2.show();
    }

    //FoodPicker Fragment
    public static class FoodPickerFragment extends Fragment {
        //food list
        private static ArrayList<Food> foods;
        //button for searching api
        private Button searchFoodBtn;
        //text field for searching
        private EditText searchFoodField;
        //foodpicker adapter for listview
        private static FoodPickerAdapter adapter;

        //constructor
        public FoodPickerFragment(){
            super();
            foods = new ArrayList<>();
        }

        //creates the view for the fragment
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.food_picker_layout, container, false);

            //setting up the loading text
            final TextView text = (TextView) view.findViewById(R.id.loadingAPIText);
            text.setVisibility(View.INVISIBLE);

            //setting up the progressbar
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.APIProgessBar);
            progressBar.setVisibility(View.INVISIBLE);

            //setting up the search text field
            searchFoodField = (EditText) view.findViewById(R.id.searchFood);

            //setting up the search food button and the action
            searchFoodBtn = (Button) view.findViewById(R.id.searchFoodBtn);
            searchFoodBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String search = searchFoodField.getText().toString();
                    //validating the input
                    if(search.trim().equals("")){
                        //showing the toast
                        Toast toast = Toast.makeText(getActivity(), R.string.enter_food, Toast.LENGTH_SHORT); //this is the ListActivity
                        toast.show();
                    } else {
                        //make the loading elements visible
                        progressBar.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                        GetFoodFromAPI async = new GetFoodFromAPI(view, search);
                        async.execute();
                    }
                }
            });

            //setting up the list view and adapter
            ListView listView = (ListView) view.findViewById(R.id.list);
            adapter = new FoodPickerAdapter(getActivity());
            listView.setAdapter(adapter);
            listView.setVisibility(View.INVISIBLE);

            //setting up the listview item click listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Food food = adapter.getItem(position);
                    confirmationDialog(food);
                }
            });

            adapter.notifyDataSetChanged();

            return view;
        }

        //creates the confirmation dialog for food
        private void confirmationDialog(Food food){
            final Food f = food;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.confirmation);

            builder.setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    //passing values back to food tracker
                    Intent intent = new Intent(getActivity(), FoodTracker.class);
                    intent.putExtra("FOOD", f.getFoodName());
                    intent.putExtra("CALORIES", f.getCalories());
                    intent.putExtra("DESCRIPTION", f.getDescription());
                    getActivity().setResult(5, intent);
                    getActivity().finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //Async task for getting API data
        private static class GetFoodFromAPI extends AsyncTask {
            private View view;
            //query string
            private String query = "";
            //food list
            private ArrayList<Food> foodList;

            //constructor
            public GetFoodFromAPI(View view, String query){
                this.view = view;
                this.query = query;
            }

            //does the database call
            @Override
            protected Object doInBackground(Object[] params) {
                Log.i(ACTIVITY_NAME, "fetching food from API");

                AppAPIHelper api = new AppAPIHelper();

                foodList = api.searchFood(api.openConnection(api.makeQuery(query)));

                return null;
            }

            //makes the ui visible
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                //component visibility
                foods = foodList;
                adapter.notifyDataSetChanged();

                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.APIProgessBar);
                TextView textView = (TextView) view.findViewById(R.id.loadingAPIText);
                ListView listView = (ListView) view.findViewById(R.id.list);

                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);

            }
        }

        //food picker list view adapter
        private class FoodPickerAdapter extends ArrayAdapter<Food> {

            public FoodPickerAdapter(Context ctx) {
                super(ctx, 0);
            }

            //get count
            @Override
            public int getCount() {
                return foods.size();
            }

            //inflates the list view
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View result = inflater.inflate(R.layout.food_picker_row, null);

                //set up the list view text
                TextView food = (TextView) result.findViewById(R.id.tp_foodName);
                TextView cal = (TextView) result.findViewById(R.id.tp_foodCal);

                //get data
                Food data = getItem(position);

                // set the text views to the data
                food.setText(data.getFoodName());
                cal.setText(String.valueOf(data.getCalories()));

                return result;
            }

            //get Food item
            @Nullable
            @Override
            public Food getItem(int position) {
                return foods.get(position);
            }

            //get Food Id
            public long getItemId(int position) {
                return foods.get(position).getId();
            }
        }

    }

}
