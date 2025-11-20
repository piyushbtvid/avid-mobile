function init()

  m.top.id = "CreatorController"

  m.constants = GetConstants()
  m.scene = getScene()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.creatorGrid = m.top.findNode("creatorGrid")
  m.creatorGrid.observeField("itemSelected", "onItemSelected")
  m.creatorGrid.observeField("itemFocused", "onItemFocused")
  m.sliderTiles = m.top.findNode("sliderTiles")
  m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")
  m.leftMenu = m.scene.findNode("leftMenu")
  m.leftMenu.visible = true

  m.TopMenu = m.scene.findNode("LiveTopMenu")
  m.TopMenu.visible = false
  loadCreator()
end function

function onNavigateTo(param as object)
  m.creatorGrid.translation = [150, 700]

  pageState = param.controllerState
  if isValid(pageState) and isvalid(pageState.rowListIndex)
    m.rowListIndex = pageState.rowListIndex
  end if
end function

function onNavigateAway()
  m.top.controllerState = { rowListIndex: m.selectedItemId }
end function

function loadCreator()
  startSpinner(m.top)
  login_Token = LocalStorage().getValueForKey("login_Token")
  creatorPageContent = TVService(m.apiProvider).getCreatorsListData(login_Token)
  creatorPageContent.httpResponse.observeField("response", "onGetContentResponse")
  m.http.sendRequest(creatorPageContent)
end function


function onGetContentResponse(event)
  stopSpinner()
  response = event.getData()

  feedList = createObject("RoSGNode", "ContentNode")
  feedList.addFields({ row_type: "" })
  feedList.addFields({ creator_type: "creator" })

  itemArray = []
  if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data) and response.data.data.data.count() > 0
    contentResponse = response.data.data.data
    feedList.update(contentResponse[0], true)
    feedList.row_type = contentResponse[0].content_type
    setSliderContent(feedList)
    m.sliderTiles.itemContent = feedList
    for i = 1 to contentResponse.count() - 1
      contentNode = createObject("RoSGNode", "CreatorDataResponseModel")
      contentNode.callfunc("parseData", contentResponse[i])
      itemArray.push(contentNode)
    end for
  end if
  feedList.appendChildren(itemArray)
  m.creatorGrid.content = feedList

  if isValid(m.rowListIndex)
    m.creatorGrid.jumpToItem = m.rowListIndex
    applyFocus(m.creatorGrid, true, "onNavigateTo() - CreatorController.brs")
  else
    applyFocus(m.sliderTiles, true, "onNavigateTo() - CreatorController.brs")
  end if
end function

function onItemSelected(event as object)
  selectedIndex = event.getData()
  m.selectedItemId = selectedIndex
  selectedItem = m.creatorGrid.content.getChild(selectedIndex)
  selectedId = selectedItem.data.id
  pageInfo = createPageInfo(m.constants.CONTROLLERS.CREATORDETAIL, { creatorIndex: selectedId })
  m.scene.loadController = pageInfo
end function

function onLeftNavFocusEscape(event as object) as void
  escapeStatus = event.getData()
  if escapeStatus = true
    OnKeyEvent(m.constants.remote_keys.RIGHT, true)
    m.creatorGrid.translation = [150, 700]
    m.creatorGrid.visible = true
    m.sliderTiles.visible = true
    'm.creatorGrid.jumpToItem = 0
  end if
end function

function OnKeyEvent(key as string, press as boolean) as boolean

  handled = false
  focusMap = createFocusMap()
  currentFocusMap = getCurrentFocusItem(m.scene)

  if (press)

    if not handled
      if key = m.constants.REMOTE_KEYS.BACK
        if m.leftMenu.isExpanded = false
          m.leftMenu.isExpanded = true
          applyFocus(m.leftMenu, true, "onNavigateTo() - HomeController.brs")
          handled = true
        end if
        handled = true
      else if key = m.constants.remote_keys.RIGHT
        if m.creatorGrid.hasFocus()
          handled = true
        else
          handled = componentFocusHandler(key, focusMap, currentFocusMap)
        end if
      else if key = m.constants.remote_keys.UP and m.creatorGrid.isInFocusChain()
        applyFocus(m.sliderTiles, true, "onNavigateTo() - CreatorController.brs")
        m.sliderTiles.visible = true
        m.creatorGrid.translation = [150, 700]
        handled = true
      else if key = m.constants.remote_keys.DOWN
        if m.creatorGrid.hasFocus()
        else
          applyFocus(m.creatorGrid, true, "onNavigateTo() - CreatorController.brs")
          m.creatorGrid.jumpToItem = 0
          handled = true
        end if
        handled = true

      else
        handled = componentFocusHandler(key, focusMap, currentFocusMap)
      end if
    end if

  end if
  return handled
end function


function createFocusMap()

  focusMap = {}

  focusMap[m.leftMenu.id] = { up: invalid, down: invalid, Left: invalid, Right: m.sliderTiles.id }
  focusMap[m.sliderTiles.id] = { up: invalid, down: m.creatorGrid.id, Left: invalid: Right: invalid }
  focusMap[m.creatorGrid.id] = { up: m.sliderTiles.id, down: invalid, Left: invalid: Right: invalid }

  return focusMap

end function


sub onItemFocused(event)
  index = event.getData()
  m.creatorGrid.translation = [150, 180]
  m.sliderTiles.visible = false
end sub
