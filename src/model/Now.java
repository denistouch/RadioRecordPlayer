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

    public Track getTrack() {
        return track;
    }

    @Override
    public String toString() {
        return "id: " + id +
                "\nsong: " + track.getSong() +
                "\nartist: " + track.getArtist() +
                "\nimage_600: " + track.getImage600();

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}