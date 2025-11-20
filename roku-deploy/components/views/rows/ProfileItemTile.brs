function init()
  m.itemLabel = m.top.findNode("itemLabel")
  m.image = m.top.findNode("image")
  m.scaledElements = m.top.findNode("scaledElements")
  m.circleFocus = m.top.findNode("circleFocus")

  ' Initialize states
  m.isFocused = false
  m.isSelected = false
  m.circleFocus.visible = false
end function

function onContentChanged()
  content = m.top.itemContent
  m.isCreateProfile = content.isCreateProfile

  if content <> invalid
    m.itemLabel.text = content.title
    m.image.uri = content.url

    if content.isSelected <> invalid
      updateSelectionState(content.isSelected)
    end if
    
    if content.isCreateProfile = true
      m.image.width = m.top.width
      m.image.loadWidth = m.top.width
      m.image.height = m.top.height
      m.image.loadHeight = m.top.height
      m.circleFocus.width = m.top.width+8
      m.circleFocus.height = m.top.height+8
    end if
  end if
end function

sub onFocusChanged()
  m.isFocused = (m.top.focusPercent >= 0.5) and (m.top.gridHasFocus or m.top.rowListHasFocus)
  updateVisualState()
end sub

sub updateSelectionState(isSelected as boolean)
  m.isSelected = isSelected
  updateVisualState()
end sub

sub updateVisualState()
  if m.isSelected
    m.circleFocus.visible = true
    m.circleFocus.uri = "pkg://images/border_a.png"

    if m.isFocused
      m.circleFocus.blendColor = "#B87333FF"
    else
      m.circleFocus.blendColor = "#FFFFFFFF"
    end if

  else if m.isFocused
    m.circleFocus.visible = true
    m.circleFocus.uri = "pkg://images/border_a.png"
    m.circleFocus.blendColor = "#B87333FF"

  else
    m.circleFocus.visible = false
  end if
end sub

function updateLayout(event as object)
  if (m.top.width > 0 and m.top.height > 0)
    m.image.width = m.top.width
    m.image.height = m.top.height - 100
    m.image.loadWidth = m.top.width
    m.image.loadHeight = m.top.height - 100
    m.itemLabel.width = m.top.width
    m.circleFocus.width = m.top.width+8
    m.circleFocus.height = m.top.height - 92
  end if
end function