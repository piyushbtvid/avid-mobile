function init()

  m.constants = GetConstants()
  m.scene = getScene()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.scaledElements = m.top.findNode("scaledElements")
  m.background = m.top.findNode("background")
  m.background.loadHeight = 716
  m.background.loadWidth = 1772
  m.metaDataLayout = m.top.findNode("metaDataLayout")
  m.lblTitle = m.top.findNode("lblTitle")
  m.lblDesription = m.top.findNode("lblDesription")
  m.watchButton = m.top.findNode("watchButton")
  m.watchButton.observeField("buttonSelected", "onWatchButtonSelected")
  m.interactionButtons = m.top.findNode("interactionButtons")
  m.addButton = m.top.findNode("addButton")
  m.thumbUpButton = m.top.findNode("thumbUpButton")
  m.thumbDownButton = m.top.findNode("thumbDownButton")
  m.micButton = m.top.findNode("micButton")
  m.searchButton = m.top.findNode("searchButton")
  m.searchButton.observeField("buttonSelected", "onSearchButtonSelected")
  m.constants = GetConstants()
  m.scene = getScene()

  m.top.observeFieldScoped("focusedChild", "onFocusChanged")

  m.thumbUpButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumb-up.png", focusImg: "pkg://images/focusThumbUpIcon.png", width: 65, height: 65 }
  m.thumbDownButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumbs-down.png", focusImg: "pkg://images/focus-thumb-down.png", width: 65, height: 65 }

  m.micButton.imgSrc = { label: "", unFocusImg: "pkg://images/macIcon.png", focusImg: "pkg://images/focusMacIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }
  m.searchButton.imgSrc = { label: "", unFocusImg: "pkg://images/searchIcon.png", focusImg: "pkg://images/FocusSearchIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }

  m.watchButton.imgSrc = { id: "platBtn", label: "", unFocusImg: "pkg:/images/play-button.png", focusImg: "pkg://images/button-focus.png", width: 124, height: 124, focusedWidth: 164, focusedHeight: 164 }

  m.scaledElementsMac = m.top.findNode("scaledElementsMac")
  m.scaledElementsBtn = m.top.findNode("scaledElementsBtn")

  m.addButton.observeField("buttonSelected", "addMyListButtonSelectedFun")
  m.thumbUpButton.observeField("buttonSelected", "addThumbUpBtnSelectedFun")
  m.thumbDownButton.observeField("buttonSelected", "addThumbDownBtnSelectedFun")

  m.isMyList = false
  m.likeList = false
  m.disLikeList = false
  m.myListArrIdGlobal = []
end function

function onFocusChanged(event as object)

  if (m.top.hasFocus())
    loadMyList()
    applyFocus(m.watchButton, true, "onNavigateTo() - HomeController.brs")
  end if

end function


function onContentChanged(nodeEvent as object)

  m.watchButton.visible = true
  m.scaledElementsBtn.visible = true


  content = nodeEvent.getData()

  if content.creator_type = "creator"
    m.scaledElementsMac.visible = false
    m.addButton.visible = false
    m.thumbUpButton.visible = false
    m.thumbDownButton.visible = false
  else
    m.scaledElementsMac.visible = true
    m.addButton.visible = true
    m.thumbUpButton.visible = true
    m.thumbDownButton.visible = true
  end if

  if content.landscape <> invalid and content.landscape.len() > 0
    m.background.uri = content.landscape
  else if content.channel_banner <> invalid and content.channel_banner.len() > 0
    m.background.uri = content.channel_banner
  end if

  if content.name <> invalid and content.name.len() > 0
    m.lblTitle.text = content.name
  else if content.channel_name <> invalid and content.channel_name.len() > 0
    m.lblTitle.text = content.channel_name
  end if

  if content.description <> invalid and content.description.len() > 0
    m.lblDesription.text = content.description
  else if content.channel_description <> invalid and content.channel_description.len() > 0
    m.lblDesription.text = content.channel_description
  else if content.bio <> invalid and content.bio.len() > 0
    m.lblDesription.text = content.bio
  end if

  setOtherInfo(content)

  m.background.translation = [15, 0]
  m.metaDataLayout.translation = [185, 60]
  m.interactionButtons.translation = [40, 40 + m.metaDataLayout.boundingRect().height + 50]

  loadContent(content.slug)

end function

function loadContent(video_id)
  login_Token = LocalStorage().getValueForKey("login_Token")
  detailsPageContent = TVService(m.apiProvider).getDetails(video_id, login_Token)
  detailsPageContent.httpResponse.observeField("response", "onGetContentResponse")
  m.http.sendRequest(detailsPageContent)
end function

sub onGetContentResponse(event)

  response = event.getData()
  if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
    m.videoContent = response.data.data.data
    m.relatedData = m.videoContent.related_content
    m.currentVideoId = m.videoContent.slug
    loadLikedApi()
    loadDisLikedApi()
    manageMyListlistFeature()
    
  end if
end sub

sub onWatchButtonSelected()
  itemContent = m.top.itemContent
  if lcase(itemContent.row_type) = "series"
    pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: itemContent.slug, selected_rowItem_Index: 0 })
    m.scene.loadController = pageInfo
  else if itemContent.creator_type <> invalid and lcase(itemContent.creator_type) = "creator"
    pageInfo = createPageInfo(m.constants.CONTROLLERS.CREATORDETAIL, { creatorIndex: itemContent.id })
    m.scene.loadController = pageInfo
  else
    pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { content: m.videoContent, relateddata: m.relatedData, isResume: true })
    m.scene.loadController = pageInfo
  end if
end sub

function setOtherInfo(content)
  movieInfo = []

  m.metaDataLayout.removeChildIndex(2)

  movieInfoGroup = createObject("roSGNode", "layoutGroup")
  movieInfoGroup.layoutDirection = "horiz"
  movieInfoGroup.itemSpacings = "[15]"
  ' movieInfoGroup.translation=[-15,40]

  if isValid(content.uploadedYear)
    releaseDateLabel = createObject("roSGNode", "label")
    releaseDateLabel.text = content.uploadedYear + "   |"
    releaseDateLabel.font.uri = "pkg://images/font/Montserrat-Bold.ttf"
    releaseDateLabel.font.size = "25"
    releaseDateLabel.color = "#ffffff"

    movieInfo.push(releaseDateLabel)

  end if

  if isValid(content.genres) and content.genres.count() > 0
    for each data in content.genres
      m.genre = data.name
    end for
    genreLabel = createObject("roSGNode", "label")
    genreLabel.text = m.genre + "   |"
    genreLabel.font.uri = "pkg://images/font/Montserrat-Bold.ttf"
    genreLabel.font.size = "25"
    genreLabel.color = "#ffffff"

    movieInfo.push(genreLabel)

  end if

  if isValid(content.access)
    accessLbel = createObject("roSGNode", "label")
    accessLbel.text = content.access + "   |"
    accessLbel.font.uri = "pkg://images/font/Montserrat-Bold.ttf"
    accessLbel.font.size = "25"
    accessLbel.color = "#ffffff"
    movieInfo.push(accessLbel)
  end if

  if isValid(content.rating)
    ratingLbel = createObject("roSGNode", "label")
    ratingLbel.text = content.rating
    ratingLbel.font.uri = "pkg://images/font/Montserrat-Bold.ttf"
    ratingLbel.font.size = "25"
    ratingLbel.color = "#ffffff"
    movieInfo.push(ratingLbel)
  end if



  if isValid(content.channel_subscribers)
    movieInfoGroup.itemSpacings = "[0]"
    subscriberLbel = createObject("roSGNode", "label")
    subscriberLbel.text = content.channel_subscribers + " Subscribers"
    subscriberLbel.font.uri = "pkg://images/font/Montserrat-Bold.ttf"
    subscriberLbel.font.size = "40"
    subscriberLbel.color = "#ffffff"
    movieInfo.push(subscriberLbel)
  end if

  movieInfoGroup.appendChildren(movieInfo)
  m.metaDataLayout.insertChild(movieInfoGroup, 0)

end function

function formatTime(TotalSeconds = 0 as integer) as string
  datetime = CreateObject("roDateTime")
  datetime.FromSeconds(TotalSeconds)

  hours = datetime.GetHours().ToStr()
  minutes = datetime.GetMinutes().ToStr()
  seconds = datetime.GetSeconds().ToStr()

  duration = ""
  if hours <> "0" then
    duration = duration + hours + "h "
  end if
  if minutes <> "0" then
    duration = duration + minutes + "m "
  end if

  return duration
end function

function ConvertTimestampToDate(timestamp as integer) as string
  dt = CreateObject("roDateTime")

  dt.FromSeconds(timestamp)

  formattedDate = dt.GetYear().tostr()

  return formattedDate
end function

function OnKeyEvent(key as string, press as boolean) as boolean

  handled = false
  focusMap = creaSlideteFocusMap()
  currentFocusMap = getCurrentFocusItem(m.scene)
  if currentFocusMap.focusedChild <> invalid
    currentFocusedItem = currentFocusMap.focusedChild
  end if
  itemContent = m.top.itemContent

  if (press)

    if key = "down" and currentFocusedItem.id = "watchButton"
      handled = componentFocusHandler(key, focusMap, currentFocusMap)
    else if key = "up" and isValid(itemContent) and itemContent.creator_type <> invalid and lcase(itemContent.creator_type) = "creator"
      handled = false
    else
      handled = componentFocusHandler(key, focusMap, currentFocusedItem)
    end if

  end if
  return handled
end function



function creaSlideteFocusMap()
  focusMap = {}
  focusMap[m.watchButton.id] = { up: m.addButton.id, down: invalid, left: invalid, right: invalid }
  focusMap[m.addButton.id] = { up: m.micButton.id, down: m.watchButton.id, left: invalid, right: m.thumbUpButton.id }
  focusMap[m.thumbUpButton.id] = { up: m.micButton.id, down: m.watchButton.id, left: m.addButton.id, right: m.thumbDownButton.id }
  focusMap[m.thumbDownButton.id] = { up: m.micButton.id, down: m.watchButton.id, left: m.thumbUpButton.id, right: m.micButton.id }
  focusMap[m.micButton.id] = { up: invalid, down: m.addButton.id, left: m.thumbDownButton.id, right: m.searchButton.id }
  focusMap[m.searchButton.id] = { up: invalid, down: m.addButton.id, left: m.micButton.id, right: invalid }
  return focusMap
end function

sub onSearchButtonSelected()
  pageInfo = createPageInfo(m.constants.CONTROLLERS.SEARCH, {goBackToSliderSearch:true})
  m.scene.loadController = pageInfo
end sub

