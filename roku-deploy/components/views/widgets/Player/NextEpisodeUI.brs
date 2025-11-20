sub init()
    m.top.id = "NextEpisodeUI"

    m.scene = getScene()
    m.http = httpClient()

    m.nextUIGroup = m.top.findNode("nextUIGroup")

    m.bottomOverlayRect = m.top.findNode("bottomOverlayRect")

    m.upNext = m.top.findNode("upNext")

    m.countdownCircle = m.top.findNode("countdownCircle")

    m.countdownLabel = m.top.findNode("countdownLabel")

    m.TimerMessage = m.top.findNode("TimerMessage")

    m.nextPoster = m.top.findNode("nextPoster")

    m.NextEpisodeTitle = m.top.findNode("NextEpisodeTitle")

    m.NextEpisodeMetaData = m.top.findNode("NextEpisodeMetaData")

    m.NextEpisodeDescription = m.top.findNode("NextEpisodeDescription")

    m.btnListLayout = m.top.findNode("btnListLayout")

    m.cancelButton = m.top.FindNode("cancelButton")
    m.cancelButton.imgSrc = { label: "Cancel", unFocusImg: "pkg://images/creatorButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 200, height: 76 }

    m.playNowButton = m.top.findNode("playNowButton")
    m.playNowButton.imgSrc = { label: "Play Now", unFocusImg: "pkg://images/creatorButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }

    m.playNowButton.observeField("buttonSelected", "onPlayNowBtnSelected")

    m.cancelButton.observeField("buttonSelected", "onCancelBtnSelected")

    m.episodeTimer = m.top.findNode("episodeTimer")
    m.episodeTimer.observeField("fire", "onTimerTick")

    m.top.observeFieldScoped("focusedChild", "onFocusChanged")

end sub


function onFocusChanged(event as object)
    if (m.top.hasFocus())
        applyFocus(m.playNowButton, true, "onFocusChanged() - NextEpisodeUI.brs")
    end if
end function

sub setUpNextEpisodeUI(episdoe_data)
    if isValid(episdoe_data)
        m.countdownLabel.text = "20"
        m.nextPoster.uri = episdoe_data.landscape
        m.NextEpisodeTitle.text = episdoe_data.name
        m.NextEpisodeMetaData.text = "Season " + episdoe_data.season_number.toStr() + ", Episode " + episdoe_data.episode_number.toStr()
        m.NextEpisodeDescription.text = episdoe_data.description
        m.top.timeDuration = 20
        m.episodeTimer.control = "start"
    end if
end sub


sub onTimerTick()
    timeDuration = m.top.timeDuration

    timeDuration = timeDuration - 1

    m.top.timeDuration = timeDuration

    if timeDuration <= 0
        m.episodeTimer.control = "stop"
        m.top.isPlayNowPress = true
    else
        m.countdownLabel.text = timeDuration.toStr()
        m.TimerMessage.text = "Playing next content in " + timeDuration.toStr() + " seconds"
    end if

end sub


' sub updateTimer(leftDuration as integer)
'     m.countdownLabel.text = leftDuration.toStr()
' end sub

sub onPlayNowBtnSelected()
    m.episodeTimer.control = "stop"
    m.top.isPlayNowPress = true
end sub


sub onCancelBtnSelected()
    m.episodeTimer.control = "stop"
    m.top.isCancelPress = true
    goBackInHistory(m.scene)
end sub


' creates a focus map
' '@return object focus map
function createListFocusMap()
    focusMap = {}
    focusMap[m.playNowButton.id] = { up: invalid, down: invalid, left: m.cancelButton.id, right: invalid }
    focusMap[m.cancelButton.id] = { up: invalid, down: invalid, left: invalid, right: m.playNowButton.id }
    return focusMap
end function


function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false
    focusMap = createListFocusMap()
    currentFocusComp = getCurrentFocusItem(m.scene)
    if (press)
        if key = "back"
            goBackInHistory(m.scene)
            handled = true
        else if key = "left"
            if m.playNowButton.hasFocus()
                applyFocus(m.cancelButton, true, "onFocusChanged() - NextEpisodeUI.brs")
            else
                handled = true
            end if
        else if key = "right"
            if m.cancelButton.hasFocus()
                applyFocus(m.playNowButton, true, "onFocusChanged() - NextEpisodeUI.brs")
            else
                handled = true
            end if
        else
            'it should work, will check later why its not working...
            ' ?"multiple data-----"key, focusMap, currentFocusComp
            ' handled = componentFocusHandler(key, focusMap, currentFocusComp)
            ' ?"handled----"handled
            handled = true
        end if
    end if
    return handled
end function