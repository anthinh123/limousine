package limousine.Model;

import android.util.Log;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by thinh on 24/08/2017.
 */

public class Route implements ParentListItem {
    @SerializedName("intid")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;
    private int seat;
    private String time;
    private ArrayList<TripByDate> mChildItemList;

    public Route() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    @Override
    public ArrayList<TripByDate> getChildItemList() {
        return mChildItemList;
    }

    public void setChildItemList(ArrayList<TripByDate> list) {
        mChildItemList = (list);
        Log.d("thinhavb", ""+ mChildItemList.size());
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
