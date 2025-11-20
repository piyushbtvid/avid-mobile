sub init()
    m.poster = m.top.findNode("itemPoster")
    m.title = m.top.findNode("itemTitle")
    m.focusAnim = m.top.findNode("focusAnimation")
    m.unfocusAnim = m.top.findNode("unfocusAnimation")
    m.itemGroup = m.top.findNode("itemGroup")
    m.background = m.top.findNode("background")
    m.focusIcon = m.top.findNode("focusIcon")
    
    ' Initialize default states
    m.focusIcon.visible = false
    m.defaultBlendColor = "#ffffff"
end sub

sub itemContentChanged()
    itemData = m.top.itemContent
    if itemData = invalid then return
    
    ' Update poster and title
    m.poster.uri = itemData.image
    m.title.text = itemData.title
    m.title.visible = itemData.expanded
    
    ' Configure alignment based on image presence
    if itemData.image = ""
        m.itemGroup.horizAlignment = "center"
        m.title.text = "                " + itemData.title
        m.title.horizAlign = "center"
    else
        m.title.width = m.title.boundingRect().width
        m.itemGroup.horizAlignment = "left"
        m.title.horizAlign = "left"
    end if
    
    ' Update item group translation
    translationX = 30
    groupHeight = 76
    if itemData.expanded
        translationX = m.background.translation[0] + 10
        groupHeight = 66
    end if
    translationY = m.background.translation[1] + ((groupHeight - m.itemGroup.boundingRect().height) / 2)
    m.itemGroup.translation = [translationX, translationY]
    
    ' Update focus icon for non-focused selected items
    if not m.top.itemHasFocus and itemData.isSelected and itemData.image <> ""
        updateFocusIcon(itemData.expanded)
    else
        m.poster.blendColor = m.defaultBlendColor
        m.focusIcon.visible = false
    end if
end sub

sub focusDidChange()
    itemData = m.top.itemContent
    if itemData = invalid then return
    if m.top.listHasFocus
       m.background.opacity = m.top.focusPercent
    else
      m.background.opacity = 0
    end if 

    if m.top.focusPercent > 0.5 and m.top.itemHasFocus
        m.poster.blendColor = m.defaultBlendColor
        m.focusIcon.visible = false
    else if itemData.isSelected and itemData.image <> ""
        updateFocusIcon(itemData.expanded)
    else
        m.focusIcon.visible = false
    end if
end sub

sub updateFocusIcon(isExpanded as boolean)
    if isExpanded
        m.focusIcon.translation = [44, 0]
        m.focusIcon.uri = "pkg://images/rounded-left.png"
        m.focusIcon.width = 200
    else
        m.focusIcon.translation = [15, 4]
        m.focusIcon.uri = "pkg://images/menuCollapseFocus.png"
        m.focusIcon.width = 66
    end if
    m.focusIcon.blendColor = "#000000"
    m.focusIcon.visible = true
    m.poster.blendColor = "0xFFFFFFFF"
end sub

function updateLayout(event as object) as boolean
    if m.top.width <= 0 or m.top.height <= 0 then return false
    
    m.background.width = 200
    m.background.loadWidth = 200
    return true
end function