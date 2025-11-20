function init()

  m.top.id = "PlayerControlView"

  m.feedsList = m.top.findNode("feedsList")
  m.feedsList.observeField("rowItemFocused", "onRowItemFocused")
  m.feedsList.observeField("rowItemSelected", "onRowItemSelected")
  m.progressBar = m.top.findNode("progressBar")
  m.videoTitle = m.top.findNode("videoTitle")
  m.videoDuration = m.top.findNode("durationTextBottom")
  m.btnPlayPause = m.top.findNode("btnPlayPause")
  m.playerContainer = m.top.findNode("playerContainer")
  m.constants = GetConstants()
  m.scene = getScene()
  m.top.observeField("focusedChild", "onFocus")
  m.liveIcon = m.top.findNode("liveIcon")
  m.liveIcon.visible = false

  m.hideListTimer = m.top.findNode("hideListTimer")
  m.hideListTimer.ObserveField("fire", "hidePlayerContainer")

  m.btnPlayPause.observeField("buttonSelected", "onButtonSelected")


  m.playBtnConfig = m.constants.IMAGE_BUTTON_ICONS.PLAY_BUTTON
  m.pauseBtnConfig = m.constants.IMAGE_BUTTON_ICONS.PAUSE_BUTTON
  ?"m.playBtnConfig " m.playBtnConfig
  createPlayButton(m.playBtnConfig)
  'setStyle()

  m.scrollTimer = m.top.findNode("scrollTimer")
  m.scrollTimer.ObserveField("fire", "seekTo")

  m.scrollInProgress = false
  'onGetContentResponse()

end function

function onButtonSelected()
  if m.playerContainer.visible
    handlePlayPauseButtonState()
  end if
end function

function onRowItemSelected(event)

  selectedIndex = event.getData()

  if m.feedsList <> invalid and m.feedsList.content <> invalid
    row = m.feedsList.content.getChild(selectedIndex[0])
    if row <> invalid
      rowItem = row.getChild(selectedIndex[1])
      m.rowListSelectedIndex = selectedIndex
      videoContent = rowItem.video_detail

      if m.playerContainer.visible
        m.top.selectecVideoRowIndex = event.getData()
        ?"m.top.selectecVideoRowIndex " m.top.selectecVideoRowIndex
        'handlePlayNext()
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { content: videoContent, feedList: m.feedsList.content, videoIndex: m.rowListSelectedIndex })
        m.scene.loadController = pageInfo
      end if
    end if
  end if

end function

function onGetContentResponse()
  'response = event.getData()
  response = [
    [
      {
        "title": "Tech Insights",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views",
        "play_url": "https://iframe.mediadelivery.net/embed/341209/a7009698-1fc6-4b1b-94de-dbfcb098e3e5?autoplay=true&loop=false&muted=false&preload=true&responsive=true"
      },
      {
        "title": "Artistic Vibes",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views",
        "play_url": "https://iframe.mediadelivery.net/embed/341209/a7009698-1fc6-4b1b-94de-dbfcb098e3e5?autoplay=true&loop=false&muted=false&preload=true&responsive=true"
      },
      {
        "title": "Coding Corner",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Gaming Hub",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Food Adventures",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Travel Diaries",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Fitness Journey",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "145 k",
        "view": "300k Views"
      },
      {
        "title": "Music Moments",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "DIY Creations",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Science Snapshots",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Vlogging Ventures",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Crafty Ideas",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Tech Trends",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      }
    ],
    [
      {
        "title": "Tech Insights",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Artistic Vibes",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Coding Corner",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Gaming Hub",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Food Adventures",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Travel Diaries",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Fitness Journey",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "145 k",
        "view": "300k Views"
      },
      {
        "title": "Music Moments",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "DIY Creations",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Science Snapshots",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Vlogging Ventures",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Crafty Ideas",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      },
      {
        "title": "Tech Trends",
        "thumbnail_image": "pkg://images/m.jpg",
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod ",
        "view": "300k Views"
      }
  ]]

  if response <> invalid
    feedListContent = createObject("RoSGNode", "ContentNode")

     count = 0
    for each items in response
      feedListRow = feedListContent.createChild("ContentNode")
      feedListRow.title = "More Like This"
      count = count + 1
      for each item in items
        rowData = feedListRow.createChild("feedListItemDataModel")
        rowData.thumbnail_image = "https://posters.movieposterdb.com/22_10/2022/6765628/l_una-great-movie-movie-poster_c169bf1f.jpg"
        rowData.title = item.title
        rowData.video_url = item.play_url
        rowData.description = item.description
      end for
    end for

    m.feedsList.content = feedListContent
    m.feedsList.visible = true
    applyFocus(m.feedsList, true, " onFocus - PlayerControlView.brs", true)
    m.videoTitle.text = "video title" 'contentItem.title
  end if

end function

function onContentChange()

  ?"m.top.content  " m.top.content

  m.feedsList.content = m.top.content
  ?"m.feedsList.contentm.feedsList.content " m.feedsList.content
  m.feedsList.jumpToRowItem = m.top.selectecVideoRowIndex
  applyFocus(m.feedsList, true, " onFocus - PlayerControlView.brs", true)
  m.top.videoPlayer.observeField("state", "OnVideoPlayerStateChange")
end function

function OnVideoPlayerStateChange(event)

  if event.getData() = "finished"
    selectedRowIndex = m.top.selectecVideoRowIndex
    m.top.selectecVideoRowIndex = [selectedRowIndex[0], selectedRowIndex[1] + 1]
  end if

end function

function onRowItemFocused()

  fireTimer()

end function

function fireTimer()

  m.hideListTimer.control = "stop"
  m.playerContainer.visible = true
  m.hideListTimer.control = "start"

end function

function hidePlayerContainer()
  m.playerContainer.visible = false
  if not m.playerContainer.hasFocus()
    applyFocus(m.playerContainer, true, " onFocus - PlayerControlView.brs", true)
  end if
end function

function onVideoChangePos(event)

  videoCurrentPos = event.getData().videocurrentpos
  videoduration = event.getData().videoduration

  if m.liveIcon.visible = true
    m.videoDuration.text = "Live"
    m.progressBar.videoContent = invalid
  else
    if m.scrollInProgress = false
      m.progressBar.videoContent = event.getData()
    end if
    m.videoDuration.text = getRuntime(videoCurrentPos) + " / " + getRuntime(videoduration)
  end if

end function

function createPlayButton(imgConfig as object)
  m.btnPlayPause.imgSrc = imgConfig.imgSrc
  m.btnPlayPause.imgSize = imgConfig.imgSize
end function


function setTitleTime(contentItem as object)

  if isValid(contentItem)
    m.videoTitle.text = contentItem.title
    ' if contentItem.is_live_streaming
    '   m.liveIcon.visible = true
    '   m.videoDuration.text = "Live"
    ' else
    '   m.liveIcon.visible = false
    ' end if
  end if

end function

' function OnKeyEvent(key as string, press as boolean) as boolean

'   handled = false
'   focusMap = createFocusMap()
'   currentFocusComp = getCurrentFocusItem(m.scene)
'   if (press)

'     ' if m.PlayNextView.hasFocus() and key = "OK"
'     '   selectedRowIndex = m.top.selectecVideoRowIndex
'     '   m.top.selectecVideoRowIndex = [selectedRowIndex[0], selectedRowIndex[1] + 1]
'     '   handlePlayNext()
'     '   return true
'     ' end if
'     handled = componentFocusHandler(key, focusMap, currentFocusComp)
'     if not handled then handleVieoKeyEvents(key)
'     fireTimer()
'   end if

'   return handled


' end function

function OnKeyEvent(key as string, press as boolean) as boolean

  handled = false
  focusMap = createListFocusMap()
  currentFocusComp = getCurrentFocusItem(m.scene)
  if (press)

    if key = "back"
      if m.playerContainer.visible = true
        m.playerContainer.visible = false
        m.top.visible = false
        m.top.setFocus(false)
      else
        goBackInHistory(m.scene)
      end if
      handled = true

    else if key = "up"
      if m.feedsList.hasFocus()
        applyFocus(m.btnPlayPause, true, "OnKeyEvent - PlayerControlView.brs")
        handled = true
      else if m.btnPlayPause.hasFocus()
        applyFocus(m.progressBar, true, "OnKeyEvent - PlayerControlView.brs")
        handled = true
      end if
    else if key = "down"
      if m.progressBar.hasFocus()
        applyFocus(m.btnPlayPause, true, "OnKeyEvent - PlayerControlView.brs")
        handled = true
      else if m.feedsList.hasFocus()
        applyFocus(m.btnPlayPause, true, "OnKeyEvent - PlayerControlView.brs")
      end if
      ' else
      '   handled = handleVieoKeyEvents(key)
    end if
    if not handled then handleVieoKeyEvents(key)
    fireTimer()
  end if

  return handled


end function

' creates a focus map
' @return object focus map
function createListFocusMap()
  ?"igbbtoitotyoto7tbo78tbo87tbo8to8tob8tbo8bo8" m.top.selectecVideoRowIndex
  focusMap = {}

  focusMap[m.playerContainer.id] = { up: m.feedsList.id, down: m.feedsList.id, left: m.feedsList.id, right: m.feedsList.id }

  focusMap[m.feedsList.id] = { up: m.btnPlayPause.id, down: invalid, left: invalid, right: invalid }

  focusMap[m.btnPlayPause.id] = { up: m.progressBar.id, down: m.feedsList.id, left: invalid, right: invalid }

  focusMap[m.progressBar.id] = { up: invalid, down: m.btnPlayPause.id, left: invalid, right: invalid }


  return focusMap

end function

function handlePlayNext()

  selectedIndex = m.top.selectecVideoRowIndex
  feedList = m.feedsList.content
  row = feedList.getChild(selectedIndex[0])
  rowItem = row.getChild(selectedIndex[1])
  if isValid(rowItem)
    setTitleTime(rowItem)
    m.top.videoSelectedContent = rowItem
  end if
  nextVideoItem = row.getChild(selectedIndex[1] + 1)

  'showNextVideo(nextVideoItem)
  m.playerContainer.visible = true

end function

function handlePlayPauseButtonState()
  if m.top.videoPlayer.state = "playing"
    createPlayButton(m.pauseBtnConfig)
    m.top.videoPlayer.control = "pause"
  else
    createPlayButton(m.playBtnConfig)
    m.top.videoPlayer.control = "resume"
  end if

  applyFocus(m.btnPlayPause, true, " onFocus - PlayerControlView.brs", true)

end function

function handleVieoKeyEvents(key)

  if (key = "OK" or key = "down" or key = "up")
    m.playerContainer.visible = true
    applyFocus(m.feedslist, true, " onFocus - PlayerControlView.brs", true)
  end if

  if (key = "left" or key = "rewind") and not m.feedsList.hasFocus()

    m.lastKey = "left"
    m.progressBar.scrollInt = m.progressBar.progresWidth - 20
    m.scrollTimer.control = "stop"
    m.scrollTimer.control = "start"
    m.scrollInProgress = true
  end if

  if (key = "right" or key = "fastforward") and not m.feedsList.hasFocus()
    ' m.top.videoPlayer.seek = m.top.videoPlayer.position + 25
    m.lastKey = "right"
    m.scrollInProgress = true
    m.progressBar.scrollInt = m.progressBar.progresWidth + 20
    m.scrollTimer.control = "stop"
    m.scrollTimer.control = "start"

  end if

  if key = "play" or key = "pause"
    handlePlayPauseButtonState()
  end if

end function



function seekTo()

  value = m.top.videoPlayer.duration / m.progressBar.width
  coverwidth = value * m.progressBar.progresWidth
  m.top.videoPlayer.seek = coverwidth
  m.scrollInProgress = false
  m.scrollTimer.control = "stop"
  m.playerContainer.visible = true
  applyFocus(m.progressBar, true, " onFocus - PlayerControlView.brs", true)


end function
