function init()

    m.scaledElements = m.top.findNode("scaledElements")
    m.progressBar = m.top.findNode("progressBar")
    m.frontRect = m.top.findNode("frontRect")
    m.cardImage = m.top.findNode("cardImage")


    m.metadataGroup = m.top.findNode("metadataGroup")
    m.lblTitle = m.top.findNode("lblTitle")
    m.description = m.top.findNode("description")
    m.leftDuration = m.top.findNode("leftDuration")
    m.playIcon = m.top.findNode("playIcon")


    ' Initialize scaling properties
    m.scaledElements.scaleRotateCenter = [0.5, 0.5]
    m.scaledElements.translation = [0, 0]
    m.scalePercentage = 1.0

end function

'***** Handlers *****

function onFocusChanged(event as object)


    if (not m.top.rowListHasFocus and not m.top.gridHasFocus) or m.top.rowFocusPercent = 0 then
        scalePercentage = 1.0
        m.progressBar.translation = [30, 300]
    else
        m.progressBar.translation = [30, 300]
        focusPercent = m.top.focusPercent
        scalePercentage = 1.0 + (0.10 * focusPercent)
    end if


    m.scaledElements.scale = [scalePercentage, scalePercentage]

    itemWidth = m.top.width
    itemHeight = m.top.height


    m.scaledElements.translation = [
        (1 - scalePercentage) * itemWidth / 2,
        (1 - scalePercentage) * itemHeight / 2
    ]

    if scalePercentage > 1.0 then
        parent = m.scaledElements.getParent()
        if parent.getChildCount() > 1 then
            parent.removeChild(m.scaledElements)
            parent.appendChild(m.scaledElements)
        end if
    end if
end function

function updateLayout(event as object)
    if (m.top.width > 0 and m.top.height > 0) then

        m.cardImage.width = m.top.width
        m.cardImage.height = m.top.height - 84
        m.cardImage.loadWidth = m.top.width
        m.cardImage.loadHeight = m.top.height - 84

    end if

end function

function onContentChanged(nodeEvent as object)

    content = nodeEvent.getData()

    m.cardImage.loadDisplayMode = "scaleToZoom"


    m.progressBar.width = calculateProgressDuration(content)

    totalDuration = content.duration.toInt()

    leftDuration = totalDuration - content.progress_seconds

    if content.poster_image <> invalid and content.poster_image.len() > 0
        m.cardImage.uri = content.poster_image
        m.lblTitle.text = content.title

        if content.description <> invalid
            m.description.text = content.description
        end if

        if content.progress_seconds <> invalid and content.row_type = "continue"
            if leftDuration <> invalid and leftDuration > 0
                m.leftDuration.text = FormatDurationwithHr(leftDuration) + " left"
                m.leftDuration.visible = true
                m.playIcon.visible = true
            end if
        else
            m.leftDuration.visible = false
            m.playIcon.visible = false
        end if

    end if

end function


function calculateProgressDuration(content) as float
    'print "m.top.width: "; m.top.width

    if content = invalid or content.duration = invalid or content.progress_seconds = invalid
        return 0
    end if

    totalDuration = content.duration.toInt()
    progressSeconds = content.progress_seconds '/ 1000
    totalWidth = m.top.width

    if totalDuration <= 0 'or totalWidth <= 0 then
        return 0
    end if

    if totalDuration <= progressSeconds
        return 0
    end if

    if totalDuration <> invalid and totalDuration > 0
        progressWidth = (progressSeconds / totalDuration) * totalWidth
    end if

    return progressWidth
end function

