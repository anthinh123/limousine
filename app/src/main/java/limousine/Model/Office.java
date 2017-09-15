package limousine.Model;

/**
 * Created by An Van Thinh on 9/1/2017.
 * day la lop chua cac thong  tin cua cac don vi doanh nghiep van tai
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Office {
    @SerializedName("office_id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
