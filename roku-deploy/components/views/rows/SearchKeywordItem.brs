function init()
    m.searchIndex = m.top.findNode("searchIndex")
    m.spaceIconIndex = m.top.findNode("spaceIconIndex")
    m.focusImage = m.top.findNode("focusImage")

    m.menuBackColor = m.top.findNode("menuBackColor")
    m.deleteSelectImage = m.top.findNode("deleteSelectImage")
    m.spaceSelectImage = m.top.findNode("spaceSelectImage")
end function

function itemContentChanged()
    itemData = m.top.itemContent
    m.searchIndex.width = 0
    m.item = itemData
    m.searchIndex.text = itemData.searchText
    m.searchIndex.font.size = 30
    m.spaceIconIndex.uri = itemData.spaceIcon

    m.searchIndex.width = itemData.FHDItemWidth
    m.top.width = itemData.FHDItemWidth
    m.focusImage.width = itemData.FHDItemWidth
    m.focusImage.height = 44
end function

sub showfocus()
    m.searchIndex.color = "#FFFFFF"
    m.focusImage.visible = false

    if m.top.gridHasFocus and m.top.focusPercent > 0.5 then
        m.searchIndex.color = "#FFFFFF99"
        m.focusImage.visible = true
        m.focusImage.blendColor = "#000000"

    else if (m.top.index = 0 or m.top.index = 1 or m.top.index = 28) then
        m.focusImage.uri = "pkg:/images/fill_r8.9.png"
        m.focusImage.visible = true
        m.searchIndex.color = "#000000"
        m.focusImage.blendColor = "#EAE3C9"
    else
        m.focusImage.uri = "pkg:/images/menuCollapseFocus.png"
    end if
end sub
