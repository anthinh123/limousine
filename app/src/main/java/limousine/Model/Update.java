package limousine.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by An Van Thinh on 9/14/2017.
 */

public class Update {
    @SerializedName("changedRows")
    @Expose
    private int row;

    public int getRow() {
        return row;
    }
}
