package com.faithForward.network.dto;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\t\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\bB\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u00fb\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\r\u001a\u00020\u0005\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0010\u001a\u00020\u0005\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0005\u0012\u0006\u0010\u0014\u001a\u00020\u0005\u0012\u0006\u0010\u0015\u001a\u00020\u0005\u0012\u0006\u0010\u0016\u001a\u00020\u0003\u0012\b\u0010\u0017\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0018\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0019\u001a\u00020\u0003\u0012\b\u0010\u001a\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u001b\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u001c\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u001d\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u001e\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u001f\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010 \u001a\u00020!\u00a2\u0006\u0002\u0010\"J\t\u0010E\u001a\u00020\u0003H\u00c6\u0003J\t\u0010F\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010G\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010&J\u000b\u0010H\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010I\u001a\u00020\u0005H\u00c6\u0003J\t\u0010J\u001a\u00020\u0012H\u00c6\u0003J\t\u0010K\u001a\u00020\u0005H\u00c6\u0003J\t\u0010L\u001a\u00020\u0005H\u00c6\u0003J\t\u0010M\u001a\u00020\u0005H\u00c6\u0003J\t\u0010N\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010O\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010&J\t\u0010P\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010Q\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010R\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010S\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010T\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010U\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010V\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010W\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010X\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010Y\u001a\u00020!H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0005H\u00c6\u0003J\t\u0010[\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\\\u001a\u00020\u0005H\u00c6\u0003J\t\u0010]\u001a\u00020\u0005H\u00c6\u0003J\t\u0010^\u001a\u00020\u0005H\u00c6\u0003J\t\u0010_\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010`\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010&J\u00bc\u0002\u0010a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\r\u001a\u00020\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00052\b\b\u0002\u0010\u0014\u001a\u00020\u00052\b\b\u0002\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u00032\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0019\u001a\u00020\u00032\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010 \u001a\u00020!H\u00c6\u0001\u00a2\u0006\u0002\u0010bJ\u0013\u0010c\u001a\u00020d2\b\u0010e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010f\u001a\u00020\u0003H\u00d6\u0001J\t\u0010g\u001a\u00020\u0005H\u00d6\u0001R\u0016\u0010\b\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u001a\u0010\u0017\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\'\u001a\u0004\b%\u0010&R\u0018\u0010\u0018\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010$R\u0016\u0010\u0013\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010$R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0016\u0010\u000b\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010$R\u0016\u0010\r\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010$R\u001a\u0010\f\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\'\u001a\u0004\b.\u0010&R\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\'\u001a\u0004\b/\u0010&R\u0016\u0010\u0010\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010$R\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010$R\u0016\u0010\t\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010$R\u0016\u0010\u0011\u001a\u00020\u00128\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u00104R\u0016\u0010\u0016\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010+R\u0016\u0010\u0007\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010$R\u0018\u0010\u001a\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010$R\u0018\u0010\u001c\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010$R\u0018\u0010\u001e\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010$R\u0016\u0010\u0019\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010+R\u0018\u0010\u001b\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010$R\u0018\u0010\u001d\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010$R\u0018\u0010\u001f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010$R\u0016\u0010\n\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b>\u0010$R\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010$R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010$R\u0016\u0010 \u001a\u00020!8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010BR\u0016\u0010\u0014\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010$R\u0016\u0010\u0015\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010$\u00a8\u0006h"}, d2 = {"Lcom/faithForward/network/dto/Movie;", "", "id", "", "type", "", "title", "slug", "description", "posterImage", "thumbnailImage", "imdbRating", "orderIndex", "monetizationType", "planAmount", "planPeriod", "planFrequency", "releaseDate", "", "duration", "videoType", "videoUrl", "showOnTv", "downloadEnable", "downloadUrl", "subtitleOnOff", "subtitleLanguage1", "subtitleUrl1", "subtitleLanguage2", "subtitleUrl2", "subtitleLanguage3", "subtitleUrl3", "videoDetail", "Lcom/faithForward/network/dto/common/VideoDetail;", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Integer;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/faithForward/network/dto/common/VideoDetail;)V", "getDescription", "()Ljava/lang/String;", "getDownloadEnable", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getDownloadUrl", "getDuration", "getId", "()I", "getImdbRating", "getMonetizationType", "getOrderIndex", "getPlanAmount", "getPlanFrequency", "getPlanPeriod", "getPosterImage", "getReleaseDate", "()J", "getShowOnTv", "getSlug", "getSubtitleLanguage1", "getSubtitleLanguage2", "getSubtitleLanguage3", "getSubtitleOnOff", "getSubtitleUrl1", "getSubtitleUrl2", "getSubtitleUrl3", "getThumbnailImage", "getTitle", "getType", "getVideoDetail", "()Lcom/faithForward/network/dto/common/VideoDetail;", "getVideoType", "getVideoUrl", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Integer;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/faithForward/network/dto/common/VideoDetail;)Lcom/faithForward/network/dto/Movie;", "equals", "", "other", "hashCode", "toString", "ff-media-Data_debug"})
public final class Movie {
    @com.google.gson.annotations.SerializedName(value = "id")
    private final int id = 0;
    @com.google.gson.annotations.SerializedName(value = "type")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String type = null;
    @com.google.gson.annotations.SerializedName(value = "title")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @com.google.gson.annotations.SerializedName(value = "slug")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String slug = null;
    @com.google.gson.annotations.SerializedName(value = "description")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String description = null;
    @com.google.gson.annotations.SerializedName(value = "poster_image")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String posterImage = null;
    @com.google.gson.annotations.SerializedName(value = "thumbnail_image")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String thumbnailImage = null;
    @com.google.gson.annotations.SerializedName(value = "imdb_rating")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String imdbRating = null;
    @com.google.gson.annotations.SerializedName(value = "order_index")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer orderIndex = null;
    @com.google.gson.annotations.SerializedName(value = "monetization_type")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String monetizationType = null;
    @com.google.gson.annotations.SerializedName(value = "plan_amount")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer planAmount = null;
    @com.google.gson.annotations.SerializedName(value = "plan_period")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String planPeriod = null;
    @com.google.gson.annotations.SerializedName(value = "plan_frequency")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String planFrequency = null;
    @com.google.gson.annotations.SerializedName(value = "release_date")
    private final long releaseDate = 0L;
    @com.google.gson.annotations.SerializedName(value = "duration")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String duration = null;
    @com.google.gson.annotations.SerializedName(value = "video_type")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String videoType = null;
    @com.google.gson.annotations.SerializedName(value = "video_url")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String videoUrl = null;
    @com.google.gson.annotations.SerializedName(value = "show_on_tv")
    private final int showOnTv = 0;
    @com.google.gson.annotations.SerializedName(value = "download_enable")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer downloadEnable = null;
    @com.google.gson.annotations.SerializedName(value = "download_url")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String downloadUrl = null;
    @com.google.gson.annotations.SerializedName(value = "subtitle_on_off")
    private final int subtitleOnOff = 0;
    @com.google.gson.annotations.SerializedName(value = "subtitle_language1")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String subtitleLanguage1 = null;
    @com.google.gson.annotations.SerializedName(value = "subtitle_url1")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String subtitleUrl1 = null;
    @com.google.gson.annotations.SerializedName(value = "subtitle_language2")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String subtitleLanguage2 = null;
    @com.google.gson.annotations.SerializedName(value = "subtitle_url2")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String subtitleUrl2 = null;
    @com.google.gson.annotations.SerializedName(value = "subtitle_language3")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String subtitleLanguage3 = null;
    @com.google.gson.annotations.SerializedName(value = "subtitle_url3")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String subtitleUrl3 = null;
    @com.google.gson.annotations.SerializedName(value = "video_detail")
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.network.dto.common.VideoDetail videoDetail = null;
    
    public Movie(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String slug, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.lang.String posterImage, @org.jetbrains.annotations.NotNull()
    java.lang.String thumbnailImage, @org.jetbrains.annotations.NotNull()
    java.lang.String imdbRating, @org.jetbrains.annotations.Nullable()
    java.lang.Integer orderIndex, @org.jetbrains.annotations.NotNull()
    java.lang.String monetizationType, @org.jetbrains.annotations.Nullable()
    java.lang.Integer planAmount, @org.jetbrains.annotations.Nullable()
    java.lang.String planPeriod, @org.jetbrains.annotations.NotNull()
    java.lang.String planFrequency, long releaseDate, @org.jetbrains.annotations.NotNull()
    java.lang.String duration, @org.jetbrains.annotations.NotNull()
    java.lang.String videoType, @org.jetbrains.annotations.NotNull()
    java.lang.String videoUrl, int showOnTv, @org.jetbrains.annotations.Nullable()
    java.lang.Integer downloadEnable, @org.jetbrains.annotations.Nullable()
    java.lang.String downloadUrl, int subtitleOnOff, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage3, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl3, @org.jetbrains.annotations.NotNull()
    com.faithForward.network.dto.common.VideoDetail videoDetail) {
        super();
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSlug() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPosterImage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getThumbnailImage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getImdbRating() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getOrderIndex() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMonetizationType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getPlanAmount() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPlanPeriod() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPlanFrequency() {
        return null;
    }
    
    public final long getReleaseDate() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDuration() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVideoType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVideoUrl() {
        return null;
    }
    
    public final int getShowOnTv() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getDownloadEnable() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDownloadUrl() {
        return null;
    }
    
    public final int getSubtitleOnOff() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSubtitleLanguage1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSubtitleUrl1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSubtitleLanguage2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSubtitleUrl2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSubtitleLanguage3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSubtitleUrl3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.dto.common.VideoDetail getVideoDetail() {
        return null;
    }
    
    public final int component1() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component13() {
        return null;
    }
    
    public final long component14() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component16() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component17() {
        return null;
    }
    
    public final int component18() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component20() {
        return null;
    }
    
    public final int component21() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component22() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component23() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component24() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component25() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component26() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component27() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.dto.common.VideoDetail component28() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.dto.Movie copy(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String slug, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.lang.String posterImage, @org.jetbrains.annotations.NotNull()
    java.lang.String thumbnailImage, @org.jetbrains.annotations.NotNull()
    java.lang.String imdbRating, @org.jetbrains.annotations.Nullable()
    java.lang.Integer orderIndex, @org.jetbrains.annotations.NotNull()
    java.lang.String monetizationType, @org.jetbrains.annotations.Nullable()
    java.lang.Integer planAmount, @org.jetbrains.annotations.Nullable()
    java.lang.String planPeriod, @org.jetbrains.annotations.NotNull()
    java.lang.String planFrequency, long releaseDate, @org.jetbrains.annotations.NotNull()
    java.lang.String duration, @org.jetbrains.annotations.NotNull()
    java.lang.String videoType, @org.jetbrains.annotations.NotNull()
    java.lang.String videoUrl, int showOnTv, @org.jetbrains.annotations.Nullable()
    java.lang.Integer downloadEnable, @org.jetbrains.annotations.Nullable()
    java.lang.String downloadUrl, int subtitleOnOff, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage3, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl3, @org.jetbrains.annotations.NotNull()
    com.faithForward.network.dto.common.VideoDetail videoDetail) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}