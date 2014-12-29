package net.jmichels.whatsforlunch;

/**
 * Created by Jay on 12/28/2014.
 */

import android.app.Activity;
import android.app.DatePickerDialog;
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

    private int mYear;
    private int mMonth;
    private int mDay;

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
        mView = inflater.inflate(R.layout.fragment_main, container, false);

        Calendar rightNow = Calendar.getInstance();
        mYear = rightNow.get(Calendar.YEAR);
        mMonth = rightNow.get(Calendar.MONTH);
        mDay = rightNow.get(Calendar.DAY_OF_MONTH);

        final Button dateButton = (Button) mView.findViewById(R.id.date_button);
        dateButton.setText((mMonth+1) + "/"
                + mDay + "/" + mYear);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(mView.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateButton.setText((monthOfYear+1) + "/"
                                        + dayOfMonth + "/" + year);
                                mDay = dayOfMonth;
                                mMonth = monthOfYear;
                                mYear = year;
                                fetchMenu();
                            }
                        }, mYear, mMonth, mDay);
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
        String day = String.valueOf(mDay);
        String month = String.valueOf(mMonth+1);

        FetchMenuTask fetchMenu = new FetchMenuTask(getActivity(), this.getView());
        fetchMenu.execute(month,day,diningHall.getId().toString());

        TextView diningHallTextView = (TextView)mView.findViewById(R.id.diningHallTextView);
        diningHallTextView.setText("Loading menu data...");
    }
}