package com.faithForward.network.dto;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b=\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B\u0089\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0012\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0015\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u001c\u001a\u00020\u001d\u0012\u0006\u0010\u001e\u001a\u00020\u001f\u00a2\u0006\u0002\u0010 J\t\u0010A\u001a\u00020\u0003H\u00c6\u0003J\t\u0010B\u001a\u00020\u000eH\u00c6\u0003J\u000b\u0010C\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010D\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010E\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010F\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010G\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010H\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010I\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010J\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010K\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010M\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010N\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010O\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010P\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010Q\u001a\u00020\u001dH\u00c6\u0003J\t\u0010R\u001a\u00020\u001fH\u00c6\u0003J\t\u0010S\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010T\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010U\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010V\u001a\u00020\u0003H\u00c6\u0003J\t\u0010W\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010X\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010Y\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010,J\u00a6\u0002\u0010Z\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00052\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00052\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001fH\u00c6\u0001\u00a2\u0006\u0002\u0010[J\u0013\u0010\\\u001a\u00020]2\b\u0010^\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010_\u001a\u00020\u0005H\u00d6\u0001J\t\u0010`\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\"R\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\"R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\"R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\'R\u0018\u0010\u000b\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\"R\u0011\u0010\u001e\u001a\u00020\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u001a\u0010\f\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010-\u001a\u0004\b+\u0010,R\u0016\u0010\n\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\"R\u0016\u0010\r\u001a\u00020\u000e8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0016\u0010\u0012\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\'R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u0010\"R\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\"R\u0018\u0010\u0018\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010\"R\u0018\u0010\u001a\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\"R\u0016\u0010\u0015\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010\'R\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010\"R\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010\"R\u0018\u0010\u001b\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010\"R\u0016\u0010\t\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b:\u0010\"R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010\"R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b<\u0010\"R\u0016\u0010\u001c\u001a\u00020\u001d8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010>R\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010\"R\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b@\u0010\"\u00a8\u0006a"}, d2 = {"Lcom/faithForward/network/dto/Item;", "", "type", "", "id", "", "title", "slug", "description", "thumbnailImage", "posterImage", "imdbRating", "orderIndex", "releaseDate", "", "duration", "videoType", "videoUrl", "showOnTv", "downloadEnable", "downloadUrl", "subtitleOnOff", "subtitleLanguage1", "subtitleUrl1", "subtitleLanguage2", "subtitleUrl2", "subtitleLanguage3", "subtitleUrl3", "videoDetail", "Lcom/faithForward/network/dto/common/VideoDetail;", "monetization", "Lcom/faithForward/network/dto/Monetization;", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/faithForward/network/dto/common/VideoDetail;Lcom/faithForward/network/dto/Monetization;)V", "getDescription", "()Ljava/lang/String;", "getDownloadEnable", "getDownloadUrl", "getDuration", "getId", "()I", "getImdbRating", "getMonetization", "()Lcom/faithForward/network/dto/Monetization;", "getOrderIndex", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getPosterImage", "getReleaseDate", "()J", "getShowOnTv", "getSlug", "getSubtitleLanguage1", "getSubtitleLanguage2", "getSubtitleLanguage3", "getSubtitleOnOff", "getSubtitleUrl1", "getSubtitleUrl2", "getSubtitleUrl3", "getThumbnailImage", "getTitle", "getType", "getVideoDetail", "()Lcom/faithForward/network/dto/common/VideoDetail;", "getVideoType", "getVideoUrl", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/faithForward/network/dto/common/VideoDetail;Lcom/faithForward/network/dto/Monetization;)Lcom/faithForward/network/dto/Item;", "equals", "", "other", "hashCode", "toString", "ff-media-Data_debug"})
public final class Item {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String type = null;
    private final int id = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String slug = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String description = null;
    @com.google.gson.annotations.SerializedName(value = "thumbnail_image")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String thumbnailImage = null;
    @com.google.gson.annotations.SerializedName(value = "poster_image")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String posterImage = null;
    @com.google.gson.annotations.SerializedName(value = "imdb_rating")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String imdbRating = null;
    @com.google.gson.annotations.SerializedName(value = "order_index")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer orderIndex = null;
    @com.google.gson.annotations.SerializedName(value = "release_date")
    private final long releaseDate = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String duration = null;
    @com.google.gson.annotations.SerializedName(value = "video_type")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String videoType = null;
    @com.google.gson.annotations.SerializedName(value = "video_url")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String videoUrl = null;
    @com.google.gson.annotations.SerializedName(value = "show_on_tv")
    private final int showOnTv = 0;
    @com.google.gson.annotations.SerializedName(value = "download_enable")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String downloadEnable = null;
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
    @org.jetbrains.annotations.NotNull()
    private final com.faithForward.network.dto.Monetization monetization = null;
    
    public Item(@org.jetbrains.annotations.NotNull()
    java.lang.String type, int id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String slug, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.lang.String thumbnailImage, @org.jetbrains.annotations.NotNull()
    java.lang.String posterImage, @org.jetbrains.annotations.Nullable()
    java.lang.String imdbRating, @org.jetbrains.annotations.Nullable()
    java.lang.Integer orderIndex, long releaseDate, @org.jetbrains.annotations.Nullable()
    java.lang.String duration, @org.jetbrains.annotations.Nullable()
    java.lang.String videoType, @org.jetbrains.annotations.Nullable()
    java.lang.String videoUrl, int showOnTv, @org.jetbrains.annotations.Nullable()
    java.lang.String downloadEnable, @org.jetbrains.annotations.Nullable()
    java.lang.String downloadUrl, int subtitleOnOff, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage3, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl3, @org.jetbrains.annotations.NotNull()
    com.faithForward.network.dto.common.VideoDetail videoDetail, @org.jetbrains.annotations.NotNull()
    com.faithForward.network.dto.Monetization monetization) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getType() {
        return null;
    }
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSlug() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getThumbnailImage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPosterImage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getImdbRating() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getOrderIndex() {
        return null;
    }
    
    public final long getReleaseDate() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDuration() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getVideoType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getVideoUrl() {
        return null;
    }
    
    public final int getShowOnTv() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDownloadEnable() {
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
    
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.dto.Monetization getMonetization() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final long component10() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component13() {
        return null;
    }
    
    public final int component14() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component15() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component16() {
        return null;
    }
    
    public final int component17() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component19() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component21() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component22() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component23() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.dto.common.VideoDetail component24() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.dto.Monetization component25() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
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
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.faithForward.network.dto.Item copy(@org.jetbrains.annotations.NotNull()
    java.lang.String type, int id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.String slug, @org.jetbrains.annotations.Nullable()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.lang.String thumbnailImage, @org.jetbrains.annotations.NotNull()
    java.lang.String posterImage, @org.jetbrains.annotations.Nullable()
    java.lang.String imdbRating, @org.jetbrains.annotations.Nullable()
    java.lang.Integer orderIndex, long releaseDate, @org.jetbrains.annotations.Nullable()
    java.lang.String duration, @org.jetbrains.annotations.Nullable()
    java.lang.String videoType, @org.jetbrains.annotations.Nullable()
    java.lang.String videoUrl, int showOnTv, @org.jetbrains.annotations.Nullable()
    java.lang.String downloadEnable, @org.jetbrains.annotations.Nullable()
    java.lang.String downloadUrl, int subtitleOnOff, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl1, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl2, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleLanguage3, @org.jetbrains.annotations.Nullable()
    java.lang.String subtitleUrl3, @org.jetbrains.annotations.NotNull()
    com.faithForward.network.dto.common.VideoDetail videoDetail, @org.jetbrains.annotations.NotNull()
    com.faithForward.network.dto.Monetization monetization) {
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