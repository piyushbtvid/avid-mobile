function init()

  m.scaledElements = m.top.findNode("scaledElements")
  m.background = m.top.findNode("background")
  m.backRect = m.top.findNode("backRect")
  m.totalDurationBar = m.top.findNode("totalDurationBar")
  m.progressBar = m.top.findNode("progressBar")
  m.lblTitle = m.top.findNode("lblTitle")
  m.ratingName = m.top.findNode("ratingName")
  m.uploadedYear = m.top.findNode("uploadedYear")
  m.metadataGroup = m.top.findNode("metadataGroup")

  ' Initialize scaling properties
  m.scaledElements.scaleRotateCenter = [0.5, 0.5]
  m.scaledElements.translation = [0, 0]
  m.scalePercentage = 1.0

end function

'***** Handlers *****

function onFocusChanged(event as object)

 
  if (not m.top.rowListHasFocus and not m.top.gridHasFocus) or m.top.rowFocusPercent = 0 then
    scalePercentage = 1.0
  else
    
    focusPercent = m.top.focusPercent
    if focusPercent > 1 and m.top.rowFocusPercent < 1 then
      focusPercent = m.top.rowFocusPercent
      m.background.height = m.top.height - 50
      m.background.loadHeight = m.top.height - 50
      m.backRect.height = m.top.height - 50
      m.backRect.width = m.top.width'-30
      m.background.width = m.top.width'-30
      m.background.loadWidth = m.top.width'-30
    end if
    scalePercentage = 1.0 + (0.10 * focusPercent)
  end if


  ' Apply scaling transformation
  m.scaledElements.scale = [scalePercentage, scalePercentage]

  ' Adjust translation to keep item centered during scaling
  itemWidth = m.top.width
  itemHeight = m.top.height
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

    m.metadataGroup.translation = [30, m.background.height + 60]

  end if

end function

function onContentChanged(nodeEvent as object)

  content = nodeEvent.getData()

  if content.content_type <> invalid and content.content_type = "Live Channel"
    m.background.loadDisplayMode = "scaleToFit"
  else
    m.backRect.width = m.top.width - 5
    m.backRect.height = m.top.height - 5
    m.backRect.translation = [35, 55]
    m.background.loadDisplayMode = "limitSize"
  end if

  'if content <> invalid and content.creator <> invalid
  if content.thumbnail_image <> invalid and content.thumbnail_image.len() > 0

    if content.row_type = "video_row"
      m.background.uri = content.poster_image
    else
      m.background.uri = content.thumbnail_image
    end if
    m.lblTitle.text = content.title
    if content.rating <> invalid
      m.ratingName.text = content.rating 'creator.name
    end if
    if content.uploadedYear <> invalid
      m.uploadedYear.text = content.uploadedYear
    else
      m.uploadedYear.text = content.views + " Views"
    end if

  else if content.poster_image <> invalid and content.poster_image.len() > 0
    m.background.uri = content.poster_image
    m.lblTitle.text = content.title
    m.ratingName.text = content.creator.name
    m.uploadedYear.text = content.views + " Views"
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
