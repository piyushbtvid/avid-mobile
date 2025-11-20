function init()
  m.title = m.top.findNode("title")
  m.bgImg = m.top.findNode("bgImg")
  m.btnFocus = m.top.findNode("btnFocus")

  m.title.width = 0
  m.padding = 15

end function

function onFocusChanged(event as object)

  if m.top.focusPercent >= 0.5 and m.top.rowListHasFocus = true
    m.btnFocus.visible = true
    m.title.color = "#ffffff"
  else
    m.title.color = "#3B3B3B"
    m.btnFocus.visible = false
  end if
end function

function updateLayout(event as object)
  if m.top.width > 0 and m.top.height > 0 then
    ' Set original size for the content
    m.bgImg.width = m.top.width - m.padding
    m.bgImg.loadWidth = m.top.width - m.padding
    m.btnFocus.width = m.top.width - m.padding
    buttonHeight = 65
    m.bgImg.height = buttonHeight
    m.btnFocus.height = buttonHeight
    m.btnFocus.loadHeight = buttonHeight
    m.title.height = buttonHeight
  end if
end function

' function onContentChanged(nodeEvent as object)

'   m.title.text = m.top.itemContent.title
'   titleWidth = m.title.boundingRect().width
'   m.title.width = titleWidth
'   m.title.translation = [((m.top.width - m.padding) - titleWidth) / 2, 0]

' end function

function onContentChanged(nodeEvent as object)
  m.title.text = m.top.itemContent.title
  titleWidth = m.title.boundingRect().width

  maxTitleWidth = 330
  if titleWidth > maxTitleWidth then
    titleWidth = maxTitleWidth
  end if
  
  m.title.width = titleWidth
  m.title.translation = [((m.top.width - m.padding) - titleWidth) / 2, 0]
end function