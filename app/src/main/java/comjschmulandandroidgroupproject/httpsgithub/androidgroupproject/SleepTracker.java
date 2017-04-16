package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Sleep;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support.SleepFragment;

import static android.os.SystemClock.elapsedRealtime;

public class SleepTracker extends AppCompatActivity {
    private final String TAG = "SleepTracker";
    final ArrayList<Sleep> sleepObjArray = new ArrayList<>();
    private SQLiteDatabase db;
    private Cursor results;
    Context ctx;
    int tempSleepDuration;
    int sleepTarget = 7*60*60;

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


        //create a message adapter from inner class
        final SleepAdapter messageAdapter = new SleepAdapter(this);
        listView.setAdapter(messageAdapter);

        //list menu on item listner
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sleep sleepObj = messageAdapter.getItem(position);

                Bundle bun = new Bundle();
                bun.putLong("Date",sleepObj.getDate().getTime());
                bun.putInt("Duration",sleepObj.getDuration());
                bun.putInt("ID",sleepObj.getId());
                bun.putInt("Target",sleepTarget);

                SleepFragment frag = new SleepFragment();
                frag.setArguments(bun);
                getFragmentManager().beginTransaction()
                        .replace(R.id.sleepFragmentHolder, frag)
//                        .addToBackStack("ID")
                        .commit();
            }
        });

        //when set sleep button is clicked
        Button setSleepBtn = (Button) findViewById(R.id.sleep_set);
        setSleepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    final AlertDialog.Builder d = new AlertDialog.Builder(SleepTracker.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_num_picker, null);
                    d.setTitle("Set Sleep");
                    d.setMessage("How long did you sleep");
                    d.setView(dialogView);
                    final NumberPicker hourNumberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker_hr);
                    hourNumberPicker.setMaxValue(16);
                    hourNumberPicker.setMinValue(0);
                    hourNumberPicker.setWrapSelectorWheel(true);

                    final NumberPicker minNumberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker_min);
                    minNumberPicker.setMaxValue(60);
                    minNumberPicker.setMinValue(0);
                    minNumberPicker.setWrapSelectorWheel(true);

                    //if you want to consistently update
//                    hourNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                        @Override
//                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
//                            Log.d(TAG, "onValueChange: ");
//                        }
//                    });

                    d.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           int tempDurationHr = hourNumberPicker.getValue();
                            int tempDurationMin = minNumberPicker.getValue();
                            tempSleepDuration = ((tempDurationHr*60)+tempDurationMin)*60;

                            Date tempDate = new Date();

                            //make new sleep obj
                            Sleep setCurrentSleep = new Sleep(tempDate, tempSleepDuration);
                            //add sleep object to db in async task
                            SleepInsert si = new SleepInsert();
                            si.execute(setCurrentSleep);
                            //add sleep object to sleep array
                            sleepObjArray.add(0,setCurrentSleep);
                            messageAdapter.notifyDataSetChanged();//update the listview
                        }
                    });
                    d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertDialog = d.create();
                    alertDialog.show();


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
                Chronometer timer = (Chronometer) findViewById(R.id.chronometer);

                if(isChecked){
                    timer.setBase(elapsedRealtime());
                    timer.start();
                }
                if(!isChecked){
                    timer.stop();
                    Log.i(TAG, "onCheckedChanged: "+timer.getText());

                    long elapsedMillis = SystemClock.elapsedRealtime() - timer.getBase();
                    int tempDurr = (int)elapsedMillis/1000;

                    Sleep tempSleepObj = new Sleep(new Date(),tempDurr);
                    //add sleep object to db in async task
                    SleepInsert si = new SleepInsert();
                    si.execute(tempSleepObj);
                    //add sleep object to sleep array
                    sleepObjArray.add(0,tempSleepObj);
                    messageAdapter.notifyDataSetChanged();//update the listview

                }


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

    private class SleepInsert extends AsyncTask<Sleep,Integer,String>{
        @Override
        protected String doInBackground(Sleep...args){
            AppDBHelper dbHelper = new AppDBHelper(ctx);
            dbHelper.insertSleepSession(args[0]);//update the database
            dbHelper.close();
            return "done";
        }

        protected void onPostExecute(String args){
            //notify user of constraints
            Context ct = getApplicationContext();
            CharSequence text = "Sleep added to the Database";
            int d2 = Toast.LENGTH_LONG;
            Toast t = Toast.makeText(ct,text,d2);
            t.show();
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
