function init()

    m.itemLabel = m.top.findNode("itemLabel")
    m.navigationGroup = m.top.findNode("navigationGroup")
    m.icon = m.top.findNode("icon")
    m.backImg = m.top.findNode("backImg")
    m.timeSlot = m.top.findNode("timeSlot")

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
        m.icon.width = 228
        m.icon.height = 67

        m.icon.translation = [(m.backImg.width - m.icon.width) / 2, (m.backImg.height - m.icon.height) / 2]

    end if

end function

function onContentChanged(nodeEvent as object)

    content = nodeEvent.getData()

    if isValid(content)
        if content.type = "channel"
            m.backImg.uri = "pkg://images/channe_back_poster.png"
            m.icon.uri = content.icon
        else
            m.itemLabel.text = content.title
            m.timeSlot.text = content.timeSlot
            m.backImg.uri = "pkg://images/programBackgroundPoster.png"
        end if
    end if

    ' bound = m.navigationGroup.boundingRect()
    ' x = (m.top.width - bound.width) / 2
    ' y = (m.top.height - bound.height) / 2
    ' m.navigationGroup.translation = [x, y + 2]

end function

