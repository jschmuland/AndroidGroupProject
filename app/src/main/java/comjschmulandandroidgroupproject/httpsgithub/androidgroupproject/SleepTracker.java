package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Sleep;

public class SleepTracker extends AppCompatActivity {
    private final String TAG = "SleepTracker";
    final ArrayList<Sleep> sleepObjArray = new ArrayList<>();
    private SQLiteDatabase db;
    private Cursor results;
    Context ctx;

    private int idColumn,dateColumn,durationColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;

        setContentView(R.layout.activity_template);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting Sleep array in Async Task
        SleepQuery sq = new SleepQuery();
        sq.execute(this);

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
                    //add sleep object to db in async task
                    SleepInsert si = new SleepInsert();
                    si.execute(setCurrentSleep);
                    //add sleep object to sleep array
                    sleepObjArray.add(0,setCurrentSleep);
                    messageAdapter.notifyDataSetChanged();//update the listview

                    sleepInputHndl.setText("");

                }catch (Exception e){
                    //notify user of constraints
                    Context ct = getApplicationContext();
                    CharSequence text = "Please enter a sleep number in sec only";
                    int d2 = Toast.LENGTH_LONG;
                    Toast t = Toast.makeText(ct,text,d2);
                    t.show();
                }
            }
        });

        //When timer is pressed
        ToggleButton sleepToggelBtn = (ToggleButton)findViewById(R.id.sleepToggleBtn);
        sleepToggelBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

    }// end onCreate

    protected class SleepQuery extends AsyncTask<Context,Integer,String>{

        @Override
        protected String doInBackground(Context...args){
            AppDBHelper dbHelper = new AppDBHelper(args[0]);
            sleepObjArray.addAll(dbHelper.getAllSleep());
            Collections.reverse(sleepObjArray);

            dbHelper.close();
            return "done";
        }

    }

    protected class SleepInsert extends AsyncTask<Sleep,Integer,String>{
        @Override
        protected String doInBackground(Sleep...args){
            AppDBHelper dbHelper = new AppDBHelper(ctx);
            dbHelper.insertSleepSession(args[0]);//update the database
            dbHelper.close();
            return "done";
        }
    }

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
            int tempduration = tempSleep.getDuration();
            Log.i(TAG, "getView: date = "+tempSleep.getDate() +" duration = "+tempduration);
            LayoutInflater inflater = SleepTracker.this.getLayoutInflater();
            View generatedView = inflater.inflate(R.layout.sleep_row_layout,null);
            //get and set date in listview
            TextView viewDate = (TextView)generatedView.findViewById(R.id.sleepDate);
            viewDate.setText(tempSleep.getDate().toString());
            //get and set duration in listview
            TextView viewDuration = (TextView) generatedView.findViewById(R.id.sleepTime);
            //for looks

            int hours = (int) tempduration/3600;
            int min = (tempduration%3600)/60;
            int sec = tempduration%60;

            if (hours>1){
                viewDuration.setText(hours + "h "+min+"m "+sec+"s");
            }else {
                viewDuration.setText(min+"m "+sec+"s");
            }
            // set checkmark if over 6 hours of sleep.
            if(tempduration/60>6){
                ImageView checkMark = (ImageView)generatedView.findViewById(R.id.sleep_target_made);
                checkMark.setVisibility(View.VISIBLE);
            }

            return generatedView;

        }
    }

}
