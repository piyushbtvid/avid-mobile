function init()
  m.itemLabel = m.top.findNode("itemLabel")
  m.backFocus = m.top.findNode("backFocus")
  m.itemLabel.width = 0
end function

function onContentChanged()
  content = m.top.itemContent
  if content <> invalid
    m.itemLabel.text = content.title
    titleWidth = m.itemLabel.boundingRect().width
    titleHeight = m.itemLabel.boundingRect().height
    m.itemLabel.width = titleWidth
    m.itemLabel.translation = [(m.top.width - titleWidth) / 2, (m.top.height - titleHeight) / 2]

    m.backFocus.width = m.top.width
    m.backFocus.height = 40 'm.top.height
    m.backFocus.translation = [0, 17]
    

    ' Initial state update
    updateColor()
  end if
end function

function onFocusChanged()
  content = m.top.itemContent
  if content <> invalid
    updateColor()
  end if
end function

function updateColor()
  content = m.top.itemContent
  if content <> invalid and content.isSelected <> invalid
    if content.isSelected and not (m.top.focusPercent > 0.8 and m.top.rowListHasFocus)
      m.itemLabel.color = "#FFFFFF" ' Selected - white
      m.backFocus.visible = true
      m.backFocus.blendColor = "#3B3B3B"
    else if m.top.focusPercent > 0.8 and m.top.rowListHasFocus
      m.itemLabel.color = "#FFFFFF" ' Focused - white
      m.backFocus.blendColor = "#B87333"
      m.backFocus.visible = true
    else
      m.itemLabel.color = "#000000" ' Normal - black
      m.backFocus.visible = false
    end if
  end if
end function