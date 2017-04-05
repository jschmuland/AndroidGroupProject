package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Sleep;

/**
 * Created by user on 4/4/2017.
 */

public class SleepFragment extends Fragment {
    private static final String TAG = "SleepFragment";
    Context parent;
    int durr,id,target;
    Date date;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bun = getArguments();
        date = new Date(bun.getLong("Date"));
        durr = bun.getInt("Duration");
        id = bun.getInt("ID");
        target = bun.getInt("Target");
    }

    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        parent = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState){
        super.onCreateView(inflater,containter,savedInstanceState);
        View gui = inflater.inflate(R.layout.sleep_detail_frag,null);

        TextView durText = (TextView) gui.findViewById(R.id.sleep_duration);
        durText.setText(""+durr);

//        date.

        TextView startText = (TextView) gui.findViewById(R.id.start_time);
        durText.setText(""+durr);

        ProgressBar sleepProgress = (ProgressBar) gui.findViewById(R.id.sleep_progress_bar);

        sleepProgress.setProgress(100*durr/target);

        return gui;
    }
}
