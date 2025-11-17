function init()

  m.scaledElements = m.top.findNode("scaledElements")
  m.background = m.top.findNode("background")
  m.backRect = m.top.findNode("backRect")


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


  end if

end function

function onContentChanged( nodeEvent as object )

      content = nodeEvent.getData()
      m.background.uri = content.posterUrl

end function

