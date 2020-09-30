package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("track")
    @Expose
    private Track track;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

}