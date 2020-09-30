package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultNow {

    @SerializedName("result")
    @Expose
    private List<Now> result = null;

    public List<Now> getResult() {
        return result;
    }

    public void setResult(List<Now> result) {
        this.result = result;
    }

    public String toString() {
        return result.toString();
    }

}
