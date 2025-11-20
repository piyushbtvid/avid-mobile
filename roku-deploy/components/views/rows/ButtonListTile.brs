function init()

    m.scaledElements = m.top.findNode("scaledElements")
    m.poster = m.top.findNode("poster")
    m.backRect = m.top.findNode("backRect")
  
    m.scalePercentage = 0.8
  
  end function
  
  '***** Handlers *****
  
  function onFocusChanged(event as object)
  
  
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
  
  function updateLayout(event as object)
  
    if (m.top.width > 0 and m.top.height > 0) then
  
      m.poster.width = m.top.width
      m.poster.height = m.top.height
      m.poster.loadWidth = m.top.width
      m.poster.loadHeight = m.top.height
  
    end if
  
  end function
  
  function onContentChanged(nodeEvent as object)
  
    content = nodeEvent.getData()
    ?"content " content
  
     if content <> invalid
        m.poster.uri = content.thumbnail_image
     end if

  
  end function
  
  