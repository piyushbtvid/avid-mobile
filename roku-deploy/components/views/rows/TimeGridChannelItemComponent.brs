function Init()

  m.top.id = "TimeGridChannelItem"

  m.title = m.top.findNode("title")
  m.poster = m.top.findNode("poster")
  m.channelContainer = m.top.findNode("channelContainer")

end function

'***** Handlers *****

function onContentSet(event as object)

  content = event.getData()


  if content <> invalid
    m.poster.uri = content.hdgridposterurl
  end if

end function

function onLayoutChanged(event as object)

  boundingRect = m.channelContainer.boundingRect()

  width = m.top.width
  height = m.top.height
  containerWidth = boundingRect.width
  containerHeight = boundingRect.height

  m.title.width = m.top.width
  
  m.poster.width = m.top.width
  m.poster.height = m.top.height

  m.poster.loadWidth = m.top.width
  m.poster.loadHeight = m.top.height

  if (width > 0 and height > 0)
    xPos = (width - containerWidth) / 2
    yPos = (height - containerHeight) / 2
    m.channelContainer.translation = [xPos, yPos]
  end if

end function
