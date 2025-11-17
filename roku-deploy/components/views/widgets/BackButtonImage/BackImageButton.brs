function init()
  m.top.id = "BackImageButton"
  m.btnUnFocusImage = m.top.findNode("btnUnFocusImage")
  m.btnFocusImage = m.top.findNode("btnFocusImage")
  m.detailsContainer = m.top.findNode("detailsContainer")
  m.btnIcon = m.top.findNode("btnIcon")
  m.btnLabel = m.top.findNode("btnLabel")
  
  
  m.top.observeField( "focusedChild", "onFocusChange" )
  setDefaults()
  m.paddingX = 20
end function

'***** Handlers *****
sub updateButtonUI(event as object)
  if not m.top.updateButton then return 
  'm.btnLabel.font = createFont(m.constants.fonts[m.top.fontType], m.top.fontsize)
  m.btnUnFocusImage.height = m.top.btnViewHeight
  m.btnFocusImage.height = m.top.btnViewHeight
  m.btnLabel.height = m.top.btnViewHeight
  m.btnLabel.vertAlign = "center"
  m.btnLabel.horizAlign = "left"
  if m.top.icon = ""
    m.detailsContainer.translation = [0, m.top.btnViewHeight/2]
    m.detailsContainer.removeChild(m.btnIcon)
    containerWidth = m.detailsContainer.boundingrect().width
    m.btnLabel.horizAlign = "center"
    m.btnLabel.width = containerWidth + 4*m.paddingX
    m.btnFocusImage.width = containerWidth + 4*m.paddingX
    m.btnUnFocusImage.width = containerWidth + 4*m.paddingX
  else
    m.detailsContainer.translation = [20, m.top.btnViewHeight/2]
    iconTransY = (m.top.btnViewHeight - m.top.iconHeight)/2
    m.btnIcon.translation = [0, iconTransY]
    containerWidth = m.detailsContainer.boundingrect().width
    m.btnFocusImage.width = containerWidth + 2*m.paddingX
    m.btnUnFocusImage.width = containerWidth + 2*m.paddingX
  end if
  m.top.maxWidth = 0
  m.top.minWidth = 0
end sub

function onFocusChange( event as object )
  if ( m.top.hasFocus() )
    m.btnUnFocusImage.visible = false
    m.btnFocusImage.visible = true
  else
    m.btnUnFocusImage.visible = true
    m.btnFocusImage.visible = false
  end if
end function

function setButtonFocus(params) 
  if (params )
    m.btnUnFocusImage.visible = false
    m.btnFocusImage.visible = true
  else
    m.btnUnFocusImage.visible = true
    m.btnFocusImage.visible = false
  end if
end function

'***** Helpers *****

'Configures all default values as needed for the image button
function setDefaults()

  m.top.focusedIconUri = "pkg:/images/transparent_image.png"
  m.top.iconUri = "pkg:/images/transparent_image.png"
  m.top.focusFootprintBitmapUri = "pkg:/images/transparent_image.png"
  m.top.focusBitmapUri="pkg:/images/transparent_image.png"

end function

function changeButtonUI(params)
  m.top.label = params.text
  m.top.icon = params.icon
  m.top.updateButton = true
end function