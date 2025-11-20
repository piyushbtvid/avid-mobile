function init()
  m.top.id = "SeasonsNavButton"
  m.label = m.top.findNode("label")
  m.focusFeedback = m.top.findNode("focusFeedback")
  m.underline = m.top.findNode("underline")

  setDefaults()

  m.top.observeField("focusedChild", "onFocusChange")
  m.top.observeField("label", "onLabelChange")
  m.top.observeField("selectButton", "onSelectButton")

end function

'***** Handlers *****

function onFocusChange()

  if m.parent = invalid then
    m.parent = m.top.getParent()
    m.parent.observeField("buttonSelected", "onButtonSelected")
  end if


  if m.top.hasFocus() then
    m.underline.visible = true
    m.label.color= "#ffffff"
  else
    m.underline.visible = false
    m.label.color= "#ffffff"
  end if
end function

'***** Helpers *****

'Configures all default values as needed for the image button
function setDefaults()
  m.selected = false
  m.PADDING_X = 20
  m.PADDING_Y = 17
  m.HEIGHT = 20
  m.WIDTH = 70  ' Set the desired width of the background here
  m.parent = invalid

  m.top.focusedIconUri = "pkg:/images/transparent_image.png"
  m.top.iconUri = "pkg:/images/transparent_image.png"
  m.top.focusFootprintBitmapUri = "pkg:/images/transparent_image.png"
  m.top.focusBitmapUri = "pkg:/images/transparent_image.png"

  m.top.height = m.HEIGHT
  m.top.id = "NavButton"

  m.label.height = m.HEIGHT
  m.label.font.size = 30
end function

function onLabelChange()
  labelWidth = m.label.boundingRect().width
  m.label.width = labelWidth


  m.label.translation = [(m.WIDTH - m.label.width) / 2, m.PADDING_Y + 40]
  if m.label.width > 70 then
    m.label.translation = "[10,57]"
    m.underline.width = m.WIDTH + 110
    m.underline.translation = "[40 , 95]"
  else  
    m.underline.width = m.WIDTH
  end if
  m.top.minWidth = m.WIDTH
  m.top.maxWidth = m.WIDTH
end function

function onButtonSelected(event) as void
  index = event.getData()
  if index = 6 OR m.top.index = 6 then return

  m.selected = index = m.top.index
  onFocusChange()
end function

function onSelectButton() as void
  m.selected = m.top.selectButton
  onFocusChange()
end function