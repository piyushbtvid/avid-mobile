function init()

  m.top.id = "LabelButton"

  m.itemLabel = m.top.findNode("itemLabel")
  m.focusBack = m.top.findNode("focusBack")

  m.top.focusedIconUri = "pkg:/images/transparent_image.png"
  m.top.iconUri = "pkg:/images/transparent_image.png"
  m.top.focusFootprintBitmapUri = "pkg:/images/transparent_image.png"
  m.top.focusBitmapUri = "pkg:/images/transparent_image.png"
  m.top.observeField("focusedChild", "onFocus")


end function


'***** Handlers *****

' set height and widht of button node and lable '
function onSizeChange()
  m.top.height = m.top.buttonConfig.height
end function

function onFocus()

  if (m.top.hasFocus())
    if m.top.buttonConfig <> invalid
      m.focusBack.visible = true
      m.focusBack.color = m.top.buttonConfig.FocusbackGroundColor
    end if
  else
    m.focusBack.visible = true
    m.focusBack.color = "#5C5F66"
  end if

end function

function onButtonChange()
  m.config = m.top.buttonConfig
  m.itemLabel.text = m.config.text

  if isValid(m.config.footPrint) and not m.config.footPrint
    m.focusBack.visible = false
  else
    m.focusBack.visible = false
  end if

  alignButtonLabel()
  m.itemLabel.font.size = m.config.fontsize

end function

function alignButtonLabel()

  m.itemLabel.width = m.top.buttonConfig.width

  viewIconBounds = m.itemLabel.boundingRect()
  width = viewIconBounds.width
  height = viewIconBounds.height

  if (m.itemLabel.text.len())
    if m.itemLabel.text.len() > 14
      m.focusBack.width = m.top.buttonConfig.width
    else
      m.focusBack.width = m.top.buttonConfig.width
    end if
  end if
  m.focusBack.height = m.top.buttonConfig.height
  x = (m.focusBack.width - width) / 2
  y = (m.focusBack.height - height) / 2

  m.itemLabel.translation = [x + 2, y + 4]

  m.top.minWidth = width
end function
