function init()
 
  m.top.id = "NavButton"
  m.bg = m.top.findNode("bg")
  m.content = m.top.findNode("content")
  m.content2 = m.top.findNode("content2")
  m.icon = m.top.findNode("icon")
  m.label = m.top.findNode("label")
  m.focusFeedback = m.top.findNode("focusFeedback")
  m.underline = m.top.findNode("underline")
  m.underline.visible= false
  m.bg.visible = false
  setDefaults()

  m.top.observeField("focusedChild", "onFocusChange")
  m.top.observeField("label", "onLabelChange")
  m.icon.observeField("loadStatus", "onIconChange")
  m.top.observeField("selectButton", "onSelectButton")
end function

'***** Handlers *****

function onFocusChange()

  if m.parent = invalid
    m.parent = m.top.getParent()
    m.parent.observeField("buttonSelected", "onButtonSelected")
  end if

  ' if (m.top.hasFocus()) AND m.selected = true
  '   m.label.Color = "#ffffff"
  '   m.icon.blendColor= "#ffffff"
  ' else
  '   m.label.Color = "#E50914"
  '   m.icon.blendColor= "#E50914"
  ' end if

  if (m.top.hasFocus())
     m.bg.visible = true
    if m.top.isKidsMode = false
      if m.selected = true  
        m.label.Color = "#ffffff"
        m.icon.blendColor= "#ffffff"
        m.bg.blendColor = "#E50914"
       else
        m.label.Color = "#ffffff"
        m.icon.blendColor= "#ffffff"
        m.bg.blendColor = "#E50914"
       end if
      else
        m.label.Color = "#ffffff"
        m.icon.blendColor= "#ffffff"
        m.bg.blendColor = "#0573C4"
    end if
  else
    m.bg.visible = false
    if m.selected
       m.underline.visible = true
  else
    m.underline.visible = false
  end if
end if


  ' if m.selected
  '     m.label.Color = "#E50914"
  '     m.icon.blendColor= "#E50914" 
  ' else
  '   m.label.Color = "#FFFFFF"
  '   m.icon.blendColor= "#FFFFFF" 
  ' end if

  ' if m.top.isKidsMode
  '   m.underline.color = "#FFDD29"
  ' else
  '   m.underline.color = "#E50914"
  ' end if

end function

function onKidsModeChange()
  if m.top.isKidsMode
    m.underline.color = "#FFDD29"
  else
    m.underline.color = "#0082F0"
  end if
end function

'***** Helpers *****

'Configures all default values as needed for the image button
function setDefaults()
  m.selected = false
  m.PADDING_X = 20
  m.PADDING_Y = 8
  m.ICON_WIDTH = 32
  m.ITEMS_SPACING = 5
  m.HEIGHT = 52
  m.parent = invalid

  m.top.focusedIconUri = "pkg:/images/transparent_image.png"
  m.top.iconUri = "pkg:/images/transparent_image.png"
  m.top.focusFootprintBitmapUri = "pkg:/images/transparent_image.png"
  m.top.focusBitmapUri = "pkg:/images/transparent_image.png"

  m.top.height = m.HEIGHT
  m.top.id = "NavButton"

  m.icon.loadWidth = m.ICON_WIDTH
  m.icon.loadHeight = m.ICON_WIDTH
  ' m.icon.blendColor = "#E50914"



  m.content.translation = [m.PADDING_X, m.HEIGHT / 2]

  m.label.height = m.HEIGHT
  m.label.font.size = 30

  m.bg.height = m.HEIGHT + 5
  m.bg.uri = "pkg:/images/menu_item_bg_$$res$$.9.png"
  m.bg.blendColor = "#000000"
end function

function onLabelChange()
  labelWidth = m.label.boundingRect().width
  m.label.width = labelWidth
end function

function onIconChange() as void
  if m.icon.loadStatus <> "ready" then return

  iconWidth = m.icon.boundingRect().width
  iconHeight = m.icon.boundingRect().height
  m.content.translation = [m.PADDING_X, m.HEIGHT / 2]
  m.label.translation = [m.PADDING_X + iconWidth + m.ITEMS_SPACING, m.PADDING_Y]

  m.WIDTH = m.label.width + iconWidth + m.ITEMS_SPACING + (m.PADDING_X * 2) + 5
  m.top.minWidth = m.WIDTH
  m.top.maxWidth = m.WIDTH
  m.bg.width = m.WIDTH
  m.underline.width = m.WIDTH - 45
end function

function onButtonSelected(event) as void
  index = event.getData()
  if index = 7 OR m.top.index > 7 then return

  m.selected = index = m.top.index

  onFocusChange()
end function

function onSelectButton() as void
  m.selected = m.top.selectButton
  onFocusChange()
end function