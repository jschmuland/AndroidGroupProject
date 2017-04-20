package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.MealPlan;
//Author Kathleen McNulty
//Fragment to display meals
public class MealPlannerFragment extends Fragment {
//Creating objects
    int parentID;
    ArrayList<Meal> meals = new ArrayList<Meal>();
    EditText editTextmeal;
    ListView theListmeal;
    Button button1;
    MealActivityAdapter adapter;
    AppDBHelper helper;

    public MealPlannerFragment() {
        parentID = -1;//set it to a default that wouldn't affect db.insert later on
    }
   //Submitting to database and instantiating async task
    public void onActivityCreated(Bundle savedTest) {
        super.onActivityCreated(savedTest);
        //Creating objects
        helper = new AppDBHelper(getActivity());
        adapter = new MealPlannerFragment.MealActivityAdapter(getActivity());
        button1 = (Button) getActivity().findViewById(R.id.submitmealbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meal myNewMeal = new Meal(editTextmeal.getText().toString());
                //makes sure no orphan children in the database
                if (parentID > -1) {
                    long returnedID = helper.insertMeal(myNewMeal, parentID);
                    if (returnedID >= 0) {
                        myNewMeal.setId((int) returnedID);
                        meals.add(myNewMeal);
                        adapter.notifyDataSetChanged();
                    }

                }
                editTextmeal.setText("");
            }
        });

        //Queries for all mealplans from specified mealPlanID
        MealQuery queryThread = new MealQuery(parentID);
        try {
            meals = queryThread.execute().get();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }


        editTextmeal = (EditText) getActivity().findViewById(R.id.editTextMeal);
        theListmeal = (ListView) getActivity().findViewById(R.id.theListMeal);
        theListmeal.setAdapter(adapter);
        
        
         //To go to the food picker
        theListmeal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FoodPicker.class);
                Meal meal = adapter.getItem(position);
                intent.putExtra("parentID", meal.getId());
                Log.d("MEAL", "position: " + position + " id: " + id + " parent ID: " + meal.getId());
                startActivityForResult(intent, 5);
            }
        });
        setHasOptionsMenu(true);
//To delete meal
        theListmeal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.Dialogue2) //Add a dialog message to strings.xml

                        .setTitle(R.string.Dialogue_Title2)
                        .setPositiveButton(R.string.Dialogue_Positive2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                              //deleteMeal(adapter.getItem(pos), pos);

                            }
                        })
                        .setNegativeButton(R.string.Dialogue_Negative2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();


                return true;
            }
        });
    }

//To create the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            parentID = savedInstanceState.getInt("parentID");
            Log.d("MEALPLANNERFRAGMENT", " i got the parentID: " + parentID);
        }


        return inflater.inflate(R.layout.meals_layout, container, false);
    }
 //Method to create options in menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ft_toolbar, menu);
        // getActivity().getMenuInflater().inflate(R.menu.ft_toolbar, menu);
        MenuItem foodItem = (MenuItem) menu.findItem(R.id.action_mealplanner);
        foodItem.setVisible(false);


    }//end onCreateOptionsMenu
   //Options in help menu to go to each of the other activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case (R.id.action_exercise):
                intent = new Intent(getActivity(), Exercise.class);
                startActivity(intent);
                return true;
            case (R.id.action_mealplanner):
                intent = new Intent(getActivity(), FoodTracker.class);
                startActivity(intent);
                return true;
            case (R.id.action_sleep):
                intent = new Intent(getActivity(), SleepTracker.class);
                startActivity(intent);
                return true;
            case (R.id.action_home):
                getActivity().finish();
                return true;
            case (R.id.action_help):

                createHelpDialog();
                return true;
        }

        return false;

    }

//Creates dialogue in help menu
    public void createHelpDialog() {
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
    //Inflates listview and connects to array
    private class MealActivityAdapter extends ArrayAdapter<Meal> {

        public MealActivityAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return meals.size();
        }

        public Meal getItem(int position) {
            return meals.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            //Just specifying the chat window is going to use what layout for each item????
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = null;

            result = inflater.inflate(R.layout.food_picker_row, null);
            TextView mealtext = (TextView) result.findViewById(R.id.tp_foodName);
            mealtext.setText(getItem(position).getMealName()); // get the string at position
            return result;
        }

    }
  //AsyncTask to retrieve all meals from database
    private class MealQuery extends AsyncTask<String, Integer, ArrayList<Meal>> {

        int parentID;

        public MealQuery(int parentID) {
            this.parentID = parentID;
        }

        @Override
        protected ArrayList<Meal> doInBackground(String... args) {
            publishProgress(1);
            ArrayList<Meal> results = helper.getAllMeals(parentID);
            publishProgress(100);
            return results;
        }

        @Override
        public void onPostExecute(ArrayList<Meal> res) {
            super.onPostExecute(res);
        }
    }


}
