function init()

  m.scaledElements = m.top.findNode("scaledElements")
  m.background = m.top.findNode("background")
  m.backRect = m.top.findNode("backRect")
  m.totalDurationBar = m.top.findNode("totalDurationBar")
  m.progressBar = m.top.findNode("progressBar")
  ' m.gradient = m.top.findNode("gradient")
  ' m.itemTitle = m.top.findNode("itemTitle")
  ' m.expirationTag = m.top.findNode("expirationTag")


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
  ' if m.top.focusPercent > 0.7 and (m.top.gridHasFocus)
  '   m.gradient.visible = true
  '   m.itemTitle.visible = true
  ' else
  '   m.gradient.visible = false
  '   m.itemTitle.visible = false
  ' end if

  ' if m.itemTitle.numWrappedLines = 1
  '   m.itemTitle.translation = [10, 180]
  ' else
  '   m.itemTitle.translation = [10, 150]
  ' end if

end function

function updateLayout(event as object)

  if (m.top.width > 0 and m.top.height > 0) then

    m.background.width = m.top.width
    m.background.height = m.top.height
    m.background.loadWidth = m.top.width
    m.background.loadHeight = m.top.height
    m.backRect.width = m.top.width
    m.backRect.height = m.top.height
    m.totalDurationBar.width = m.top.width

  end if

end function

function onContentChanged(nodeEvent as object)

  content = nodeEvent.getData()
  if content.media <> invalid
    if content.media.thumbnailImage <> invalid and content.media.thumbnailImage.len() > 0
      m.background.uri = content.media.thumbnailImage
    else if content.media.posterImage <> invalid and content.media.posterImage.len() > 0
      m.background.uri = content.media.posterImage
    end if

    m.progressBar.width = calculateProgressDuration(content)
    m.totalDurationBar.visible = true
    m.progressBar.visible = true
  end if

  if content.thumbnail_image <> invalid and content.thumbnail_image.len() > 0
    m.background.uri = content.thumbnail_image
  else if content.poster_image <> invalid and content.poster_image.len() > 0
    m.background.uri = content.poster_image
  end if

end function

function calculateProgressDuration(content)

  progress_seconds = content.progress_seconds / 1000
  totalduration = content.media.duration.toInt()
  totalWidth = m.top.width

  if totalDuration <= 0 then
    return 0
  end if

  progressWidth = (progress_seconds / totalDuration) * totalWidth

  return progressWidth

end function