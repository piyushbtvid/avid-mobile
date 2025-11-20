sub init()
    m.title = m.top.findNode("itemTitle")
    m.focusAnim = m.top.findNode("focusAnimation")
    m.unfocusAnim = m.top.findNode("unfocusAnimation")
    m.itemGroup = m.top.findNode("itemGroup")
    m.background = m.top.findNode("background")
    'm.focusIcon = m.top.findNode("focusIcon")

    ' Initialize default states
   ' m.focusIcon.visible = false
    m.defaultBlendColor = "#ffffff"
end sub

sub itemContentChanged()
    itemData = m.top.itemContent
    if itemData = invalid then return

    m.title.text = itemData.title

end sub

sub focusDidChange()
    itemData = m.top.itemContent
    if itemData = invalid then return
    if m.top.listHasFocus
        m.background.opacity = m.top.focusPercent
    else
        m.background.opacity = 0
    end if

    ' if m.top.focusPercent > 0.5 and m.top.itemHasFocus
    '     m.focusIcon.visible = false
    ' else
    '     m.focusIcon.visible = false
    ' end if
end sub


function updateLayout(event as object) as boolean
    if m.top.width <= 0 or m.top.height <= 0 then return false

    m.background.width = m.top.width+6
    m.background.loadWidth = m.top.width+6
    m.background.height = m.top.height
    m.title.width = m.top.width-10
    return true
end function