sub init()
  m.top.id = "GuideController"

  m.scene = getScene()
  m.constants = GetConstants()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.background = m.top.findNode("background")
  m.channelList = m.top.findNode("channelList")


  font = CreateObject("roSGNode", "Font")
  font.uri = "pkg://images/font/Montserrat-Regular.ttf"
  font.size = 20

  mediumFont = CreateObject("roSGNode", "Font")
  mediumFont.uri = "pkg://images/font/Montserrat-Bold.ttf"
  mediumFont.size = 20

  m.channelList.observeField("programSelected", "onChannelSelected")
  m.channelList.observeField("programFocused", "programFocused")
  m.channelList.timeBarBitmapUri = "pkg://images/timePostercc.png"
  m.channelList.timeLabelColor = "#FFFFFF"
  m.channelList.programBackgroundBitmapUri = ""
  m.channelList.timeLabelFont = font

  m.channelList.fillProgramGaps = 50
  m.channelList.programHorizMargin = 50
  m.channelList.programTitleColor = "#FFFFFF"
  m.channelList.programTitleFont = mediumFont

  currentTime = CreateObject("roDateTime") ' roDateTime is initialized
  ' to the current time
  t = currentTime.AsSeconds()
  t = t - (t mod 1800) ' RDE-2665 - TimeGrid works best when contentStartTime is set to a 30m mark
  m.channelList.contentStartTime = t
  m.channelList.leftEdgeTargetTime = currentTime.AsSeconds()
  m.channelList.channelNoDataText = "Loading..."
  m.channelList.loadingDataText = "Loading..."
  m.channelList.automaticLoadingDataFeedback = false
  m.channelList.numRows = 7
  m.channelList.programTitleFocusedColor = "#000000"
  m.channelList.programBackgroundBitmapUri = "pkg://images/liveListBack.png"
  m.channelList.channelInfoBackgroundBitmapUri = "pkg://images/channe_back_poster.png"

  m.upArrow = m.top.findNode("upArrow")
  m.upArrow.width = 220
  m.upArrow.height = 40
  m.upArrow.translation = [(1920 - m.upArrow.width) / 2, 0]

  m.title = m.top.findNode("title")
  m.description = m.top.findNode("description")

  m.scene.observeFieldScoped("upNavEscape", "onUpNavFocusEscape")
  m.scene.observeFieldScoped("downNavEscape", "onDownNavFocusEscape")

  m.leftmenu = m.scene.findNode("leftMenu")
  m.leftmenu.visible = false

  m.TopMenu = m.scene.findNode("LiveTopMenu")
  'm.TopMenu.findNode("topLiveMenu").jumpToRowItem = [0, 2]
  m.TopMenu.visible = false

  m.BrowseSelectTopMenu = m.scene.findNode("BrowseSelectTopMenu")
  m.BrowseSelectTopMenu.findNode("browseTopLiveMenu").jumpToRowItem = [0, 1]
  m.BrowseSelectTopMenu.visible = true

  m.BrowseSelectTopMenu.observeFieldScoped("downTopNavEscape", "onDowntTopNavFocusEscape")

  m.liveVideo = m.top.findNode("liveVideo")


  m.categoryList = m.top.findNode("categoryList")
  m.categoryList.observeField("rowItemFocused", "onCategoryListFocus")
  m.categoryList.observeField("rowItemSelected", "onButtonItemSelected")

  m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")
end sub

function onNavigateTo(params as object)
  loadLiveTVList()
  applyFocus(m.BrowseSelectTopMenu, true, "onNavigateTo() - LiveTvController.brs")
end function

function onGetCategoryResponse(event)
  response = event

  if isValid(response)
    feedListContent = createObject("RoSGNode", "ContentNode")
    row = feedListContent.createChild("ContentNode")

    for each item in response
      itemNode = row.createChild("navigationViewModel")
      itemNode.addField("FHDItemWidth", "float", false)
      itemNode.title = item.name
      ' itemNode.type = item.slug
      ' itemNode.sectionId = item.id.toStr()

      textLength = item.name.len()
      if textLength <= 4
        baseWidth = 200
      else if textLength <= 6
        baseWidth = 230
      else if textLength <= 8
        baseWidth = 260
      else if textLength <= 10
        baseWidth = 320
      else
        baseWidth = 369
      end if

      itemNode.FHDItemWidth = baseWidth
    end for
    m.categoryList.content = feedListContent
    m.categoryList.visible = true
  end if
end function

sub loadLiveTVList()
  startSpinner(m.top)
  login_Token = LocalStorage().getValueForKey("login_Token")
  creatorPageContent = TVService(m.apiProvider).getLiveChannelContent(login_Token)
  creatorPageContent.httpResponse.observeField("response", "onMoviesPageResponse")
  m.http.sendRequest(creatorPageContent)
end sub

sub onMoviesPageResponse(event)

  m.videoStreamList = []
  response = event.getData().data
  feedList = createObject("RoSGNode", "ContentNode")
  if isValid(response) and isValid(response.data)
    for each item in response.data
      m.videoStreamList.push(item.video_link)
    end for
  end if
  fetchEPGData()
end sub

sub fetchEPGData()
  epgContent = TVService(m.apiProvider).getGuideStreamContent()
  epgContent.httpResponse.observeField("response", "onGetEPGResponse")
  m.http.sendRequest(epgContent)
end sub

function onGetEPGResponse(event)
  response = event.getData()
  content = CreateObject("roSGNode", "ContentNode")
  stopSpinner()

  onGetCategoryResponse(response.data.data.response.categories)

  for each category in response.data.data.response.categories
    for each channelData in category.stream_channels
      ' Create a row for each channel
      channelRow = content.createChild("ContentNode")
      channelRow.title = channelData.name
      channelRow.HDGRIDPOSTERURL = channelData.thumbnail
      channelRow.channelId = channelData.id.toStr()
      channelRow.channelNumber = channelData.number.toStr()

      dt = CreateObject("roDateTime")
      now = dt.AsSeconds()
      playStart = now - (now mod 1800)
      ' Add programs as children of the channel row
      for each program in channelData.broadcasts
        programNode = channelRow.createChild("ContentNode")
        programNode.title = program.title
        programNode.description = program.description
        programNode.ShortDescriptionLine1 = channelData.source_url

        dt.FromISO8601String((Left(program.view_start_at_iso, 19) + ".000").Replace("T", " "))
        program_start = dt.AsSeconds()

        programNode.playStart = program_start 'program.view_start_at_iso
        programNode.playDuration = program.view_duration

        playstart += programNode.playDuration


      end for
    end for
  end for

  ' Set the TimeGrid's start and end times
  if content.getChildCount() > 0 and content.getChild(0).getChildCount() > 0
    firstProgram = content.getChild(0).getChild(0)
    lastProgram = content.getChild(content.getChildCount() - 1).getChild(content.getChild(content.getChildCount() - 1).getChildCount() - 1)

    m.channelList.startTime = firstProgram.programStartTime
    m.channelList.endTime = lastProgram.programEndTime
  end if

  m.channelList.content = content
  program = m.channelList.content.getChild(0).getChild(0)
  m.title.text = program.title
  m.description.text = program.description
  'playVideo(program.shortdescriptionline1)
  playVideo("https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8")


  m.liveVideo.visible = true
  m.channelList.visible = true

end function

function formatTime(time as object, useUTC = false as boolean) as string
  if useUTC
    time.ToUTC() ' Force UTC time
  else
    time.ToLocalTime() ' Use local time
  end if

  hours = time.GetHours()
  minutes = time.GetMinutes()
  ampm = "AM"

  if hours >= 12
    ampm = "PM"
    if hours > 12
      hours = hours - 12
    end if
  else if hours = 0
    hours = 12
  end if

  minutesStr = minutes.ToStr()
  if minutesStr.Len() = 1
    minutesStr = "0" + minutesStr
  end if

  return hours.ToStr() + ":" + minutesStr + " " + ampm
end function

sub playVideo(streamURL)

  videoNumber = Rnd(m.videoStreamList.Count() - 1)

  if m.liveVideo.state <> invalid and (m.liveVideo.state = "buffering" or m.liveVideo.state = "playing")
    m.liveVideo.control = "stop"
    m.liveVideo.unObserveField("state")
  end if
  m.liveVideo.observeField("state", "OnVideoPlayerStateChange")
  videoContent = createObject("RoSGNode", "ContentNode")
  videoContent.streamformat = "auto"
  videoContent.url = m.videoStreamList[videoNumber]
  m.liveVideo.content = videoContent
  m.liveVideo.control = "play"
  m.liveVideo.visible = true
end sub

function OnVideoPlayerStateChange()
  ?"m.liveVideom.liveVideo", m.liveVideo.state
  ?"m.liveVideom.liveVideo", m.liveVideo.errorCode
  ?"m.liveVideom.liveVideo", m.liveVideo.errorMsg
  ?"m.liveVideom.liveVideo", m.liveVideo.errorStr
  ?"m.liveVideom.liveVideo", m.liveVideo.errorInfo
end function

function programFocused(event as object)
  if m.channelList <> invalid
    ChannelProgramFocused(m.channelList.channelFocused, m.channelList.programFocused)
  end if
end function


sub ChannelProgramFocused(currentRowIndex as integer, currentItemIndex as integer)
  row = invalid
  if m.channelList.content <> invalid then
    row = m.channelList.content.GetChild(currentRowIndex)
  end if
  if row <> invalid
    focusIndexToSet = currentItemIndex
    if currentItemIndex < 0 then currentItemIndex = 0
    UpdateItemDetails(currentRowIndex, currentItemIndex)

  end if
  m.previousFocusedRow = currentRowIndex
  m.previousFocusedItemIndex = currentItemIndex
end sub

sub UpdateItemDetails(channelIndex, programIndex)
  content = m.channelList.content
  if content = invalid then return
  channel = content.GetChild(channelIndex)
  if channel = invalid then return
  program = channel.GetChild(programIndex)
  m.title.text = program.title
  m.description.text = program.description
  'playVideo(program.shortdescriptionline1)
  playVideo("https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8")

end sub


sub onChannelSelected()
  selectedIndex = m.channelList.itemSelected
  if selectedIndex >= 0
    selectedItem = m.channelList.content.getChild(selectedIndex)

    ' Check if we selected a program or channel
    if selectedItem.isChannel = false
      ' Show program details
      ' m.programDetails.findNode("programTitle").text = selectedItem.title
      ' m.programDetails.findNode("programTime").text = selectedItem.timeSlot
      ' m.programDetails.findNode("programDescription").text = selectedItem.description
      ' m.programDetails.visible = true
    else
      ' Play the channel
      playVideo("https://example.com/stream/" + selectedItem.channelId + ".m3u8")
    end if
  end if
end sub

function onUpNavFocusEscape(event as object) as void
  escapeStatus = event.getData()
  if escapeStatus = true
    OnKeyEvent(m.constants.remote_keys.UP, true)
  end if
end function

function onDownNavFocusEscape(event as object) as void
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

function onLeftNavFocusEscape(event as object) as void
  escapeStatus = event.getData()
  if escapeStatus = true
    OnKeyEvent(m.constants.remote_keys.RIGHT, true)
  end if
end function

function OnKeyEvent(key as string, press as boolean) as boolean

  handled = false

  if (press)
    focusMap = createFocusMap()
    currentFocusMap = getCurrentFocusItem(m.scene)
    if key = m.constants.remote_keys.BACK
      m.scene.loadController = { page: m.constants.CONTROLLERS.STREAM, params: {} }
      handled = true

    else if key = m.constants.remote_keys.DOWN
      if m.categoryList.hasFocus()
        applyFocus(m.channelList, true, "onNavigateTo() - LiveTvController.brs")
        handled = true
      else
        applyFocus(m.categoryList, true, "onNavigateTo() - LiveTvController.brs")
        handled = true
      end if

    else if key = m.constants.remote_keys.LEFT

      handled = true
    else
      handled = componentFocusHandler(key, focusMap, currentFocusMap)
    end if

  end if
  return handled
end function

function createFocusMap()
  focusMap = {}
  'focusMap[m.leftmenu.id] = { up: invalid, down: invalid, Left: invalid, Right: m.categoryList.id }
  focusMap[m.BrowseSelectTopMenu.id] = { up: invalid, down: m.categoryList.id, Left: invalid, Right: invalid }
  focusMap[m.categoryList.id] = { up: m.BrowseSelectTopMenu.id, down: m.channelList.id, Left: invalid, Right: invalid }
  focusMap[m.channelList.id] = { up: m.categoryList.id, down: invalid, Left: invalid, Right: invalid }
  return focusMap
end function