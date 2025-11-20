sub init()
    m.top.id = "LiveTvStreamController"

    m.scene = getScene()
    m.constants = GetConstants()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.background = m.top.findNode("background")
    m.channelList = m.top.findNode("channelList")
    m.liveVideo = m.top.findNode("liveVideo")
    m.liveDataGrp = m.top.findNode("liveDataGrp")
    m.liveDataGrp.visible = true
    m.epgTimer = m.top.findNode("epgTimer")
    m.epgTimer.ObserveField("fire", "hideEpgData")

    m.scene.observeFieldScoped("upNavEscape", "onUpNavFocusEscape")
    m.scene.observeFieldScoped("downNavEscape", "onDowntNavFocusEscape")

    m.channelList.observeField("rowItemSelected", "onChannelSelected")
    m.channelList.observeField("rowItemFocused", "onChannelFocused")

    m.TopMenu = m.scene.findNode("LiveTopMenu")
    m.TopMenu.visible = false
    applyFocus(m.TopMenu, false, "onNavigateTo() - BrowserController.brs")

    m.BrowseSelectTopMenu = m.scene.findNode("BrowseSelectTopMenu")
    m.BrowseSelectTopMenu.visible = true

    m.BrowseSelectTopMenu.findNode("browseTopLiveMenu").jumpToRowItem = [0, 0]
    m.BrowseSelectTopMenu.observeFieldScoped("downTopNavEscape", "onDowntTopNavFocusEscape")
    m.BrowseSelectTopMenu.observeFieldScoped("rightNavEscape", "onRightNavFocusEscape")

    m.leftmenu = m.scene.findNode("leftMenu")
    m.leftmenu.visible = false

    m.upArrow = m.top.findNode("upArrow")
    m.upArrow.width = 220
    m.upArrow.height = 40
    m.upArrow.translation = [(1920 - m.upArrow.width) / 2, 0]
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
    if params.content <> invalid and params.content.video_detail <> invalid
        playVideo(params.content.video_detail.play_url)
        m.liveVideo.visible = true
        m.BrowseSelectTopMenu.itemSelected = 0
    else
        m.BrowseSelectTopMenu.itemSelected = 0
        m.liveVideo.visible = true
        playVideo("https://rcntv-rcnmas-1-us.roku.wurl.tv//playlist.m3u8")
    end if

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
    if m.BrowseSelectTopMenu <> invalid
        applyFocus(m.BrowseSelectTopMenu, true, "setInitialFocus() - BrowserController.brs")
    end if
    
    ' Ensure we don't start on channel column
    if m.channelList.content <> invalid and m.channelList.content.getChildCount() > 0
        m.channelList.jumpToRowItem = [0, 1]
    end if
    
    if m.focusTimer <> invalid
        m.focusTimer.control = "stop"
        m.top.removeChild(m.focusTimer)
    end if
end sub

function setupEPGGrid()
    epgData = [
        {
            channel: { icon: "pkg://images/channel_Icon.png", name: "ABC", type: "channel" },
            programs: [
                { title: "Betty King- The Cry", time: "8:00AM - 9:00AM", duration: "60min" },
            ]
        },
        {
            channel: { icon: "pkg://images/1.png", name: "NBC", type: "channel" },
            programs: [
                { title: "Today Show", time: "8:00AM - 10:00AM", duration: "120min" }
            ]
        },
        {
            channel: { icon: "pkg://images/2.png", name: "CBS", type: "channel" },
            programs: [
                { title: "CBS Morning", time: "8:00AM - 9:30AM", duration: "90min" },
            ]
        },
        {
            channel: { icon: "pkg://images/3.png", name: "FOX", type: "channel" },
            programs: [
                { title: "Fox & Friends", time: "8:00AM - 11:00AM", duration: "180min" }
            ]
        },
        {
            channel: { icon: "pkg://images/4.png", name: "CNN", type: "channel" },
            programs: [
                { title: "CNN Newsroom", time: "8:00AM - 12:00PM", duration: "240min" }
            ]
        }
    ]

    content = CreateObject("roSGNode", "ContentNode")

    for each channelData in epgData
        row = content.createChild("ContentNode")

        ' Channel item (column 0) - set focusable to false
        channelItem = row.createChild("feedEpgDataModel")
        channelItem.title = channelData.channel.name
        channelItem.icon = channelData.channel.icon
        channelItem.type = channelData.channel.type
        channelItem.isChannel = true
        channelItem.focusable = false ' This makes it non-focusable
        channelItem.timeSlot = ""
        channelItem.duration = ""

        ' Program items (columns 1+) - set focusable to true
        for each program in channelData.programs
            programItem = row.createChild("feedEpgDataModel")
            programItem.title = program.title
            programItem.timeSlot = program.time
            programItem.duration = program.duration
            programItem.isChannel = false
            programItem.focusable = true ' This makes it focusable
            programItem.type = "program"
        end for
    end for

    m.channelList.content = content
    m.channelList.numRows = content.getChildCount()
    
    ' Start focus on first program item (row 0, column 1)
    if content.getChildCount() > 0 and content.getChild(0).getChildCount() > 1
        m.channelList.jumpToRowItem = [0, 1]
    end if
end function

sub onChannelFocused(event as object)
    focusedIndex = event.getData()
    if focusedIndex <> invalid and focusedIndex.count() = 2
        row = m.channelList.content.getChild(focusedIndex[0])
        if row <> invalid
            m.epgTimer.control = "start"
            rowItem = row.getChild(focusedIndex[1])
            if rowItem <> invalid and (rowItem.type = "channel" or focusedIndex[1] = 0)
                ' Move focus to the first program item in the same row
                if row.getChildCount() > 1
                    m.channelList.jumpToRowItem = [focusedIndex[0], 1]
                else if m.channelList.numRows > focusedIndex[0] + 1
                    ' Move to next row if current row has no programs
                    m.channelList.jumpToRowItem = [focusedIndex[0] + 1, 1]
                end if
            end if
        end if
    end if
end sub

sub onChannelSelected(event as object)
    selectedIndex = event.getData()
    if selectedIndex <> invalid and selectedIndex.count() = 2
        row = m.channelList.content.getChild(selectedIndex[0])
        if row <> invalid
            rowItem = row.getChild(selectedIndex[1])
            if rowItem <> invalid and rowItem.type <> "channel"
                playVideo("https://rcntv-rcnmas-1-us.roku.wurl.tv//playlist.m3u8")
            end if
        end if
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

function OnKeyEvent(key as string, press as boolean) as boolean
    handled = false

    if (press)
        if m.liveDataGrp.visible = false
            showEpgData()
        end if

        focusMap = createFocusMap()
        currentFocusMap = getCurrentFocusItem(m.scene)

        if key = m.constants.remote_keys.BACK
            m.liveVideo.control = "stop"
            m.scene.loadController = { page: m.constants.CONTROLLERS.STREAM, params: {} }
            handled = true
            
        else if key = m.constants.remote_keys.DOWN
            applyFocus(m.channelList, true, "onNavigateTo() - BrowserController.brs")
            handled = true
            
        else if key = m.constants.remote_keys.LEFT
            ' Prevent moving to channel column when pressing left
            currentFocus = m.channelList.rowItemFocused
            if currentFocus <> invalid and currentFocus.count() = 2 and currentFocus[1] = 1
                handled = true ' Block left movement to channel column
            else
                handled = componentFocusHandler(key, focusMap, currentFocusMap)
            end if
            
        else
            handled = componentFocusHandler(key, focusMap, currentFocusMap)
        end if
        
        ' After any key event, check if we landed on a channel item
        if handled and m.channelList.hasFocus()
            preventChannelFocus()
        end if
    end if
    
    return handled
end function

function preventChannelFocus() as boolean
    currentFocus = m.channelList.rowItemFocused
    if currentFocus <> invalid and currentFocus.count() = 2
        row = m.channelList.content.getChild(currentFocus[0])
        if row <> invalid and row.getChildCount() > 0
            rowItem = row.getChild(currentFocus[1])
            if rowItem <> invalid and rowItem.type = "channel"
                ' Move focus to next available program item
                if row.getChildCount() > 1
                    m.channelList.jumpToRowItem = [currentFocus[0], 1]
                else if m.channelList.numRows > currentFocus[0] + 1
                    ' Move to next row if current row has no programs
                    m.channelList.jumpToRowItem = [currentFocus[0] + 1, 1]
                else if m.channelList.numRows > 0
                    ' Move to previous row if at the end
                    m.channelList.jumpToRowItem = [currentFocus[0] - 1, 1]
                end if
                return true
            end if
        end if
    end if
    return false
end function

function createFocusMap()
    focusMap = {}
    focusMap[m.BrowseSelectTopMenu.id] = { up: invalid, down: m.channelList.id, Left: invalid, Right: invalid }
    focusMap[m.channelList.id] = { up: m.BrowseSelectTopMenu.id, down: invalid, Left: invalid, Right: invalid }
    return focusMap
end function

function onNavigateAway()
    m.liveVideo.control = "stop"
    m.liveDataGrp.visible = false
    m.BrowseSelectTopMenu.visible = false
    m.epgTimer.control = "stop"
end function

function showEpgData()
    m.liveDataGrp.visible = true
    m.epgTimer.control = "start"
    m.BrowseSelectTopMenu.visible = true

    applyFocus(m.liveVideo, false, "onNavigateTo() - BrowserController.brs")
    applyFocus(m.channelList, true, "onNavigateTo() - BrowserController.brs")
end function

sub hideEpgData()
    m.epgTimer.control = "stop"
    m.liveDataGrp.visible = false
    m.BrowseSelectTopMenu.visible = false
    applyFocus(m.liveVideo, true, "onNavigateTo() - BrowserController.brs")
    applyFocus(m.channelList, false, "onNavigateTo() - BrowserController.brs")
end sub