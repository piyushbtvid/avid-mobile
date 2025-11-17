function init()

  m.scaledElements = m.top.findNode("scaledElements")
  m.background = m.top.findNode("background")
  m.metadataGroup = m.top.findNode("metadataGroup")
  m.lblTitle = m.top.findNode("lblTitle")


  m.scalePercentage = 0.8

end function

'***** Handlers *****

function onFocusChanged( event as Object )

  if (not m.top.rowListHasFocus and not m.top.gridHasFocus) or m.top.rowFocusPercent = 0 then
      scalePercentage = m.scalePercentage
  else
      focusPercent = m.top.focusPercent
      if focusPercent > 1 and m.top.rowFocusPercent < 1 then
          focusPercent = m.top.rowFocusPercent
      end if
      scalePercentage = m.scalePercentage + ((1 - m.scalePercentage) * focusPercent)
  end if

end function

function updateLayout( event as Object )

  if ( m.top.width > 0 and m.top.height > 0 ) then

    m.background.width = m.top.width
    m.background.height = m.top.height
    m.background.loadWidth = m.top.width
    m.background.loadHeight = m.top.height

    m.metadataGroup.translation = [ 20, m.top.height ]

  end if

end function


function onContentChanged( nodeEvent as object )
  content = nodeEvent.getData()  
    feedContent = content.feed
    if feedContent <> invalid
      m.background.visible = true
      m.metadataGroup.visible = false
      if feedContent.title <> invalid then m.lblTitle.text = feedContent.title

      if isValid( feedContent.images )
          m.background.uri = feedContent.images.posterLandscape
          else
          m.background.uri = feedContent.uri
      end if
    else
      if content.movie_poster <> ""
         m.background.uri = content.movie_poster
    else if content.posterUrl <> ""
      m.background.uri = content.posterUrl
    else if content.thumbUrl <> ""
      m.background.uri = content.thumbUrl
    else
      m.background.uri = content.show_poster
    end if
  end if

end function

