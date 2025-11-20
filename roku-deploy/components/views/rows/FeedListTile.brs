sub init()
  m.scaledElements = m.top.findNode("scaledElements")
  m.backRect = m.top.findNode("backRect")
  m.background = m.top.findNode("background")
  m.totalDurationBar = m.top.findNode("totalDurationBar")
  m.progressBar = m.top.findNode("progressBar")

  ' Initialize scaling properties
  m.scaledElements.scaleRotateCenter = [0.5, 0.5]
  m.scaledElements.translation = [0, 0]
  m.scalePercentage = 1.0
end sub

sub onFocusChanged()
  if (not m.top.rowListHasFocus and not m.top.gridHasFocus) or m.top.rowFocusPercent = 0 then
    scalePercentage = 1.0
  else
    focusPercent = m.top.focusPercent
    if focusPercent > 1 and m.top.rowFocusPercent < 1 then
      focusPercent = m.top.rowFocusPercent
    end if
    
    scalePercentage = 1.0 + (0.10 * focusPercent)
  end if
 
  ' Apply scaling transformation
  m.scaledElements.scale = [scalePercentage, scalePercentage]

  ' Adjust translation to keep item centered during scaling
  itemWidth = m.backRect.width
  itemHeight = m.backRect.height
  m.scaledElements.translation = [
    (1 - scalePercentage) * itemWidth / 2,
    (1 - scalePercentage) * itemHeight / 2
  ]

  ' Bring to front when focused
  if scalePercentage > 1.0 then
    parent = m.scaledElements.getParent()
    if parent.getChildCount() > 1 then
      parent.removeChild(m.scaledElements)
      parent.appendChild(m.scaledElements)
    end if
  end if
end sub


function updateLayout(event as object)
  if (m.top.width > 0 and m.top.height > 0) then
    if m.originalWidth = invalid or m.originalWidth = 0 then
      m.originalWidth = m.top.width
      m.originalHeight = m.top.height
    end if

    updateLayoutWithDimensions(m.originalWidth, m.originalHeight)
  end if
end function

function updateLayoutWithDimensions(width as float, height as float)
  m.background.width = width
  m.background.height = height 
  m.background.loadWidth = width
  m.background.loadHeight = height
  m.backRect.width = width
  m.backRect.height = height
  m.totalDurationBar.width = width
end function

function onContentChanged(nodeEvent as object)

  content = nodeEvent.getData()

  if content.content_type <> invalid and content.content_type = "Live Channel"
    m.background.loadDisplayMode = "scaleToFit"
  else
    m.backRect.width = m.originalWidth - 5
    m.backRect.height = m.originalHeight - 5
    m.backRect.translation = [5,55]
    m.background.loadDisplayMode = "limitSize"
  end if

  if content.thumbnail_image <> invalid and content.thumbnail_image.len() > 0
    m.background.uri = content.thumbnail_image
  else if content.poster_image <> invalid and content.poster_image.len() > 0
    m.background.uri = content.poster_image
  end if

end function

' function calculateProgressDuration(content)

'   progress_seconds = content.progress_seconds / 1000
'   totalduration = content.media.duration.toInt()
'   totalWidth = m.top.width

'   if totalDuration <= 0 then
'     return 0
'   end if

'   progressWidth = (progress_seconds / totalDuration) * totalWidth

'   return progressWidth

' end function
