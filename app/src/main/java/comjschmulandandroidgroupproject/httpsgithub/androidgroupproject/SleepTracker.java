package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Sleep;

public class SleepTracker extends AppCompatActivity {
    private final String TAG = "SleepTracker";
    final ArrayList<Sleep> sleepObjArray = new ArrayList<>();
    protected AppDBHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor results;
    private int idColumn,dateColumn,durationColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting writable database
            dbHelper = new AppDBHelper(this);
        db = dbHelper.getWritableDatabase();
        //query database
        results = db.query(false,AppDBHelper.SLEEP_TABLE,
                new String[]{AppDBHelper.KEY_ID, AppDBHelper.DATE, AppDBHelper.HOURS_SLEPT},
                null,null,null,null,null,null);
        //number of rows
        int rows = results.getCount();
        idColumn = results.getColumnIndex(AppDBHelper.KEY_ID);
        dateColumn = results.getColumnIndex(AppDBHelper.DATE);
        durationColumn = results.getColumnIndex(AppDBHelper.HOURS_SLEPT);

        results.moveToFirst();
        //put results into the array list
        while(!results.isAfterLast()){
            Date tempDate = new Date(results.getLong(dateColumn));
            sleepObjArray.add(new Sleep(results.getInt(idColumn),tempDate,results.getInt(durationColumn)));
            results.moveToNext();
        }

        Log.i(TAG, "onCreate:  sleepObjArray " +sleepObjArray.toString());



        //getting listview
        ListView listView = (ListView) findViewById(R.id.sleepListView);

        //getting edit text object
        final EditText sleepInputHndl = (EditText) findViewById(R.id.sleepInput);

        //create a message adapter from inner class
        final SleepAdapter messageAdapter = new SleepAdapter(this);
        listView.setAdapter(messageAdapter);

        //when set sleep button is clicked
        Button setSleepBtn = (Button) findViewById(R.id.sleep_set);
        setSleepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //read sleep input and get new date time
                    int tempSleepDuration = Integer.parseInt(sleepInputHndl.getText().toString());
                    Date tempDate = new Date();

                    Log.i(TAG, "onClick: current time = "+tempDate);
                    //make new sleep obj
                    Sleep setCurrentSleep = new Sleep(tempDate, tempSleepDuration);
                    sleepObjArray.add(setCurrentSleep);
                    messageAdapter.notifyDataSetChanged();

                    sleepInputHndl.setText("");

                }catch (Exception e){
                    Context ct = getApplicationContext();
                    CharSequence text = "Please enter a number only";
                    int d2 = Toast.LENGTH_LONG;
                    Toast t = Toast.makeText(ct,text,d2);
                    t.show();
                }
            }
        });

    }// end onCreate


    /*inner class for array adapter*/
    private class SleepAdapter extends ArrayAdapter<Sleep>{
        private SleepAdapter(Context context){
            super(context, 0);
        }
        @Override
        public int getCount(){
            return sleepObjArray.size();
        }

        @Override
        public Sleep getItem(int position){
            return sleepObjArray.get(position);
        }

        // get view, set items, return view
        public View getView(int position, View convertView, ViewGroup parent){
            Sleep tempSleep = getItem(position);
            Log.i(TAG, "getView: date = "+tempSleep.getDate() +" duration = "+tempSleep.getDuration());
            LayoutInflater inflater = SleepTracker.this.getLayoutInflater();
            View generatedView = inflater.inflate(R.layout.row_layout_sleep,null);
            //get and set date in listview
            TextView viewDate = (TextView)generatedView.findViewById(R.id.sleepDate);
            viewDate.setText(tempSleep.getDate().toString());
            //get and set duration in listview
            TextView viewDuration = (TextView) generatedView.findViewById(R.id.sleepTime);
            viewDuration.setText(""+tempSleep.getDuration());
            // set checkmark if over 6 hours of sleep.
            if(tempSleep.getDuration()/60>6){
                ImageView checkMark = (ImageView)generatedView.findViewById(R.id.sleep_target_made);
                checkMark.setVisibility(View.VISIBLE);
            }

            return generatedView;

        }
    }

}
