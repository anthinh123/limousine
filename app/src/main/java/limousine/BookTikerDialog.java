package limousine;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thinh.limousine.R;

import java.util.ArrayList;
import java.util.List;

import limousine.Model.BringLocation;
import limousine.Model.Seat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thinh on 25/08/2017.
 */

public class BookTikerDialog extends DialogFragment implements View.OnClickListener {
    private static final int COUNT_TIME = 1;
    private EditText mEditPhone;
    private Button mCancel;
    private Button mBookTicket;
    private ChooseSeatFragment mFragment;
    private TextView mTextCountTime;

    private ArrayList<BringLocation> mArrLocation;
    private LocationSpinnerAdapter mAdapterLocation;
    private Spinner mStartSpinner;
    private Spinner mFinishSpinner;
    private int TIME_COUNT = 30; // thinhav: hen thoi gian nhap thong tin la 5 phut
    private Handler mHandleCountTime;
    private int mIdRoute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_ticker_dialog, container, false);
        mEditPhone = (EditText) v.findViewById(R.id.phone);
        mCancel = (Button) v.findViewById(R.id.cancel);
        mBookTicket = (Button) v.findViewById(R.id.book_ticker);
        mTextCountTime = (TextView) v.findViewById(R.id.text_count_time);
        mCancel.setOnClickListener(this);
        mBookTicket.setOnClickListener(this);

        mArrLocation = new ArrayList<BringLocation>();
        mAdapterLocation = new LocationSpinnerAdapter(getActivity(), R.layout.adapter_spinner, mArrLocation);
        mStartSpinner = (Spinner) v.findViewById(R.id.point_start_spinner);
        mFinishSpinner = (Spinner) v.findViewById(R.id.point_finish_spinner);
        mStartSpinner.setAdapter(mAdapterLocation);
        mFinishSpinner.setAdapter(mAdapterLocation);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateTimeBookTicker();

        //thinhav: start bo dem thoi gian, gioi han 5' danh cho nhap thong tin
        mHandleCountTime = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == COUNT_TIME) {
                    if (TIME_COUNT > 1) {
                        mHandleCountTime.sendEmptyMessageDelayed(COUNT_TIME, 1000);
                    } else {
                        Toast.makeText(mFragment.getActivity(), mFragment.getActivity().getResources().getString(R.string.you_time_out), Toast.LENGTH_SHORT).show();
                        mFragment.sendUpdateSeatToServer(Seat.GHE_TRONG);
                        dismiss();
                    }
                    updateTimeBookTicker();
                }
            }
        };

        // thinhav: query danh sach cac location tren server
        if (mArrLocation.size() <= 0) {
            ApiBuilder.getService().getLocation(mIdRoute).enqueue(new Callback<List<BringLocation>>() {
                @Override
                public void onResponse(Call<List<BringLocation>> call, Response<List<BringLocation>> response) {
                    if (response.isSuccessful()) {
                        final ArrayList<BringLocation> arr = (ArrayList<BringLocation>) response.body();
                        mArrLocation.addAll(arr);
                        mAdapterLocation.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<BringLocation>> call, Throwable t) {
                    Log.d("thinhavb", "err Location" + t.toString());
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandleCountTime.sendEmptyMessageDelayed(COUNT_TIME, 1000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_ticker:
                if (!checkPhoneNumber(mEditPhone.getText() + "")) {
                    Toast.makeText(mFragment.getActivity(), mFragment.getActivity().getResources().getString(R.string.phone_number_is_null), Toast.LENGTH_SHORT).show();
                } else {
                    mFragment.sendInforToServer();
                    mFragment.sendUpdateSeatToServer(Seat.GHE_DANG_CHO_XAC_NHAN);
                    dismiss();
                }
                break;
            case R.id.cancel:
                mFragment.sendUpdateSeatToServer(Seat.GHE_TRONG);
                dismiss();
                break;
        }
    }

    public void setFragment(ChooseSeatFragment fragment) {
        mFragment = fragment;
    }

    // thinhav: cap nhat lai thoi gian dat ve
    public void updateTimeBookTicker() {
        TIME_COUNT--;
        int seconds = (int) ((TIME_COUNT) % 60);
        int minutes = (int) ((TIME_COUNT) / 60);
        mTextCountTime.setText(String.format("%02d : %02d", minutes, seconds));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandleCountTime.removeMessages(COUNT_TIME);
        Log.d("thinhavb", "onDestroyView");
    }

    public boolean checkPhoneNumber(String phone) {
        if ("".equals(mEditPhone.getText() + "")) {
            return false;
        }
        if (10 < phone.length() || phone.length() > 12) {
            return false;
        }
        return true;
    }

    // thinhav: adapter danh cho spinner
    private class LocationSpinnerAdapter extends ArrayAdapter<BringLocation> {
        private ArrayList<BringLocation> mArr;
        private Activity mActivity;

        public LocationSpinnerAdapter(@NonNull Activity activity, @LayoutRes int resource, @NonNull ArrayList<BringLocation> arr) {
            super(activity, resource, arr);
            mArr = arr;
            mActivity = activity;
        }

        @Override
        public int getCount() {
            return mArr.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_spinner, parent, false);
            TextView nameOffice = (TextView) convertView.findViewById(R.id.name);
            nameOffice.setText(mArr.get(position).getName());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.adapter_spinner, parent, false);
            TextView nameOffice = (TextView) convertView.findViewById(R.id.name);
            nameOffice.setText(mArr.get(position).getName());
            return convertView;
        }
    }
}






















