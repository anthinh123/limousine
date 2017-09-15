package limousine.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thinh.limousine.R;

import java.util.ArrayList;
import java.util.List;

import limousine.Model.ModelSpinner;
import limousine.Model.Office;

/**
 * Created by An Van Thinh on 9/5/2017.
 */

public class OfficalSpinnerAdapter extends ArrayAdapter<Office> {
    private ArrayList<Office> mArr ;
    private Activity mActivity;

    public OfficalSpinnerAdapter(@NonNull Activity activity, @LayoutRes int resource, @NonNull ArrayList<Office> arr) {
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
