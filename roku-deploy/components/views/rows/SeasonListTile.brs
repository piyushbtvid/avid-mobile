function init()
    m.scaledElements = m.top.findNode("scaledElements")
    m.lblTitle = m.top.findNode("lblTitle")
    m.focusRect = m.top.findNode("focusRect")
end function

function onFocusChanged(event as object)


    ' if (not m.top.rowListHasFocus and not m.top.gridHasFocus) or m.top.rowFocusPercent = 0 then
    '   scalePercentage = m.scalePercentage
    ' else
    '   focusPercent = m.top.focusPercent
    '   if focusPercent > 1 and m.top.rowFocusPercent < 1 then
    '     focusPercent = m.top.rowFocusPercent
    '   end if
    '   scalePercentage = m.scalePercentage + ((1 - m.scalePercentage) * focusPercent)
    ' end if

    if m.top.itemHasFocus
        if m.top.focusPercent > 0.9
            m.focusRect.visible = true
        else
            m.focusRect.visible = false
        end if
    else
        m.focusRect.visible = false
    end if

end function

function updateLayout(event as object)
    if (m.top.width > 0 and m.top.height > 0) then
        m.lblTitle.width = m.top.width
        m.lblTitle.height = m.top.height
        m.focusRect.width = m.top.width
    end if
end function

function onContentChanged(nodeEvent as object)
    content = nodeEvent.getData()
    if content.seasonid <> invalid
        m.lblTitle.text = content.seasonid
    end if
end function
