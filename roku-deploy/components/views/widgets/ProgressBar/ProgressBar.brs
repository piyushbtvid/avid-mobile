function init()
  m.top.id = "ProgressBar"
  m.top.observeField( "focusedChild", "onFocusChange" )
  m.markerBall = m.top.findNode("markerBall")
  m.defaultProgressBar = m.top.findNode("defaultProgressBar")
  m.progressBar = m.top.findNode("progressBar")
end function

function onFocusChange( event as object )

  if ( m.top.hasFocus() )
    m.markerBall.uri = "pkg:/images/timeline_circle_on.png"
    m.defaultProgressBar.height = 6
  else
    m.markerBall.uri = "pkg:/images/timeline_circle_off.png"
    m.defaultProgressBar.height = 6
  end if

end function

function onVideoChangePos(event)
  
  if isValid( event.getData() )

    videoInfo = event.getData()
    videoCurrentPos = videoInfo.videocurrentpos
    videoduration = videoInfo.videoduration
    value = 1650 / videoduration
    value=videoCurrentPos * value
    m.progressBar.progresWidth = value
    m.markerBall.translation=[ value , -10 ]

  end if

end function


function scrollMarker()

  m.progressBar.progresWidth = m.top.scrollInt
  m.markerBall.translation = [ m.top.scrollInt , -10 ]

end function
