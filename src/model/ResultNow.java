package model;

import java.util.List;
import java.util.stream.Stream;

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

    @Override
    public String toString() {
        return result.toString();
    }

    public Now getById(int id) {
        for (Now now : result) {
            if (now.getId() == id)
                return now;
        }
        return null;
    }
}
