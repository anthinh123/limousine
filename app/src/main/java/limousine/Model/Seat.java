package limousine.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by thinh on 25/08/2017.
 * lop dai dien cho ghe ngo, co trang thai xem da co ng dat chua, dang cho dat
 * update theo bang ticketBySeat
 */

public class Seat {
    public final static int GHE_TRONG = 0;
    public final static int GHE_DANG_CHO_NHAP_THONG_TIN = 1;
    public final static int GHE_DANG_CHO_XAC_NHAN = 2;
    public final static int GHE_DAT_VE_THANH_CONG = 3;
    private String position; // vi tri ghe o so nao

    @SerializedName("status")
    @Expose
    private int state; // trang thai ghe

    @SerializedName("intid")
    @Expose
    private int id;

    public int getState() {
        return state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }
}
