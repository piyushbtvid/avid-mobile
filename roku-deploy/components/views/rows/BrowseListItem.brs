function init()

    m.itemLabel = m.top.findNode("itemLabel")
    m.navigationGroup = m.top.findNode("navigationGroup")
    m.backImg = m.top.findNode("backImg")

end function


function onFocusChanged(event as object)

    if (m.top.rowListHasFocus and m.top.focusPercent > 0.8)
        'm.itemLabel.color = "#FFFFFF"
        'm.icon.blendColor = "#FFFFFF"
        'm.focusImage.visible = true
    else
        'm.itemLabel.color = "#000000"
        'm.icon.blendColor = "#000000"
        'm.focusImage.visible = false
    end if

end function

function updateLayout(event as object)

    if (m.top.width > 0 and m.top.height > 0) then

        m.backImg.width = m.top.width
        m.backImg.height = m.top.height
        m.itemLabel.width = m.top.width
        m.itemLabel.height = m.top.height

    end if

end function

function onContentChanged(nodeEvent as object)

    content = nodeEvent.getData()

    if isValid(content)
        if content.type = "channel"
            m.backImg.uri = "pkg://images/channe_back_poster.png"
        else
            m.itemLabel.text = content.title
            m.backImg.uri = "pkg://images/programBackgroundPoster.png"
        end if
    end if

end function

