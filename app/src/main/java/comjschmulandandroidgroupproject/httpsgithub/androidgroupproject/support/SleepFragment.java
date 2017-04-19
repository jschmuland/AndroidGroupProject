package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.AppDBHelper;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.SleepTracker;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Sleep;


public class SleepFragment extends Fragment {
    private static final String TAG = "SleepFragment";
    Context parent;
    int durr,id,target;
    long dateLong;
    Date dateEnd, dateStart;
    Sleep sleepObj;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bun = getArguments();
        dateLong = bun.getLong("Date");
        dateEnd = new Date(dateLong);
        durr = bun.getInt("Duration");
        dateStart = new Date(dateLong - (durr*1000));
        id = bun.getInt("ID");
        target = bun.getInt("Target");

    }

    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        parent = ctx;
    }

    public void passData(Sleep sleep){
        Log.i(TAG, "passData: sleep object in frag "+sleep);
        sleepObj = sleep;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState){
        super.onCreateView(inflater,containter,savedInstanceState);

        SimpleDateFormat formatDate = new SimpleDateFormat("h:mm");
        SimpleDateFormat formatDay = new SimpleDateFormat("EEEE, MMM dd");
        final View gui = inflater.inflate(R.layout.sleep_detail_frag,null);

        TextView durText = (TextView) gui.findViewById(R.id.sleep_duration);
        durText.setText(((int)durr/3600)+"hr "+((durr%3600)/60)+"min");

        TextView startText = (TextView) gui.findViewById(R.id.start_time);
        startText.setText(formatDate.format(dateStart));

        TextView endText = (TextView) gui.findViewById(R.id.end_time);
        endText.setText("until " +formatDate.format(dateEnd));

        TextView dayText = (TextView) gui.findViewById(R.id.day_of_week);
        dayText.setText(formatDay.format(dateEnd));

        //update progress bar
        ProgressBar sleepProgress = (ProgressBar) gui.findViewById(R.id.sleep_progress_bar);
        sleepProgress.setProgress(100*durr/target);

        Button dltSleepBtn = (Button) gui.findViewById(R.id.dlt_sleep_btn);
        dltSleepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDBHelper dbHelper = new AppDBHelper(parent.getApplicationContext());
                Log.i(TAG, "onClick: id should be"+id);
                if(dbHelper.deleteSleepSession(sleepObj)){
                    ((SleepTracker)getActivity()).updateList(id);
                    //confirm delete
                    Toast toast = Toast.makeText(parent,"Sleep log deleted",Toast.LENGTH_LONG);
                    toast.show();
                    getFragmentManager().beginTransaction().remove(SleepFragment.this).commit();


                }else{
                    Toast toast = Toast.makeText(parent,"Sleep log not deleted",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return gui;
    }
}
