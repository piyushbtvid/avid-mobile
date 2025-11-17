sub init()

  m.top.id = "LiveTvController"

  m.scene = getScene()
  m.constants = GetConstants()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.background = m.top.findNode("background")
  m.liveVideo = m.top.findNode("liveVideo")

  m.scene.observeFieldScoped("upNavEscape", "onUpNavFocusEscape")
  m.scene.observeFieldScoped("downNavEscape", "onDowntNavFocusEscape")
 
  m.TopMenu = m.scene.findNode("LiveTopMenu")
  m.TopMenu.visible = true
  m.TopMenu.findNode("topLiveMenu").jumpToRowItem = [0, 1]
  m.TopMenu.observeFieldScoped("rightNavEscape", "onRightNavFocusEscape")

  m.BrowseSelectTopMenu = m.scene.findNode("BrowseSelectTopMenu")
  m.BrowseSelectTopMenu.visible = false

  m.epgTimer = m.top.findNode("epgTimer")
  m.epgTimer.ObserveField("fire", "hideEpgData")

  m.leftmenu = m.scene.findNode("leftMenu")
  m.leftmenu.visible = false

end sub

sub playVideo(streamURL)
  m.liveVideo.control = "stop"
  ?"streamURLstreamURL", streamURL
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
    m.TopMenu.itemSelected = 1
  else
    m.TopMenu.itemSelected = 1
    m.liveVideo.visible = true
    playVideo("https://rcntv-rcnmas-1-us.roku.wurl.tv//playlist.m3u8")
  end if
  m.epgTimer.control = "start"
  applyFocus(m.TopMenu, true, "onNavigateTo() - LiveTvController.brs")

end function


function onUpNavFocusEscape(event as object) as void
  escapeStatus = event.getData()
  if escapeStatus = true
    OnKeyEvent(m.constants.remote_keys.UP, true)
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

    if m.TopMenu.visible = false
            showEpgData()
        end if

    if key = m.constants.remote_keys.BACK
      m.liveVideo.control = "stop"
      'goBackInHistory(m.scene)
      m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: { sectionID: 0 } }

      handled = true
    else if key = m.constants.remote_keys.LEFT
      handled = true
    else if key = m.constants.remote_keys.OK
      handled = true

    else if key = m.constants.remote_keys.RIGHT
      handled = true
    else if key = m.constants.remote_keys.DOWN
      handled = true
    end if

  end if
  return handled
end function
 

function onNavigateAway()
  m.liveVideo.control = "stop"
  m.TopMenu.visible = false
  m.epgTimer.control = "stop"
end function

sub hideEpgData()
  m.epgTimer.control = "stop"
  m.TopMenu.visible = false
  applyFocus(m.TopMenu, false, "onNavigateTo() - LiveTvController.brs")
  applyFocus(m.liveVideo, true, "onNavigateTo() - LiveTvController.brs")
end sub

function showEpgData()
  m.TopMenu.visible = true
  m.epgTimer.control = "start"
  applyFocus(m.liveVideo, false, "onNavigateTo() - LiveTvController.brs")
  applyFocus(m.TopMenu, true, "onNavigateTo() - LiveTvController.brs")
end function
