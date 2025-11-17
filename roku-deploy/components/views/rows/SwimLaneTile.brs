function init()

  m.scaledElements = m.top.findNode("scaledElements")
  m.background = m.top.findNode("background")
  m.metadataGroup = m.top.findNode("metadataGroup")
  m.lblTitle = m.top.findNode("lblTitle")


  m.scalePercentage = 0.8
  ' m.scaledElements.scale = [m.scalePercentage, m.scalePercentage]

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
  ' m.scaledElements.scale = [scalePercentage, scalePercentage]

end function

function updateLayout( event as Object )

  if ( m.top.width > 0 and m.top.height > 0 ) then

    m.background.width = m.top.width
    m.background.height = m.top.height
    m.background.loadWidth = m.top.width
    m.background.loadHeight = m.top.height

    m.metadataGroup.translation = [ 20, m.top.height + 10 ]
    ' m.scaledElements.scaleRotateCenter = [m.top.width / 2, m.top.height / 2]

  end if

end function


function onContentChanged( nodeEvent as object )

  content = nodeEvent.getData()

      m.background.visible = true
      m.metadataGroup.visible = true
      if content.title <> invalid AND content.title.len() > 0
        m.lblTitle.text = content.title
      else if  content.seoTitle <> invalid
        m.lblTitle.text = content.seoTitle
      end if 

      if content.thumbnail_image <> invalid and content.thumbnail_image.len() > 0
        m.background.uri = content.thumbnail_image
      else if content.poster_image <> invalid and content.poster_image.len() > 0
        m.background.uri = content.poster_image
      end if

end function

