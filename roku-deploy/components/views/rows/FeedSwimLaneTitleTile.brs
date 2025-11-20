sub init()
  m.top.id = "FeedSwimLaneTitleTile"
  m.bigPoster = m.top.findNode("bigPoster")
  m.backCover = m.top.findNode("backCover")
  m.scaledElements = m.top.findNode("scaledElements")
  m.lblTitle = m.top.findNode("lblTitle")
  m.lblSub = m.top.findNode("lblSub")
  m.roundCover = m.top.findNode("roundCover")

  ' Initialize scaling properties
  m.scaledElements.scaleRotateCenter = [0.5, 0.5]
  m.scaledElements.translation = [0, 0]
  m.scalePercentage = 1.0
end sub

sub updateLayout()
  m.bigPoster.width = m.top.width-105
  m.bigPoster.height = m.top.width-105
  m.bigPoster.loadWidth = m.top.width-105
  m.bigPoster.loadHeight = m.top.width-105

  m.backCover.width = m.top.width-105
  m.backCover.height = m.top.width-105

  m.roundCover.width = m.top.width-105
  m.roundCover.height = m.top.width-105

  m.lblTitle.width = m.top.width-105
  m.lblSub.width = m.top.width-105

  m.lblTitle.translation = [0, m.top.width-105 + 40]
  m.lblSub.translation = [0, m.top.width-105 + 80]

  m.roundCover.blendColor = "#1B1B1B"

end sub

sub onContentChanged(nodeEvent as object)
  content = nodeEvent.getData()

  m.roundCover.visible = true
  m.bigPoster.uri = content.data.profile_img 

  m.lblSub.text = content.data.channel_subscribers + " Subscribers"
  m.lblTitle.text = content.data.name 

end sub

function onFocusChanged()

 if (not m.top.rowListHasFocus and not m.top.gridHasFocus) or m.top.rowFocusPercent = 0 then
    scalePercentage = 1.0
  else
    focusPercent = m.top.focusPercent
    if focusPercent > 1 and m.top.rowFocusPercent < 1 then
      focusPercent = m.top.rowFocusPercent  
    end if
 
    scalePercentage = 1.0 + (0.17 * focusPercent)
  end if
 
  m.scaledElements.scale = [scalePercentage, scalePercentage]

  itemWidth = m.bigPoster.width
  itemHeight = m.bigPoster.height
  m.scaledElements.translation = [
    (1 - scalePercentage) * itemWidth / 2,
    (1 - scalePercentage) * itemHeight / 2
  ]


  if scalePercentage > 1.0 then
    parent = m.scaledElements.getParent()
    if parent.getChildCount() > 1 then
      parent.removeChild(m.scaledElements)
      parent.appendChild(m.scaledElements)
    end if
  end if
  
end function

