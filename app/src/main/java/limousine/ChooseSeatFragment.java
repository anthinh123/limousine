package limousine;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.thinh.limousine.R;

import java.util.ArrayList;
import java.util.List;

import limousine.Model.Seat;
import limousine.Model.Update;
import limousine.adapter.GridSeatAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thinh on 25/08/2017.
 * KHi gui thong tin len server de cap nhat trang thai ghe. Ghe co 4 trang thai
 * Ghe trong: 0
 * Ghe dang trong thoi gian cho nhap thong tin dat ve (5') : 1
 * Ghe dang trong thoi gian cho goi xac nhan ve (30') : 2
 * Ghe da dat ve thanh cong : 3
 */

public class ChooseSeatFragment extends Fragment {
    public static final int VI_TRI_TAI_XE = 0;
    public static final int VI_TRI_TRONG_1 = 4;
    public static final int VI_TRI_TRONG_2 = 7;
    private static final int XE_9_CHO = 9;

    private RecyclerView mGridSeat;
    private GridSeatAdapter mGridAdapter;
    private ArrayList<Seat> mArrSeat;
    private int mPositionSeat; // thinhav: vi tri ghe duoc chon dat ve
    private ProgressDialog mProgress;
    private boolean isShowDialog; // dung de quyet dinh xem co hien dialog dat ve hay ko?
    private BookTikerDialog mBookTikerDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.choose_seat_fragment, container, false);
        mGridSeat = (RecyclerView) v.findViewById(R.id.grid_seat);
        mArrSeat = new ArrayList<Seat>();
        mGridAdapter = new GridSeatAdapter(mArrSeat, this);
        mGridSeat.setAdapter(mGridAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mGridSeat.setAdapter(mGridAdapter);
        mGridSeat.setHasFixedSize(true);
        mGridSeat.setLayoutManager(gridLayoutManager);
        ApiBuilder.getService().getAllSeat(1).enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                if (response.isSuccessful()) {
                    mArrSeat.clear();
                    final ArrayList<Seat> arr = (ArrayList<Seat>) response.body();
                    if (arr.size() == XE_9_CHO) {
                        Seat seat = new Seat();
                        mArrSeat.addAll(arr);
                        mArrSeat.add(0, seat);
                        mArrSeat.add(4, seat);
                        mArrSeat.add(7, seat);
                        mGridAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Seat>> call, Throwable t) {
                Log.d("thinhav", "getAllSeat err : " + t.toString());
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // thinhav: set trang thai ghe ngoi xem da co ai dat chua
    public void setStatePositionSeat(int state) {
        //  mArrSeat.get(mPositionSeat).setState(state);
        mGridAdapter.notifyDataSetChanged();
    }

    //thinhav: bat su kien khi click vao item trong grid
    public void onItemClick(int position) {
        if (position == VI_TRI_TAI_XE || position == VI_TRI_TRONG_1 || position == VI_TRI_TRONG_2) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.you_not_choose_seat), Toast.LENGTH_SHORT).show();
        } else if (mArrSeat.get(position).getState() == Seat.GHE_DAT_VE_THANH_CONG
                || mArrSeat.get(position).getState() == Seat.GHE_DANG_CHO_XAC_NHAN
                || mArrSeat.get(position).getState() == Seat.GHE_DANG_CHO_NHAP_THONG_TIN) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.seat_have_been_choose), Toast.LENGTH_SHORT).show();
        } else {
            mPositionSeat = position;
            isShowDialog = true;
            // thinhav: gui thong tin len server de giu cho cho thoi gian trong luc nhap thong tin
            sendUpdateSeatToServer(Seat.GHE_DANG_CHO_NHAP_THONG_TIN);
        }
    }

    // thinhav: Gui thong tin len server de cap nhat trang thai ghe. Update vao bang TicketBySeat
    public void sendUpdateSeatToServer(int statusSeat) {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setIndeterminate(true);
        mProgress.show();
        Log.d("thinhav", "id Seat: " + mArrSeat.get(mPositionSeat).getId());
        ApiBuilder.getService().updateSeatStatus(mArrSeat.get(mPositionSeat).getId(), statusSeat).enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if (response.isSuccessful()) {
                    Update update = (Update) response.body();
                    mProgress.cancel();
                    if (update.getRow() > 0 && isShowDialog) {
                        ShowDialogBookTicket();
                    } else if (update.getRow() > 0) {
                        updateSeat();
                        mBookTikerDialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.dont_book_ticker), Toast.LENGTH_SHORT).show();
                    }
                    isShowDialog = false;
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Log.d("thinhavb", "sendUpdateSeatToServer = " + t.toString());
            }
        });
    }

    // thinhav: khoi tao dialog cho dat ve
    private void ShowDialogBookTicket() {
        mBookTikerDialog = new BookTikerDialog();
        mBookTikerDialog.setFragment(this);
        mBookTikerDialog.setCancelable(false);
        mBookTikerDialog.setStyle(R.style.Theme_Dialog, 0);
        mBookTikerDialog.show(getActivity().getFragmentManager(), "asd");
    }

    // thinhav: gui thong tin dat ve len server de xu ly tiep
    public void sendInforToServer() {

    }


    // update lai trang thai ghe sau khi da dat ve thanh cong
    public void updateSeat() {
        ApiBuilder.getService().getSeatById(mArrSeat.get(mPositionSeat).getId()).enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                if (response.isSuccessful()) {
                    final ArrayList<Seat> arr = (ArrayList<Seat>) response.body();
                    final Seat seat = arr.get(0);
                    mArrSeat.remove(mPositionSeat);
                    mArrSeat.add(mPositionSeat, seat);
                    mGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Seat>> call, Throwable t) {
                Log.d("thinhav", "getSeatById errors : " + t.toString());
            }
        });
    }
}
