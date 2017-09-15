package limousine.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by An Van Thinh on 9/5/2017.
 * chuyen di trong 1 tuyen. Vd tuyen HN - CP se co chuyen di luc 8h, luc 9h,..
 */

public class TripByDate {

    @SerializedName("intid")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    private int seat;

    @SerializedName("starttime")
    @Expose
    private String timeStart;

    public TripByDate() {
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
        return timeStart;
    }

    public void setTime(String time) {
        this.timeStart = time;
    }

    public int getId() {
        return id;
    }
}
