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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Food;

public class FoodPicker extends AppCompatActivity {
    private final static String ACTIVITY_NAME = "FoodPicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_picker);

        Bundle extras = getIntent().getExtras();

        FoodPickerFragment fragment = new FoodPickerFragment();
        fragment.setArguments(extras);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.food_picker_frame_layout, fragment);
        ft.commit();

    }


    public static class FoodPickerFragment extends Fragment {
        private ArrayList<Food> foods;

        public FoodPickerFragment(){
            super();
            foods = new ArrayList<>();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.food_picker_layout, container, false);

            Food food = new Food("apple", 200);
            foods.add(food);

            ListView listView = (ListView) view.findViewById(R.id.list);
            final FoodPickerAdapter adapter = new FoodPickerAdapter(getActivity());
            listView.setAdapter(adapter);
            listView.setVisibility(View.INVISIBLE);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Food food = adapter.getItem(position);
                    confirmationDialog(food);
                }
            });

            adapter.notifyDataSetChanged();

            GetFoodFromAPI async = new GetFoodFromAPI(view);
            async.execute();

            return view;
        }

        private void confirmationDialog(Food food){
            final Food f = food;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.confirmation);

            builder.setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Intent intent = new Intent(getActivity(), FoodTracker.class);
                    intent.putExtra("FOOD", f.getFoodName());
                    intent.putExtra("CALORIES", f.getCalories());
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

        private static class GetFoodFromAPI extends AsyncTask {
            private View view;

            public GetFoodFromAPI(View view){
                this.view = view;
            }

            @Override
            protected Object doInBackground(Object[] params) {
                Log.i(ACTIVITY_NAME, "fetching food from API");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                //component visibility
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.APIProgessBar);
                TextView textView = (TextView) view.findViewById(R.id.loadingAPIText);
                ListView listView = (ListView) view.findViewById(R.id.list);

                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);

            }
        }

        private class FoodPickerAdapter extends ArrayAdapter<Food> {

            public FoodPickerAdapter(Context ctx) {
                super(ctx, 0);
            }

            @Override
            public int getCount() {
                return foods.size();
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View result = inflater.inflate(R.layout.food_picker_row, null);

                TextView food = (TextView) result.findViewById(R.id.tp_foodName);
                TextView cal = (TextView) result.findViewById(R.id.tp_foodCal);

                Food data = getItem(position);

                food.setText(data.getFoodName());
                cal.setText(String.valueOf(data.getCalories()));

                return result;
            }

            @Nullable
            @Override
            public Food getItem(int position) {
                return foods.get(position);
            }

            public long getItemId(int position) {
                return foods.get(position).getId();
            }
        }

    }

}
