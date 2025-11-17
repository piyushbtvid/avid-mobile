sub init()

    m.top.id = "StreamController"

    m.scene = getScene()
    m.constants = GetConstants()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.background = m.top.findNode("background")
    m.channelList = m.top.findNode("channelList")
    m.liveVideo = m.top.findNode("liveVideo")
    m.liveDataGrp = m.top.findNode("liveDataGrp")
    m.liveDataGrp.visible = false
    m.epgTimer = m.top.findNode("epgTimer")
    m.epgTimer.ObserveField("fire", "hideEpgData")


    m.scene.observeFieldScoped("upNavEscape", "onUpNavFocusEscape")
    m.scene.observeFieldScoped("downNavEscape", "onDowntNavFocusEscape")

    m.channelList.observeField("rowItemSelected", "onChannelSelected")
    m.channelList.observeField("rowItemFocused", "onChannelFocused")

    m.TopMenu = m.scene.findNode("LiveTopMenu")

    m.BrowseSelectTopMenu = m.scene.findNode("BrowseSelectTopMenu")
    m.BrowseSelectTopMenu.visible = false

    m.TopMenu.findNode("topLiveMenu").jumpToRowItem = [0, 0]
    m.TopMenu.observeFieldScoped("downTopNavEscape", "onDowntTopNavFocusEscape")
    m.TopMenu.observeFieldScoped("rightNavEscape", "onRightNavFocusEscape")

    m.leftmenu = m.scene.findNode("leftMenu")
    m.leftmenu.visible = false

    m.streamTitle = m.top.findNode("streamTitle")

end sub

sub playVideo(streamURL)
    m.liveVideo.control = "stop"
    videoContent = createObject("RoSGNode", "ContentNode")
    videoContent.streamformat = "auto"
    videoContent.url = streamURL
    m.liveVideo.content = videoContent
    m.liveVideo.control = "play"
    m.liveVideo.visible = true
    m.epgTimer.control = "start"
end sub

function onNavigateTo(params as object)
    m.TopMenu.visible = true

    if params.content <> invalid and params.content.video_detail <> invalid
        playVideo(params.content.video_detail.play_url)
        m.liveVideo.visible = true
        m.TopMenu.itemSelected = 0
    else
        m.TopMenu.itemSelected = 0
        m.liveVideo.visible = true
        playVideo("https://rcntv-rcnmas-1-us.roku.wurl.tv//playlist.m3u8")
    end if
    ' applyFocus(m.TopMenu, true, "onNavigateTo() - StreamController.brs")
    setupEPGGrid()
    m.epgTimer.control = "start"
    showEpgData()

    m.focusTimer = createObject("roSGNode", "Timer")
    m.focusTimer.duration = 0.1
    m.focusTimer.repeat = false
    m.focusTimer.observeField("fire", "setInitialFocus")
    m.top.appendChild(m.focusTimer)
    m.focusTimer.control = "start"
end function

sub setInitialFocus()
    if m.TopMenu <> invalid
        applyFocus(m.TopMenu, true, "setInitialFocus() - BrowserController.brs")
    end if
    if m.focusTimer <> invalid
        m.focusTimer.control = "stop"
        m.top.removeChild(m.focusTimer)
    end if
end sub


function setupEPGGrid()
    content = [{
            "portrait": "https://plus-tv-storage-dev.b-cdn.net/website/1750374504_ffb_img.jpg",
        },
        {
            "portrait": "https://plus-tv-storage-dev.b-cdn.net/website/1750814767_ffb_img.jpg",
        },
        {
            "portrait": "https://plus-tv-storage-dev.b-cdn.net/website/1750890784_ffb_img.jpg",
        }
    ]


    m.streamTitle.text = "Recommendations"
    gridContent = CreateObject("roSGNode", "ContentNode")

    for each item in content
        gridItem = gridContent.CreateChild("feedListItemDataModel")
        gridItem.thumbnail_image = item.portrait
        ?"itemitem " gridItem.thumbnail_image
    end for

    m.channelList.content = gridContent
    m.channelList.visible = true

    m.channelList.numRows = 3
    m.channelList.numColumns = 1
end function

sub onChannelFocused(event as object)
    ' focusedIndex = event.getData()
    ' row = m.channelList.content.getChild(focusedIndex[0])
    ' rowItem = row.getChild(focusedIndex[1])

    ' if rowItem.type <> "channel"
    '     m.channelList.jumpToRowItem = focusedIndex
    ' end if
end sub

sub onChannelSelected(event as object)
    selectedIndex = event.getData()
    row = m.channelList.content.getChild(selectedIndex[0])
    rowItem = row.getChild(selectedIndex[1])

    if rowItem.type <> "channel"
        playVideo("https://rcntv-rcnmas-1-us.roku.wurl.tv//playlist.m3u8")
    end if

end sub


function onUpNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.UP, true)
    end if
end function

function onDowntNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.DOWN, true)
    end if
end function

sub onDowntTopNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.DOWN, true)
    end if
end sub

function onRightNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.RIGHT, true)
    end if
end function

sub navigateHome()
    m.top.stopVideo = true
    goBackInHistory(m.scene)
    handled = true
end sub

function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false

    if (press)


        focusMap = createFocusMap()
        currentFocusMap = getCurrentFocusItem(m.scene)


        if key = m.constants.remote_keys.BACK
            if m.liveDataGrp.visible
                m.liveVideo.control = "stop"
                m.TopMenu.visible = false
                ' Add small delay
                m.navigationTimer = m.top.findNode("navigationTimer")
                if m.navigationTimer = invalid
                    m.navigationTimer = createObject("roSGNode", "Timer")
                    m.navigationTimer.duration = 0.1
                    m.navigationTimer.repeat = false
                    m.top.appendChild(m.navigationTimer)
                end if
                m.navigationTimer.control = "start"
                m.navigationTimer.observeField("fire", "navigateHome")
                handled = true
            end if
            handled = true

        else if key = m.constants.remote_keys.LEFT
            if m.liveDataGrp.visible
                m.liveVideo.control = "stop"
                m.TopMenu.visible = false
                ' Add small delay
                m.navigationTimer = m.top.findNode("navigationTimer")
                if m.navigationTimer = invalid
                    m.navigationTimer = createObject("roSGNode", "Timer")
                    m.navigationTimer.duration = 0.1
                    m.navigationTimer.repeat = false
                    m.top.appendChild(m.navigationTimer)
                end if
                m.navigationTimer.control = "start"
                m.navigationTimer.observeField("fire", "navigateHome")
                handled = true
            else
                showEpgData()
                handled = true
            end if
        else if key = m.constants.remote_keys.OK
            handled = true

        else if key = m.constants.remote_keys.RIGHT
            handled = true
        else if key = m.constants.remote_keys.DOWN
            if m.liveDataGrp.visible
                applyFocus(m.channelList, true, "onNavigateTo() - StreamController.brs")
                handled = true
            else if not m.liveDataGrp.visible and m.channelList.hasFocus() = false
                applyFocus(m.channelList, true, "onNavigateTo() - BrowserController.brs")
                handled = true
            else
                ' Keep focus on TopMenu
                applyFocus(m.TopMenu, true, "StreamController.brs")
                handled = true
            end if
        else
            handled = componentFocusHandler(key, focusMap, currentFocusMap)
        end if
        if m.liveDataGrp.visible = false
            showEpgData()
        end if
    end if
    return handled
end function

function createFocusMap()
    focusMap = {}

    focusMap[m.TopMenu.id] = { up: invalid, down: invalid, Left: invalid, Right: invalid }
    focusMap[m.channelList.id] = { up: m.TopMenu.id, down: invalid, Left: invalid, Right: invalid }
    return focusMap
end function

function onNavigateAway()
    m.liveVideo.control = "stop"
    m.liveDataGrp.visible = false
    m.TopMenu.visible = false
    m.epgTimer.control = "stop"
end function

function showEpgData()
    m.liveDataGrp.visible = true
    m.epgTimer.control = "start"
    m.TopMenu.visible = true

    applyFocus(m.liveVideo, false, "onNavigateTo() - StreamController.brs")
    if not m.channelList.hasFocus()
        applyFocus(m.channelList, true, "showEpgData()")
    else
        applyFocus(m.TopMenu, true, "showEpgData()")
    end if
end function

sub hideEpgData()
    m.epgTimer.control = "stop"
    m.liveDataGrp.visible = false
    m.TopMenu.visible = false
    applyFocus(m.liveVideo, true, "onNavigateTo() - StreamController.brs")
    applyFocus(m.channelList, false, "onNavigateTo() - StreamController.brs")
end sub