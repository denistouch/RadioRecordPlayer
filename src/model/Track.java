package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("image100")
    @Expose
    private String image100;
    @SerializedName("image200")
    @Expose
    private String image200;
    @SerializedName("image600")
    @Expose
    private String image600;
    @SerializedName("listenUrl")
    @Expose
    private String listenUrl;
    @SerializedName("itunesUrl")
    @Expose
    private String itunesUrl;
    @SerializedName("itunesId")
    @Expose
    private String itunesId;
    @SerializedName("noFav")
    @Expose
    private Boolean noFav;
    @SerializedName("noShow")
    @Expose
    private Boolean noShow;
    @SerializedName("shareUrl")
    @Expose
    private String shareUrl;


    public Integer getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    public String getImage100() {
        if (!image100.contains("https"))
            return "https://2019.radiorecord.ru" + image100;
        else return image100;
    }

    public String getImage200() {
        if (!image200.contains("https"))
            return "https://2019.radiorecord.ru" + image200;
        else return image200;
    }

    public String getImage600() {
        if (!image600.contains("https"))
            return "https://2019.radiorecord.ru" + image600;
        else return image600;
    }

    public String getListenUrl() {
        return listenUrl;
    }

    public void setListenUrl(String listenUrl) {
        this.listenUrl = listenUrl;
    }

    public String getItunesUrl() {
        return itunesUrl;
    }

    public void setItunesUrl(String itunesUrl) {
        this.itunesUrl = itunesUrl;
    }

    public String getItunesId() {
        return itunesId;
    }

    public void setItunesId(String itunesId) {
        this.itunesId = itunesId;
    }

    public Boolean getNoFav() {
        return noFav;
    }

    public void setNoFav(Boolean noFav) {
        this.noFav = noFav;
    }

    public Boolean getNoShow() {
        return noShow;
    }

    public void setNoShow(Boolean noShow) {
        this.noShow = noShow;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public void setImage100(String image100) {
        this.image100 = image100;
    }

    public void setImage200(String image200) {
        this.image200 = image200;
    }

    public void setImage600(String image600) {
        this.image600 = image600;
    }
}