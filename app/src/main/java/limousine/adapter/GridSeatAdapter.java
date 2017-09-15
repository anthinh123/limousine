package limousine.adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.thinh.limousine.R;

import java.util.ArrayList;

import limousine.ChooseSeatFragment;
import limousine.Model.Seat;

/**
 * Created by thinh on 25/08/2017.
 */

public class GridSeatAdapter extends RecyclerView.Adapter<GridSeatAdapter.ViewHolder> {
    private ArrayList<Seat> mArr;
    private ChooseSeatFragment mChooseFragment;

    public GridSeatAdapter (ArrayList<Seat> arr, Fragment fragment){
        mArr = arr;
        mChooseFragment = (ChooseSeatFragment) fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_seat_item, parent, false);
        return new GridSeatAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Seat seat =  mArr.get(position);
        holder.mName.setText(seat.getId()+"");
        if(seat.getState() == Seat.GHE_TRONG){
            holder.mName.setBackgroundColor(Color.WHITE);
            holder.mName.setText("GHE_TRONG");
        } else if (seat.getState() == Seat.GHE_DANG_CHO_NHAP_THONG_TIN){
            holder.mName.setBackgroundColor(Color.RED);
            holder.mName.setText("GHE_DANG_CHO_NHAP_THONG_TIN");
        } else if (seat.getState() == Seat.GHE_DANG_CHO_XAC_NHAN){
            holder.mName.setBackgroundColor(Color.BLUE);
            holder.mName.setText("GHE_DANG_CHO_XAC_NHAN");
        } else{
            holder.mName.setText("GHE_DAT_VE_THANH_CONG");
        }
        if (position == ChooseSeatFragment.VI_TRI_TAI_XE || position == ChooseSeatFragment.VI_TRI_TRONG_1
                || position == ChooseSeatFragment.VI_TRI_TRONG_2) {
            holder.mName.setText("vi tri khong chon duoc");
        }
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChooseFragment.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
        }
    }

}
