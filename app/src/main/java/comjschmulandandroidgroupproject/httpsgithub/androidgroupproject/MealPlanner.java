


package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.FoodEaten;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.MealPlan;

import static comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R.id.toolbar;


public class MealPlanner extends AppCompatActivity {

    ArrayList<MealPlan> mealplans;
    AppDBHelper helper;
    MealPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planner);
        helper = new AppDBHelper(this);
        ListView theList = (ListView) findViewById(R.id.theList);
        Button button = (Button) findViewById(R.id.mealsubmitbutton);
        Button exit = (Button) findViewById(R.id.exitbutton);
        final EditText editText = (EditText) findViewById(R.id.editText);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mealplans = new ArrayList<MealPlan>();
        theList = (ListView) findViewById(R.id.theList);
        adapter = new MealPlanAdapter(this);
        theList.setAdapter(adapter);

        //controls what gets changed in list
        MealPlanQuery queryThread = new MealPlanQuery();
        try {
            mealplans = queryThread.execute().get();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }


        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MealPlanner.this, MealActivity.class);
                MealPlan mp = adapter.getItem(position);
                intent.putExtra("parentID", mp.getId());
                Log.d("MEALPLANNER", "position: " + position + " id: " + id + " parent ID: " + mp.getId());
                startActivityForResult(intent, 5);
            }
        });

        theList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MealPlanner.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.Dialogue2) //Add a dialog message to strings.xml

                        .setTitle(R.string.Dialogue_Title2)
                        .setPositiveButton(R.string.Dialogue_Positive2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                deleteMealPlan(adapter.getItem(pos), pos);

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealPlan mealPP = new MealPlan(editText.getText().toString());
                long returnedID = helper.insertMealPlan(mealPP);
                //if the long returned is greater or equals to zero
                if (returnedID >= 0) {
                    Log.d("MEALPLANINSERT", "ID inserted : " + returnedID);
                    //set id of inserted mealPlan object to the one in the db
                    mealPP.setId((int) returnedID);
                    //add meal to the arraylist of mealplans
                    mealplans.add(mealPP);
                    //notify adapter of the data set change
                    adapter.notifyDataSetChanged();
                }
                editText.setText("");
            }
        });
        //My Toast

        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.EnterName, Toast.LENGTH_SHORT).show();
            }
        });
        //Snackbar

        Snackbar.make(findViewById(android.R.id.content), R.string.MakeMeal, Snackbar.LENGTH_LONG)
                .show();

        //Custom Dialogue

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);


                AlertDialog.Builder builder = new AlertDialog.Builder(MealPlanner.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.Dialogue) //Add a dialog message to strings.xml

                        .setTitle(R.string.Dialogue_Title)
                        .setPositiveButton(R.string.Dialogue_Positive, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("Response", "My information to share");
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();//you don't have to set it up to pass it between activities because it finishes and goes back to the previous activity

                            }
                        })
                        .setNegativeButton(R.string.Dialogue_Negative, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();
            }

        });


    }

    public void deleteMealPlan(MealPlan mealplan2, int position) {
        if (helper.deleteMealPlan(mealplan2)) {
            mealplans.remove(position);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ft_toolbar, menu);
        MenuItem foodItem = (MenuItem) menu.findItem(R.id.action_mealplanner);
        foodItem.setVisible(false);
        return true;
    }//end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case (R.id.action_exercise):
                intent = new Intent(MealPlanner.this, Exercise.class);
                startActivity(intent);
                return true;
            case (R.id.action_mealplanner):
                intent = new Intent(MealPlanner.this, FoodTracker.class);
                startActivity(intent);
                return true;
            case (R.id.action_sleep):
                intent = new Intent(MealPlanner.this, SleepTracker.class);
                startActivity(intent);
                return true;
            case (R.id.action_home):
                finish();
                return true;
            case (R.id.action_help):

                createHelpDialog();
                return true;
            default:
                return false;
        }

    }


    public void createHelpDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.mealplanner_helplayout, null);
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

    private class MealPlanQuery extends AsyncTask<String, Integer, ArrayList<MealPlan>> {

        ProgressBar progressBar;

        public MealPlanQuery() {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setMax(100);
        }

        @Override
        protected ArrayList<MealPlan> doInBackground(String... args) {
            publishProgress(1);
            ArrayList<MealPlan> results = helper.getAllMealPlans();
            publishProgress(100);
            return results;
        }

        @Override
        protected void onProgressUpdate(Integer... prog) {
            progressBar.setProgress(prog[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPostExecute(ArrayList<MealPlan> res) {
            super.onPostExecute(res);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class MealPlanAdapter extends ArrayAdapter<MealPlan> {

        public MealPlanAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return mealplans.size();
        }

        public MealPlan getItem(int position) {
            return mealplans.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            //Just specifying the chat window is going to use what layout for each item????
            LayoutInflater inflater = MealPlanner.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.food_picker_row, null);
            TextView fooditem = (TextView) result.findViewById(R.id.tp_foodName);
            fooditem.setText(getItem(position).getPlanName()); // get the string at position
            return result;
        }
    }

}









