package limousine;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thinh.limousine.MainActivity;
import com.example.thinh.limousine.R;

import java.util.ArrayList;
import java.util.List;

import limousine.Model.Office;
import limousine.Model.Route;
import limousine.Model.TripByDate;
import limousine.adapter.OfficalSpinnerAdapter;
import limousine.adapter.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thinh on 23/08/2017.
 */

public class RouteListFragment extends Fragment implements View.OnClickListener, Callback<List<Office>> {
    private Spinner mOfficeSpinner;
    private Button mDateButton;
    private RecyclerView mRouteExpandList;
    private ProgressBar mProgressList;
    //    private ArrayList<TripByDate> mArrTrip; // danh sach cac chuyen
    private ArrayList<Office> mArrOffices;
    private ArrayList<Route> mArrRoute;
    private MainActivity mMainActivity;
    private ListAdapter mRouteAdapter;
    private String mDateTrip; // thinhav: thoi gian de query cac chuyen di

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.route_list_fragment, container, false);
        mArrRoute = new ArrayList<Route>();
        // mArrTrip = new ArrayList<TripByDate>();
        mArrOffices = new ArrayList<>();
        mOfficeSpinner = (Spinner) v.findViewById(R.id.spinner_office);
        mDateButton = (Button) v.findViewById(R.id.date);
        mProgressList = (ProgressBar) v.findViewById(R.id.progress_bar);
        mRouteExpandList = (RecyclerView) v.findViewById(R.id.route_list);
        mRouteExpandList.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRouteExpandList.setLayoutManager(layoutManager);

        mOfficeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mProgressList.setVisibility(View.VISIBLE);
                queryRoute(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mDateButton.setOnClickListener(this);
        loadOffice();
        return v;
    }

    // thinhav: query danh sach cac chuyen di trong 1 tuyen
    private ArrayList<TripByDate> queryTripByDate(final Route route, String dateTrip) {
        int idRoute = route.getId();
        final ArrayList<TripByDate> arr = new ArrayList<TripByDate>();
        ApiBuilder.getService().getTripByDate(idRoute, "2017-08-06").enqueue(new Callback<List<TripByDate>>() {
            @Override
            public void onResponse(Call<List<TripByDate>> call, Response<List<TripByDate>> response) {
                if (response.isSuccessful()) {
                    arr.addAll((ArrayList<TripByDate>) response.body());
                    route.setChildItemList(arr);
                } else {
                    Toast.makeText(mMainActivity, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripByDate>> call, Throwable t) {
                Log.d("thinhavb", " TripByDate error " + t);
            }
        });
        return arr;
    }

    // thinhav: ham query danh sach cac tuyen di theo don vi duoc chon trong spinner
    private void queryRoute(int i) {
        int id_office = mArrOffices.get(i).getId();
        ApiBuilder.getService().getRoute(id_office).enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                if (response.isSuccessful()) {
                    mArrRoute.clear();
                    final ArrayList arr = (ArrayList<Route>) response.body();
                    mArrRoute.addAll(arr);
                    mRouteAdapter = new ListAdapter(mArrRoute, RouteListFragment.this);
                    mRouteExpandList.setAdapter(mRouteAdapter);
                    for (Route route : mArrRoute) {
                        queryTripByDate(route, mDateTrip);
                    }
                    mProgressList.setVisibility(View.GONE);
                } else {
                    Toast.makeText(mMainActivity, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Log.d("thinhavb", " Route error : " + t);
            }
        });
    }

    // thinhav: load danh sach cac trung tam van tai
    private void loadOffice() {
        ApiBuilder.getService().getAllOffice("1").enqueue(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //       ((MainActivity) getActivity()).setTitleToolbar(R.string.list_route, true, false, true);
//        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        if (mDateTrip == null) {
//            mDateTrip = df.format(Calendar.getInstance().getTime());
//        }
//        setDateButton(mDateTrip);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date:
                final DialogFragment dialogFragment = new DatePickerFragment();
                ((DatePickerFragment) dialogFragment).setFragment(this);
                dialogFragment.show(getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.choose_date));
                break;
        }
    }

    public void setDateButton(String date) {
        mDateButton.setText(date);
        mDateTrip = date;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity) activity;
    }

    public void onClickItemList(int position) {
        mMainActivity.replaceFragment(new ChooseSeatFragment());
    }

    @Override
    public void onResponse(Call<List<Office>> call, Response<List<Office>> response) {
        if (response.isSuccessful() && mArrOffices.size() <= 0) {
            final ArrayList<Office> arr = (ArrayList<Office>) response.body();
            mArrOffices.addAll(arr);
            final OfficalSpinnerAdapter adapterOffice = new OfficalSpinnerAdapter(getActivity(), R.layout.adapter_spinner, mArrOffices);
            mOfficeSpinner.setAdapter(adapterOffice);
        } else {
            Toast.makeText(mMainActivity, response.code(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<List<Office>> call, Throwable t) {
        Log.d("thinhavb", " Office error : " + t);
    }

    // thinhav: class hien thi bang chon ngay de hien thi thong tin cac trip
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private Fragment mFragment;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = day + "-" + (month + 1) + "-" + year;
            ((RouteListFragment) mFragment).setDateButton(date);
        }

        public void setFragment(Fragment f) {
            mFragment = f;
        }
    }
}
