Library "Roku_Ads.brs"

sub init()
    m.top.functionName = "playContentWithAds"
    m.top.id = "PlayerTask"
end sub

sub playContentWithAds()
    adVideo = m.top.adVideo
    m.video = m.top.video

    m.top.adPlaying = false
    view = m.video.getParent()

    RAF = Roku_Ads()
    RAF.setAdUrl(adVideo) '"https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/vmap_ad_samples&sz=640x480&cust_params=sample_ar%3Dpremidpost&ciu_szs=300x250&gdfp_req=1&ad_rule=1&output=vmap&unviewed_position_start=1&env=vp&cmsid=496&vid=short_onecue&correlator=")
    RAF.enableAdMeasurements(true)


    adPods = RAF.getAds()
    keepPlaying = true

    if adPods <> invalid and adPods.count() > 0
        m.top.adPlaying = true
        keepPlaying = RAF.showAds(adPods, invalid, view)
    end if

    adPlaying = "true"
    port = CreateObject("roMessagePort")
    m.video.observeField("state", port)
    m.video.observeField("position", port)
    m.video.observeField("isPlaybackCompleted", port) 
    m.video.visible = true
    m.video.control = "play"
    m.video.setFocus(true)

    curPos = 0
    isPlayingPostroll = false

    while keepPlaying
        msg = wait(0, port)
        if type(msg) = "roSGNodeEvent"
            field = msg.GetField()
            data = msg.GetData()

            'print "EVENT: "; field; " = "; data

            if field = "position" then
                curPos = data

                ' Mid-roll ad check
                adPods = RAF.getAds(msg)
                if adPods <> invalid and adPods.count() > 0
                    ' Only stop video for mid-roll if we're not near the end (to avoid skipping post-roll)
                    if curPos < (m.video.duration - 5)
                        'print "Mid-roll ad found at position "; curPos; ", stopping video."
                        m.video.control = "stop"
                    end if
                end if

            else if field = "state" then
                if data = "stopped" then
                    if adPods = invalid or adPods.count() = 0
                        'print "Video stopped with no ads pending, exiting."
                        exit while
                    end if

                    'print "Playing mid-roll/post-roll ads..."
                    m.top.adPlaying = true
                    keepPlaying = RAF.showAds(adPods, invalid, view)
                    adPods = invalid

                    if isPlayingPostroll then
                        exit while
                    end if

                    if keepPlaying then
                        'print "Mid-roll ads finished, resuming video at "; curPos
                        m.video.visible = true
                        m.video.seek = curPos
                        m.video.control = "play"
                        m.video.setFocus(true)
                    end if

                else if data = "finished" then
                    'print "Video state is finished, playing post-roll ads..."
                    adPods = RAF.getAds("postroll")
                    if adPods <> invalid and adPods.count() > 0
                        isPlayingPostroll = true
                        keepPlaying = RAF.showAds(adPods, invalid, view)
                    end if
                    exit while
                end if

            else if field = "isPlaybackCompleted" then
                if data = true
                    'print "Playback completed event received, playing post-roll ads..."
                    adPods = RAF.getAds("postroll")
                    if adPods <> invalid and adPods.count() > 0
                        isPlayingPostroll = true
                        keepPlaying = RAF.showAds(adPods, invalid, view)
                    end if
                    exit while
                end if
            end if
        end if
    end while

    'print "playContentWithAds finished. keepPlaying="; keepPlaying
    if not keepPlaying then
        view.backFromAds = true
    end if
end sub

