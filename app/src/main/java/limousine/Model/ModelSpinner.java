package limousine.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by An Van Thinh on 9/11/2017.
 */

public class ModelSpinner {
    @SerializedName("intid")
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
