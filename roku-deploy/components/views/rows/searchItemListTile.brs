function init()

  m.scaledElements = m.top.findNode("scaledElements")
  m.poster = m.top.findNode("poster")

  m.viewsLbl = m.top.findNode("viewsLbl")
  'm.monthLbl = m.top.findNode("monthLbl")

  m.ratingText = m.top.findNode("ratingText")
  m.genreText = m.top.findNode("genreText")

  m.titleLayoutGrp = m.top.findNode("titleLayoutGrp")
  m.scalePercentage = 0.8

  m.borderRatingBack = m.top.findNode("borderRatingBack")
  m.ratingLabel = m.top.findNode("ratingLabel")
  m.durationLbel = m.top.findNode("duration")
  m.title = m.top.findNode("title")
  m.ratingBack = m.top.findNode("ratingBack")
  m.titleLayoutGrp = m.top.findNode("titleLayoutGrp")

  m.viewsLbl.width = 0
  'm.monthLbl.width = 0

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

    m.poster.width = m.top.width - 679
    m.poster.height = m.top.height
    m.poster.loadWidth = m.top.width - 679
    m.poster.loadHeight = m.top.height

  end if

end function

function onContentChanged(nodeEvent as object)

  content = nodeEvent.getData()

  if content <> invalid
    if content.content_type = "Creator"
      m.poster.uri = content.profileImg
    else
      m.poster.uri = content.thumbnail_image
    end if

    if content.content_type = "Creator"
      m.viewsLbl.text = content.channel_subscribers + " Subscribers"
    else
      m.viewsLbl.text = content.dateUploaded
    end if

    ' if content.content_type = "Creator"
    '   m.monthLbl.text = ""
    ' else
    '   m.monthLbl.text = content.dateUploaded
    ' end if

    if isValid(content.genre) and content.genre.count() > 0
      for each data in content.genre
        m.genre = data.name
      end for
      m.genreText.text = "Genre: " + m.genre
    else if isValid(content.channel_name)
      m.genreText.text = "Channel Name: " + content.channel_name
    end if


    m.ratingLabel.width = 0
    children = []
    if isValid(content.rating) and content.rating <> ""
      m.ratingLabel.text = content.rating
      m.ratingLabel.color = "#ffffff"

      textWidth = m.ratingLabel.boundingRect().width
      borderWidth = textWidth + 30
      borderHeight = 50

      m.borderRatingBack.width = borderWidth
      m.borderRatingBack.height = borderHeight
      m.ratingBack.width = borderWidth - 4
      m.ratingBack.height = borderHeight - 4
      m.ratingBack.translation = [2, 2]
      m.ratingLabel.width = borderWidth - 4
      m.ratingLabel.height = borderHeight - 4
      m.ratingLabel.translation = [2, 2]

      children.push(m.borderRatingBack)
    end if

    if isValid(content.duration) and content.duration <> ""
      m.durationLbel.text = FormatDurationwithHr(content.duration.toInt()) + "  "
      m.durationLbel.color = "#ffffff"
      children.push(m.durationLbel)
    end if

    if isValid(content.title)
      m.title.text = content.title
      m.title.color = "#ffffff"
      children.push(m.title)
    end if

    m.titleLayoutGrp.removeChildrenIndex(m.titleLayoutGrp.getChildCount(), 0)
    for each child in children
      m.titleLayoutGrp.appendChild(child)
    end for


    if children.count() > 1
      m.titleLayoutGrp.itemSpacings = [10]
    else
      m.titleLayoutGrp.itemSpacings = [0]
    end if
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
