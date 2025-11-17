function init()

    m.top.id = "VideoController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.video = m.top.findNode("Video")
    m.video.observeField("state", "OnVideoPlayerStateChange")
    m.video.observeField("position", "OnVideoPlayerPositionChanged")



    m.video.trickPlayBar.filledBarBlendColor = "#E50914"
    m.video.trickPlayBar.trackBlendColor = "#E50914"

    m.topNav = m.scene.findNode("topNav")
    m.toplogo = m.scene.findNode("toplogo")
    m.topNav.visible = false
    m.toplogo.visible = false
    m.topNavContent = m.scene.findNode("topNavContent")
    m.topNavContent.visible = false

    m.leftmenu = m.scene.findNode("leftMenu")
    m.leftmenu.visible = false

    m.feedsList = m.top.findNode("feedsList")
    m.feedsList.rowLabelFont.uri = "pkg:/images/font/Montserrat-Bold.ttf"
    m.feedsList.rowLabelFont.size = 30
    m.feedlistBack = m.top.findNode("feedlistBack")

    m.countDownTimer = m.top.findNode("countDownTimer")
    m.countDownTimer.ObserveField("fire", "hideFeedsList")

    m.loadSubscriptioTimer = m.top.findNode("loadSubscriptioTimer")
    m.loadSubscriptioTimer.ObserveField("fire", "loadSubscriptioTimerFun")

    m.feedListAnimation = m.top.findNode("feedListAnimation")
    m.feedListAnimation_interplotar = m.top.findNode("feedListAnimation_interplotar")

    m.NextEpisodeUI = m.top.findNode("NextEpisodeUI")

    m.NextEpisodeUI.observeField("isPlayNowPress", "onPlayNowButtonSelected")

    m.NextEpisodeUI.observeField("isCancelPress", "onCancelButtonSelected")


    m.currentIndex = 0
    m.feedListRowVisibility = false
    m.playlist = []
    m.rowFocusIndex = invalid
    m.cancelPress = false

    m.PlayerTask = CreateObject("roSGNode", "PlayerTask")
    m.PlayerTask.observeField("adPlaying", "onAdStateChanged")

    m.playImg = m.top.CreateChild("poster")
    m.playImg.uri = "pkg:/images/play_select.png"
    m.playImg.width = 120
    m.playImg.height = 120
    m.playImg.translation = [(1920 - m.playImg.width) / 2, (1080 - m.playImg.height) / 2]
    m.playImg.visible = false

    m.ButtonPlay = m.top.findNode("ButtonPlay")
    m.ButtonPlay.ObserveField("buttonSelected", "buttonSelectedFunc")
    m.ButtonPause = m.top.findNode("ButtonPause")
    m.ButtonPause.ObserveField("buttonSelected", "buttonSelectedFunc")


    m.playerButtonsHideTimer = m.top.findNode("playerButtonsHideTimer")
    m.playerButtonsHideTimer.ObserveField("fire", "playerButtonsHideTimerFunc")
    m.videoMode = false

    m.TopMenu = m.scene.findNode("LiveTopMenu")
    m.TopMenu.visible = false
    m.TopMenu.observeField("stopVideo", "onLiveTvScreenVisible")
    m.TopMenu.observeFieldScoped("downTopNavEscape", "onDowntTopNavFocusEscape")

    m.topMenuTimer = m.top.findNode("topMenuTimer")
    m.topMenuTimer.ObserveField("fire", "hideTopMenuData")

    m.videoTitle = m.top.findNode("videoTitle")
    m.feedListAlreadyTriggered = false
    m.feedListShown = false

end function


function onNavigateTo(params as object)
    loadAdApi()
    m.isNextVideo = false
    m.nextEpisodeUIData = invalid
    m.position = invalid
    m.relatedData = invalid
    m.seasonData = invalid
    m.content_Id = invalid

    m.continueWatch_content_slug = ""
    m.isContinueWatching = false
    m.content_rowType = ""
    m.currentIndex = 0
    m.videoPosition = invalid

    if isValid(params)
        m.deeplink = params.deeplink
        pageState = params.controllerState
        if pageState <> invalid and pageState.videoPosition <> invalid then m.videoPosition = pageState.videoPosition
        if isValid(params.isResume) then m.isResume = params.isResume
        if isValid(params.currentIndex) then m.currentIndex = params.currentIndex

        if isValid(params.relatedData) then m.relatedData = params.relatedData
        if isValid(params.seasonData) then m.seasonData = params.seasonData

        m.istrailer = false ' default to false first

        if (params <> invalid) then
            if (params.istrailer = true or (params.content_type <> invalid and LCase(params.content_type) = "live channel"))
                m.istrailer = true
            end if
        end if


        if isValid(params.content)
            setUpVideoScreenUI(params.content, m.seasonData, m.relatedData)

        else if params.content = invalid and isValid(params.rowItem_Slug)
            if isValid(params.continueWatch) and not params.continueWatch then m.isContinueWatching = false else m.isContinueWatching = true
            m.continueWatch_content_slug = params.rowItem_Slug
            m.content_Id = params.content_Id
            m.content_rowType = params.content_Type
            loadDetailApi(params.rowItem_Slug)
        else
            addErrorDialog("Attention ", "Passed player info having error, Press back button to dismiss.", ["Dismiss"])
        end if
    else
        addErrorDialog("Attention ", "Passed player info having error, Press back button to dismiss.", ["Dismiss"])
    end if
end function


sub resetFeedListRow()
    if m.feedsList <> invalid
        m.feedsList.UnObserveField("rowItemSelected")
        m.feedsList.UnObserveField("rowItemFocused")
        count = m.feedsList.getChildCount()
        m.feedsList.removeChildrenIndex(count, 0)
    end if
end sub


sub bindRelatedFeedListData(relatedContent)
    if isValid(relatedContent)
        resetFeedListRow()
        rowItemsContent = CreateObject("roSGNode", "ContentNode")
        newRow = CreateObject("roSGNode", "ContentNode")
        newRow.title = "Next Up..."


        for each item in relatedContent
            rowItem = CreateObject("roSGNode", "feedListItemDataModel")
            rowItem.row_type = "video_row"
            rowItem.callFunc("parseData", item)
            newRow.appendChild(rowItem)
        end for

        rowItemsContent.appendChild(newRow)

        m.feedsList.ObserveField("rowItemSelected", "onRowItemSelected")
        m.feedsList.ObserveField("rowItemFocused", "onRowItemFocused")

        m.feedsList.content = rowItemsContent
        m.feedsList.visible = true
    end if
end sub


sub bindSeriesFeedListData(season)
    if isValid(season)
        resetFeedListRow()
        m.isSeriesListCreated = true
        rowItemsContent = CreateObject("roSGNode", "ContentNode")
        newRow = CreateObject("roSGNode", "ContentNode")
        newRow.title = "Episodes"

        ' for each season in seasonData
        if isValid(season.episodes) and season.episodes.count() > 0
            season.episodes.sortBy("episode_number")
            for each episode_item in season.episodes
                rowItem = CreateObject("roSGNode", "feedListItemDataModel")
                rowItem.row_type = "video_row"
                rowItem.callFunc("parseData", episode_item)
                newRow.appendChild(rowItem)
            end for
        end if
        ' end for

        rowItemsContent.appendChild(newRow)

        m.feedsList.ObserveField("rowItemSelected", "onRowItemSelected")
        m.feedsList.ObserveField("rowItemFocused", "onRowItemFocused")

        m.feedsList.content = rowItemsContent
        m.feedsList.visible = true
    end if
end sub


sub onRowItemSelected(event as object)
    data = event.getData()
    rowIndex = data[0]
    rowItemIndex = data[1]
    m.currentIndex = rowItemIndex

    selectedRow = event.getRoSGNode().content.getChild(rowIndex)
    rowItem = selectedRow.getChild(rowItemIndex)
    m.cancelPress = false


    if m.playlist <> invalid and m.seasonData <> invalid
        m.video.control = "stop"
        hideFeedsList()
        videoInfo = m.playlist[rowItemIndex]
        if isValid(videoInfo) then startNextPlayBack(videoInfo)
    else
        m.nextEpisodeUIData = invalid
        hideFeedsList()
        if m.playlist <> invalid then m.playlist.clear()
        m.isNextVideo = false
        loadDetailApi(rowItem.slug)
    end if
end sub


sub onRowItemFocused(event as object)
    focusedData = event.getData()
    rowIndex = focusedData[0]
    itemIndex = focusedData[1]
    if m.rowFocusIndex = invalid then m.rowFocusIndex = itemIndex

    if m.rowFocusIndex <> itemIndex then m.countDownTimer.control = "start"

    focusedContent = m.feedsList.content.getChild(rowIndex).getChild(itemIndex)
end sub


function loadDetailApi(video_id)
    login_Token = LocalStorage().getValueForKey("login_Token")
    videoContent = TVService(m.apiProvider).getDetails(video_id, login_Token)
    videoContent.httpResponse.observeField("response", "onGetContentResponse")
    m.http.sendRequest(videoContent)
end function


function onGetContentResponse(event)
    response = event.getData()
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        videoDetails = response.data.data.data
        if isValid(videoDetails)

            related_content = isValidArray(videoDetails.related_content)
            if related_content.count() > 0 then m.relatedData = related_content

            seasons = isValidArray(videoDetails.seasons)
            if seasons.count() > 0 then m.seasonData = seasons

            if isValid(m.seasonData)
                episodeIndexWithData = getMatchedEpisodeIndexAndData(seasons, m.content_Id)
                videoDetails = episodeIndexWithData.episode
                ' m.currentIndex = episodeIndexWithData.index
            end if

            setUpVideoScreenUI(videoDetails, m.seasonData, m.relatedData)
        end if
    end if
end function


function getMatchedEpisodeIndexAndData(seasons as object, contentId as dynamic) as object
    result = {
        index: -1
        episode: invalid
    }

    if seasons = invalid or seasons.count() = 0
        return result
    end if

    index = 0
    for each season in seasons

        season.episodes.sortBy("episode_number")
        for each episode in season.episodes

            if not isValid(contentId)
                result.index = index
                result.episode = episode
                return result
            end if

            episode_Id = episode.id

            if isString(episode_Id) then episode_Id = episode_Id.toInt()
            if isString(contentId) then contentId = contentId.toInt()

            if episode_Id = contentId
                result.index = index
                result.episode = episode
                return result
            end if

            index = index + 1
        end for
    end for

    return result
end function



function loadContinueWatchingApi(position)
    position = FIX(position)
    login_Token = LocalStorage().getValueForKey("login_Token")
    vdo_slug = m.videoSlug

    videoContent = TVService(m.apiProvider).getAddMyContinueWatching(login_Token, position, vdo_slug, m.total_duration)
    videoContent.httpResponse.observeField("response", "onGetContinueWatchingResponse")
    m.http.sendRequest(videoContent)
end function


sub onGetContinueWatchingResponse(event)
    response = event.getData()
end sub


function setUpVideoScreenUI(videoInfo, seasonData, relatedData) as void

    if isvalid(seasonData)
        season_number = videoInfo.season_number
        season = filterCurrentSeasonData(seasonData, season_number)
        createNextEpisodeList(season)
        bindSeriesFeedListData(season)
    else
        if isvalid(relatedData) then bindRelatedFeedListData(relatedData)
    end if

    m.userConfigData = m.scene.userConfigData


    m.pendingVideoInfo = videoInfo
    enable_subscription = m.userConfigData.enable_subscription
    login_Token = LocalStorage().getValueForKey("login_Token")

    if m.userConfigData.login_enable = "false" and m.userConfigData.qrlogin_enable = "false"
        startPlayBack(videoInfo)
        return
    end if

    if m.istrailer = true
        startPlayBack(videoInfo)

    else if videoInfo.access = "Paid"
        if m.scene.user_type = "Premium"
            startPlayBack(videoInfo)

        else
            m.video.control = "stop"
            m.PlayerTask.control = "stop"
            if enable_subscription = "true" and login_Token <> invalid
                m.loadSubscriptioTimer.control = "start"
            else
                startPlayBack(videoInfo)
            end if
            'm.loadSubscriptioTimer.control = "start"
        end if
    else
        startPlayBack(videoInfo)
    end if

end function

sub loadSubscriptioTimerFun()
    m.scene.unloadController = true
    pageInfo = createPageInfo(m.constants.CONTROLLERS.SUBSCRIPTIONPLAN, { returnToVideo: true, videoInfo: m.pendingVideoInfo })
    m.scene.loadController = pageInfo
    m.loadSubscriptioTimer.control = "stop"
end sub

' function filterCurrentSeasonData(seasonData, currentSeasonNumber) as object
'     filteredData = seasonData[currentSeasonNumber - 1]
'     return filteredData
' end function

function filterCurrentSeasonData(seasonData as object, currentSeasonNumber as integer) as object
    for each season in seasonData
        if season.season_number = currentSeasonNumber then
            return season
        end if
    end for
    return invalid
end function


sub startPlayBack(videoInfo)
    m.PlayerTask.control = "STOP"
    m.videoSlug = videoInfo.slug
    videoContent = createObject("RoSGNode", "ContentNode")
    videoContent.streamformat = "auto"
    if videoInfo.video_link <> invalid and videoInfo.video_link.len() > 0
        videoContent.url = videoInfo.video_link
    else if videoInfo.play_link <> invalid and videoInfo.play_link.len() > 0
        videoContent.url = videoInfo.play_link
    end if
    if videoInfo.name <> invalid
        'videoContent.title = videoInfo.name
        m.videoTitle.text = videoInfo.name
    end if

    if isValid(videoInfo.progress_seconds) then m.position = videoInfo.progress_seconds

    m.video.content = videoContent



    if isValid(m.videoPosition) then m.video.seek = m.videoPosition
    if isValid(m.isResume) and m.isResume and isValid(m.position) then m.video.seek = m.position

    enable_ads = m.userConfigData.enable_ads

    if m.istrailer = true or enable_ads = "false"
        m.video.control = "play"
    else
        m.PlayerTask.video = m.video
        m.PlayerTask.control = "RUN"
    end if
    m.video.visible = true
    applyFocus(m.video, true, "onNavigateTo() - VideoController.brs")

    checkNextEpisodeData()
end sub


function onDialogButtonSelected(event)
    buttonIndex = event.getData()
    if buttonIndex = 0
        m.errDialog.close = "true"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.PREMIUM, {})
        m.scene.loadController = pageInfo
    else
        m.errDialog.close = "true"
        goBackInHistory(m.scene)
    end if
end function


sub addErrorDialog(title as string, message as string, button as object)
    errDialog = CreateObject("roSGNode", "Dialog")
    errDialog.title = title
    errDialog.message = message
    ' errDialog.buttons = button
    m.scene.dialog = errDialog
end sub

' function onButtonSelected(event)
'     m.scene.dialog.close = true
'     'goBackInHistory(m.scene)
' end function

function OnVideoPlayerStateChange()
    position = m.video.position
    m.total_duration = m.video.duration

    if m.video.state = "error"
        m.PlayerTask.control = "STOP"
        m.video.control = "stop"
        addErrorDialog("Playback Error", "Unable to play the video. Press back button to dismiss.", ["Dismiss"])


    else if m.video.state = "playing"
        m.videoMode = true

    else if m.video.state = "stopped"
        updateContinueWatch_ProgressSeconds(position)

    else if m.video.state = "finished"
        m.video.control = "stop"
        updateContinueWatch_ProgressSeconds(position)

        if m.nextEpisodeUIData <> invalid
            startNextPlayBack(m.nextEpisodeUIData)
        else
            if m.deeplink = "true"
                m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
            else
                backToPreviousScreen()
            end if
        end if
    end if

end function


sub updateContinueWatch_ProgressSeconds(position)
    ?"m.content_rowTypem.content_rowTypem.content_rowTypem.content_rowType " m.content_rowType
    if m.content_rowType= invalid or Lcase(m.content_rowType) <> "live channel"  'isValid(m.content_rowType)
        loadContinueWatchingApi(position)
    end if
end sub


function OnVideoPlayerPositionChanged(event)
    curr_vdo_position = event.getData()
    total_vdo_duration = m.video.duration
    time_remaining = Fix(total_vdo_duration - curr_vdo_position)

    if time_remaining = 20 and not m.feedListShown and not m.feedListAlreadyTriggered
        m.feedListShown = true
        m.feedListAlreadyTriggered = true

        if m.isNextVideo and isValid(m.nextEpisodeUIData) and not m.cancelPress
            m.NextEpisodeUI.visible = true
            m.NextEpisodeUI.callFunc("setUpNextEpisodeUI", m.nextEpisodeUIData)
            applyFocus(m.NextEpisodeUI.findNode("playNowButton"), true, "onNavigateTo() - NextEpisodeUI.brs")

        else if isValid(m.relatedData)
            m.NextEpisodeUI.visible = false
            showFeedList()
        end if

    else if time_remaining <> 20
        m.feedListShown = false
        m.feedListAlreadyTriggered = false
    end if
end function


sub onPlayNowButtonSelected(event as object)
    isPress = event.getData()
    if isPress
        m.video.control = "stop"
        m.NextEpisodeUI.isPlayNowPress = false
        m.NextEpisodeUI.visible = false
        if isValid(m.nextEpisodeUIData) then startNextPlayBack(m.nextEpisodeUIData)
    end if
end sub

sub onCancelButtonSelected(event as object)
    m.PlayerTask.control = "stop"
    m.video.control = "stop"
    isPress = event.getData()
    if isPress
        m.cancelPress = true
        m.NextEpisodeUI.isCancelPress = false
        m.NextEpisodeUI.visible = false
    end if
end sub

sub startNextPlayBack(videoInfo)

    m.PlayerTask.control = "STOP"
    m.videoSlug = videoInfo.slug

    videoContent = createObject("RoSGNode", "ContentNode")
    videoContent.streamformat = "auto"
    if videoInfo.video_link <> invalid and videoInfo.video_link.len() > 0
        videoContent.url = videoInfo.video_link
    end if

    if videoInfo.name <> invalid
        'videoContent.title = videoInfo.name
        m.videoTitle.text = videoInfo.name
    end if


    if isValid(videoInfo.progress_seconds) then m.position = videoInfo.progress_seconds

    m.video.content = videoContent

    if isValid(m.position) then m.video.seek = m.position


    m.PlayerTask.video = m.video
    m.PlayerTask.control = "RUN"
    m.video.visible = true
    applyFocus(m.video, true, "onNavigateTo() - VideoController.brs")

    checkNextEpisodeData()
end sub

sub createNextEpisodeList(season)
    if isValid(season)
        m.playlist = []
        m.playlist.clear()
        ' for each season in seasonData
        if isValid(season.episodes) and season.episodes.count() > 0
            season.episodes.sortBy("episode_number")
            for each episode_item in season.episodes
                m.playlist.push(episode_item)
                m.isNextVideo = true
            end for
        end if
        ' end for
    else
        m.isNextVideo = false
    end if
end sub


sub checkNextEpisodeData()
    m.nextEpisodeUIData = invalid
    if isValid(m.playlist) and m.currentIndex <= m.playlist.count() - 1
        m.currentIndex = m.currentIndex + 1
        nextVideoInfo = m.playlist[m.currentIndex]
        if isValid(nextVideoInfo)
            m.nextEpisodeUIData = nextVideoInfo
        end if
    end if

    'if m.nextEpisodeUIData = invalid and isValid(m.relatedData) then bindRelatedFeedListData(m.relatedData)
end sub


sub backToPreviousScreen()
    m.currentIndex = 0
    m.isNextVideo = false
    m.PlayerTask.control = "stop"
    m.video.control = "stop"
    if m.isContinueWatching
        if isValid(m.content_rowType) and not isLiveType(m.content_rowType)
            loadDetailController(m.continueWatch_content_slug)
        else
            goBackInHistory(m.scene)
        end if
    else
        if m.deeplink = "true"
            m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
        else
            goBackInHistory(m.scene)
        end if
    end if
end sub


function isLiveType(rowType as string) as boolean
    rowType = LCase(rowType.trim())
    return rowType = "live-channels" or rowType = "live-stream" or rowType = "live channel"
end function


sub loadDetailController(slug as string)
    m.scene.unloadController = true
    pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, {
        rowItem_Slug: slug
    })
    m.scene.loadController = pageInfo
end sub


sub hideFeedsList()
    applyFocus(m.video, true, "onNavigateTo() - VideoController.brs")
    m.feedListAnimation_interplotar.keyValue = [[65, 670], [65, 1350]]
    m.feedListAnimation.control = "start"
    m.countDownTimer.control = "stop"
    m.feedListRowVisibility = false
    m.feedlistBack.visible = false
end sub


sub showFeedList()
    m.feedListAnimation_interplotar.keyValue = [[65, 1350], [65, 670]]
    m.feedListAnimation.control = "start"
    m.feedsList.jumpToRowItem = [0, 0]
    applyFocus(m.feedsList, true, "onNavigateTo() - VideoController.brs")
    m.countDownTimer.control = "start"
    m.feedListRowVisibility = true
    m.feedlistBack.visible = true
end sub

sub onDowntTopNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.DOWN, true)
    end if
end sub

function OnKeyEvent(key as string, press as boolean) as boolean

    if key = "OK" and m.playImg.visible = false
        if (m.videoMode = true) or (m.video.control = "pause")
            if m.content_rowType <> invalid and Lcase(m.content_rowType.toStr()) <> "live channel"
                m.focusKey = 1
                updateFocus()
            end if
        end if
    end if

    if not press then return false


    if key = m.constants.REMOTE_KEYS.BACK
        if m.feedsList.hasFocus()
            hideFeedsList()
        else
            position = m.video.position
            updateContinueWatch_ProgressSeconds(position)
            backToPreviousScreen()
            return true
        end if
        return true

    else if (key = m.constants.REMOTE_KEYS.DOWN and m.TopMenu.visible)
        m.TopMenu.visible = false
        m.videoTitle.visible = false
        m.topMenuTimer.control = "stop"
        showFeedList()
        return true

    else if (key = m.constants.REMOTE_KEYS.DOWN and m.video.hasFocus())
        if not m.feedListRowVisibility and m.feedsList.visible and not m.NextEpisodeUI.visible then showFeedList()
        return true
    else if key = m.constants.REMOTE_KEYS.DOWN and m.feedsList.hasFocus()
        hideFeedsList()
        return true

    else if (key = m.constants.REMOTE_KEYS.UP and m.video.hasFocus()) or (key = m.constants.REMOTE_KEYS.UP and m.feedsList.hasFocus())
        ' m.TopMenu.findNode("topLiveMenu").jumpToRowItem = [0, 0]
        ' hideFeedsList()
        ' m.TopMenu.itemSelected = -1
        ' m.TopMenu.visible = true
        ' m.videoTitle.visible = true
        ' applyFocus(m.TopMenu, true, "onNavigateTo() - VideoController.brs")
        ' m.topMenuTimer.control = "start"
        return true
    else if key = m.constants.REMOTE_KEYS.LEFT
        return true
    end if

    return false
end function

function backFromAdsFun(event as object)
    backToPreviousScreen()
end function

sub playerButtonsHideTimerFunc()
    m.playerButtonsHideTimer.control = "stop"
    m.playImg.visible = false
    m.videoTitle.visible = false
    applyFocus(m.video, true, "onNavigateTo() - VideoController.brs")
end sub

sub buttonSelectedFunc(obj)
    if obj.getNode() = "ButtonPlay" or obj.getNode() = "ButtonPause"
        if m.video.control = "pause"
            m.videoMode = true
            m.video.control = "resume"
            m.focusKey = 1
            updateFocus()
        else
            m.videoMode = false
            m.video.control = "pause"
            m.playerButtonsHideTimer.control = "start"
            m.playImg.uri = "pkg:/images/play_select.png"
            m.playImg.visible = true
            applyFocus(m.ButtonPause, true, "onNavigateTo() - VideoController.brs")
        end if
    end if
end sub


sub updateFocus()
    if m.focusKey = 1
        m.playerButtonsHideTimer.control = "start"
        m.playImg.visible = true
        m.videoTitle.visible = true
        applyFocus(m.ButtonPlay, true, "onNavigateTo() - VideoController.brs")

        if m.videoMode = false
            m.playImg.uri = "pkg:/images/play_select.png"
        else
            m.playImg.uri = "pkg:/images/pause_select.png"
        end if
    end if
end sub

sub onLiveTvScreenVisible(event)

    if event.getData() <> invalid and event.getData() = true
        if m.video.state = "playing"
            position = m.video.position
            updateContinueWatch_ProgressSeconds(position)
        end if
    end if
    m.PlayerTask.control = "stop"
    m.video.control = "stop"
end sub

sub hideTopMenuData()
    m.topMenuTimer.control = "stop"
    m.videoTitle.visible = false
    m.TopMenu.visible = false
    applyFocus(m.video, true, "onNavigateTo() - VideoController.brs")
end sub

sub onAdStateChanged(event as object)
    isAdPlaying = event.getData()
    if isAdPlaying
        m.TopMenu.visible = false
        m.videoTitle.visible = false
        m.topMenuTimer.control = "stop"
    else
        m.videoTitle.visible = true
    end if
end sub

function onNavigateAway()
    m.top.controllerState = { videoPosition: m.video.position }
end function



function loadAdApi()
    AdContent = TVService(m.apiProvider).getAdApi()
    AdContent.httpResponse.observeField("response", "onGetAdContentResponse")
    httpClient().sendRequest(AdContent)
end function

sub onGetAdContentResponse(event)
    response = event.getData()

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data) and response.data.data.data.google_ima_sdk.tag_url <> ""
        adData = response.data.data.data.google_ima_sdk.tag_url
        m.PlayerTask.adVideo = adData
    end if
end sub