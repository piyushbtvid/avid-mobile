sub init()

    m.backRect = m.top.findNode("backRect")
    m.frontRect = m.top.findNode("frontRect")

    m.poster = m.top.findNode("itemPoster")
    m.title = m.top.findNode("itemTitle")
    m.itemGroup = m.top.findNode("itemGroup")

end sub

sub itemContentChanged()
    itemData = m.top.itemContent
    ?"itemData " itemData.isSelected
    if itemData <> invalid
        m.poster.uri = itemData.image
        m.title.text = itemData.title

        updateColors(itemData.isSelected, m.top.focusPercent > 0.5 and m.top.itemHasFocus)
    end if
end sub

sub focusDidChange()
    itemData = m.top.itemContent
    isSelected = (itemData <> invalid and itemData.isSelected)
    updateColors(isSelected, m.top.focusPercent > 0.5 and m.top.itemHasFocus)
end sub

sub updateColors(isSelected as boolean, hasFocus as boolean)
    if hasFocus
        m.backRect.color = "#B87333"
        m.frontRect.color = "#1e1e1e"
    else if isSelected
        m.backRect.color = "#dddddd"
        m.frontRect.color = "#1e1e1e"
    else
        m.backRect.color = "#1e1e1e"
        m.frontRect.color = "#1e1e1e"
    end if
end sub

function updateLayout(event as object)
    if m.top.width > 0 and m.top.height > 0 then

    end if
end function