package model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Station {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("short_title")
    @Expose
    private String shortTitle;
    @SerializedName("icon_gray")
    @Expose
    private String iconGray;
    @SerializedName("icon_fill_colored")
    @Expose
    private String iconFillColored;
    @SerializedName("icon_fill_white")
    @Expose
    private String iconFillWhite;
    @SerializedName("new")
    @Expose
    private Boolean _new;
    @SerializedName("stream_64")
    @Expose
    private String stream64;
    @SerializedName("stream_128")
    @Expose
    private String stream128;
    @SerializedName("stream_320")
    @Expose
    private String stream320;
    @SerializedName("genre")
    @Expose
    private List<Genre> genre = null;
    @SerializedName("detail_page_url")
    @Expose
    private String detailPageUrl;
    @SerializedName("shareUrl")
    @Expose
    private String shareUrl;

    @Override
    public String toString() {
        return "id: " + id +
                "\nprefix: " + prefix +
                "\ntitle: " + title +
                "\nshortTitle: " + shortTitle +
                "\niconGray: " + iconGray +
                "\niconFillColored: " + iconFillColored +
                "\niconFillWhite: " + iconFillWhite +
                "\n_new: " + _new +
                "\nstream64: " + stream64 +
                "\nstream128: " + stream128 +
                "\nstream320: " + stream320 +
                "\ndetailPageUrl: " + detailPageUrl +
                "\nshareUrl: " + shareUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTitle() {
        return title;
    }

    public String getStream128() {
        return stream128;
    }

    public String getStream320() {
        return stream320;
    }

    public String getIconGray() {
        return iconGray;
    }

    public String getIconFillColored() {
        return iconFillColored;
    }

    public String getIconFillWhite() {
        return iconFillWhite;
    }

    public Boolean getNew() {
        return _new;
    }

    public String getStream64() {
        return stream64;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public String getDetailPageUrl() {
        return detailPageUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public void setIconGray(String iconGray) {
        this.iconGray = iconGray;
    }

    public void setIconFillColored(String iconFillColored) {
        this.iconFillColored = iconFillColored;
    }

    public void setIconFillWhite(String iconFillWhite) {
        this.iconFillWhite = iconFillWhite;
    }

    public void setNew(Boolean _new) {
        this._new = _new;
    }

    public void setStream64(String stream64) {
        this.stream64 = stream64;
    }

    public void setStream128(String stream128) {
        this.stream128 = stream128;
    }

    public void setStream320(String stream320) {
        this.stream320 = stream320;
    }

    public void setGenre(List<Genre> genre) {
        this.genre = genre;
    }

    public void setDetailPageUrl(String detailPageUrl) {
        this.detailPageUrl = detailPageUrl;
    }
}

