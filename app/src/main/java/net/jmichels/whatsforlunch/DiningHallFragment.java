package net.jmichels.whatsforlunch;

/**
 * Created by Jay on 12/28/2014.
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiningHallFragment extends Fragment {
    /**
     * The fragment argument representing the dining hall for this
     * fragment.
     */
    private static final String ARG_DINING_HALL = "dining_hall";

    private View mView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DiningHallFragment newInstance(DiningHall diningHall) {
        DiningHallFragment fragment = new DiningHallFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DINING_HALL, diningHall);
        fragment.setArguments(args);
        return fragment;
    }

    public DiningHallFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the last input date or use today
        Calendar rightNow = Calendar.getInstance();
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        final int year = settings.getInt("menuYear",rightNow.get(Calendar.YEAR));
        final int month = settings.getInt("menuMonth", rightNow.get(Calendar.MONTH));
        final int day = settings.getInt("menuDay", rightNow.get(Calendar.DAY_OF_MONTH));

        mView = inflater.inflate(R.layout.fragment_main, container, false);

        final Button dateButton = (Button) mView.findViewById(R.id.date_button);
        dateButton.setText((month+1) + "/"
                + day + "/" + year);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(mView.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // save the input date in shared prefs
                                SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putInt("menuYear", year);
                                editor.putInt("menuMonth", monthOfYear);
                                editor.putInt("menuDay", dayOfMonth);

                                // Commit the edits!
                                editor.commit();

                                dateButton.setText((monthOfYear+1) + "/"
                                        + dayOfMonth + "/" + year);

                                fetchMenu();
                            }
                        }, year, month, day);
                dpd.show();
            }
        });

        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);
        ((MainActivity) activity).onSectionAttached(diningHall);
    }

    public void fetchMenu() {
        DiningHall diningHall = (DiningHall)getArguments().getSerializable(ARG_DINING_HALL);

        // Get the last input date or use today
        Calendar rightNow = Calendar.getInstance();
        SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
        int month = settings.getInt("menuMonth", rightNow.get(Calendar.MONTH));
        int day = settings.getInt("menuDay", rightNow.get(Calendar.DAY_OF_MONTH));

        FetchMenuTask fetchMenu = new FetchMenuTask(getActivity(), this.getView());
        fetchMenu.execute(String.valueOf(month+1),String.valueOf(day),diningHall.getId().toString());

        TextView diningHallTextView = (TextView)mView.findViewById(R.id.diningHallTextView);
        diningHallTextView.setText("Loading menu data...");
    }
}