function init()

  m.scaledElements = m.top.findNode("scaledElements")
  m.poster = m.top.findNode("poster")
  m.title = m.top.findNode("title")
  m.description = m.top.findNode("description")
  'm.totalDurationBar = m.top.findNode("totalDurationBar")
  m.viewsLbl = m.top.findNode("viewsLbl")
  m.monthLbl = m.top.findNode("monthLbl")
  m.duration = m.top.findNode("duration")
  m.scalePercentage = 0.8

  m.title.width = 0
  m.duration.width = 0
  m.viewsLbl.width = 0
  m.monthLbl.width = 0

  ' Initialize scaling properties
  m.scaledElements.scaleRotateCenter = [0.5, 0.5]
  m.scaledElements.translation = [0, 0]
  m.scalePercentage = 1.0
end function

function onFocusChanged(event as object)

  if (not m.top.rowListHasFocus and not m.top.gridHasFocus) or m.top.rowFocusPercent = 0 then
    scalePercentage = 1.0
  else
    focusPercent = m.top.focusPercent
    if focusPercent > 1 and m.top.rowFocusPercent < 1 then
      focusPercent = m.top.rowFocusPercent
    end if
    scalePercentage = 1.0 + (0.14 * focusPercent)
  end if

end function

function updateLayout(event as object)

  if (m.top.width > 0 and m.top.height > 0) then

    m.poster.width = m.top.width - 579
    m.poster.height = m.top.height
    m.poster.loadWidth = m.top.width - 579
    m.poster.loadHeight = m.top.height

  end if

end function

function onContentChanged(nodeEvent as object)

  content = nodeEvent.getData()

  if content <> invalid
    m.poster.uri = content.thumbnail_image
    m.title.text = content.title
    m.description.text = content.description
    m.viewsLbl.text = content.views + " Views"
    m.monthLbl.text = content.dateUploaded
    m.description.width = 550

    if isValid(content.duration) and content.duration.toInt() <> 0
      m.duration.text = FormatDuration(content.duration.toInt())
      m.title.width = 400 'm.title.boundingRect().width '- 1055
      
    else
      m.duration.text = ""
      m.title.width = 550
    end if

    viewsLblWidth = m.viewsLbl.boundingRect().width
    m.viewsLbl.width = viewsLblWidth

    monthLblWidth = m.monthLbl.boundingRect().width
    m.monthLbl.width = monthLblWidth

    durationWidth = m.duration.boundingRect().width
    m.duration.width = durationWidth

  end if

end function
