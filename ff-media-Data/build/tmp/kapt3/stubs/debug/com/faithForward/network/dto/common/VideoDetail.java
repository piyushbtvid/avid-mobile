package com.faithForward.network.dto.common;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003JE\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0016\u0010\b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0016\u0010\u0005\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b\u00a8\u0006\u001e"}, d2 = {"Lcom/faithForward/network/dto/common/VideoDetail;", "", "videoThumbnailImage", "", "hlsPlaylistUrl", "previewAnimationUrl", "videoTitle", "playLink", "playUrl", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getHlsPlaylistUrl", "()Ljava/lang/String;", "getPlayLink", "getPlayUrl", "getPreviewAnimationUrl", "getVideoThumbnailImage", "getVideoTitle", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "ff-media-Data_debug"})
public final class VideoDetail {
    @com.google.gson.annotations.SerializedName(value = "video_thumbnail_image")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String videoThumbnailImage = null;
    @com.google.gson.annotations.SerializedName(value = "hls_playlist_url")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String hlsPlaylistUrl = null;
    @com.google.gson.annotations.SerializedName(value = "preview_animation_url")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String previewAnimationUrl = null;
    @com.google.gson.annotations.SerializedName(value = "video_title")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String videoTitle = null;
    @com.google.gson.annotations.SerializedName(value = "play_link")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String playLink = null;
    @com.google.gson.annotations.SerializedName(value = "play_url")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String playUrl = null;
    
    public VideoDetail(@org.jetbrains.annotations.NotNull()
    java.lang.String videoThumbnailImage, @org.jetbrains.annotations.NotNull()
    java.lang.String hlsPlaylistUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String previewAnimationUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String videoTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String playLink, @org.jetbrains.annotations.NotNull()
    java.lang.String playUrl) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVideoThumbnailImage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getHlsPlaylistUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPreviewAnimationUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getVideoTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPlayLink() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPlayUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
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
    public final com.faithForward.network.dto.common.VideoDetail copy(@org.jetbrains.annotations.NotNull()
    java.lang.String videoThumbnailImage, @org.jetbrains.annotations.NotNull()
    java.lang.String hlsPlaylistUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String previewAnimationUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String videoTitle, @org.jetbrains.annotations.NotNull()
    java.lang.String playLink, @org.jetbrains.annotations.NotNull()
    java.lang.String playUrl) {
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