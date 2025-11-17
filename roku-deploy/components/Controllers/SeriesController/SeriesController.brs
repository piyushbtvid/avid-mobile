function init()

  m.top.id = "SeriesController"

  m.constants = GetConstants()
  m.scene = getScene()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.seriesList = m.top.findNode("seriesList")
  m.seriesLayout = m.top.findNode("seriesLayout")
  m.seriesList.observeField("ItemFocused", "onGridItemFocused")
  m.seriesList.observeField("ItemSelected", "onGridItemSelected")

  m.seriesTitle = m.top.findNode("seriesTitle")
  m.sliderTiles = m.top.findNode("sliderTiles")
  m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")

  m.leftMenu = m.scene.findNode("leftMenu")
  m.leftMenu.visible = true

  m.TopMenu = m.scene.findNode("LiveTopMenu")
  m.TopMenu.visible = false

end function

function onNavigateTo(param as object)
  m.TopMenu.visible = false
  loadSeries()
  pageState = param.controllerState
  if isValid(pageState) and isvalid(pageState.rowListIndex)
    m.rowListIndex = pageState.rowListIndex
  end if
end function

function onNavigateAway()

  m.top.controllerState = { rowListIndex: m.selectedGridId }

end function

function loadSeries()
  startSpinner(m.top)
  login_Token = LocalStorage().getValueForKey("login_Token")

  seriesPageContent = TVService(m.apiProvider).getSeriesData(login_Token)
  seriesPageContent.httpResponse.observeField("response", "onSeriesResponse")
  m.http.sendRequest(seriesPageContent)

end function

function onSeriesResponse(event)

  stopSpinner()

  response = event.getData()
  feedList = createObject("RoSGNode", "ContentNode")
  feedList.addFields({ row_type: "" })
  itemArray = []
  if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data) and response.data.data.data.count() > 0 
    contentResponse = response.data.data.data

    m.seriesTitle.text = "Series"
    feedList.row_type = contentResponse[0].content_type
    feedList.update(contentResponse[0], true)
    setSliderContent(feedList)
    m.sliderTiles.itemContent = feedList

    for i = 1 to contentResponse.count() - 1
      contentNode = createObject("RoSGNode", "feedListItemDataModel")
      contentNode.row_type = "series"
      contentNode.callfunc("parseData", contentResponse[i])
      itemArray.push(contentNode)
    end for
  end if

  feedList.appendChildren(itemArray)
  m.seriesList.content = feedList
  if isValid(m.rowListIndex)
    m.seriesList.jumpToItem = m.rowListIndex
    applyFocus(m.seriesList, true, "onNavigateTo() - SeriesController.brs")
  else
    applyFocus(m.sliderTiles, true, "onNavigateTo() - SeriesController.brs")
  end if
end function


function onGridItemSelected(event as object)

  selectedIndex = event.getData()
  selectedItem = m.seriesList.content.getChild(selectedIndex)
  m.selectedGridId = selectedIndex
  selectedGrid = m.seriesList.content

  pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex, content_Type: selectedItem.content_Type, continueWatch : false  })
  m.scene.loadController = pageInfo

end function


function onGridItemFocused(event as object)

  m.seriesLayout.translation = [153, 50]
  m.sliderTiles.visible = false

end function


function onLeftNavFocusEscape(event as object) as void
  escapeStatus = event.getData()
  if escapeStatus = true
    OnKeyEvent(m.constants.remote_keys.RIGHT, true)
    m.seriesLayout.translation = [137, 740]
    m.seriesList.jumpToItem = 0
    m.seriesList.visible = true
    m.sliderTiles.visible = true
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
      else if key = m.constants.remote_keys.UP and m.seriesList.isInFocusChain()
        applyFocus(m.sliderTiles, true, "onNavigateTo() - SeriesController.brs")
        m.sliderTiles.visible = true
        m.seriesLayout.translation = [137, 740]
        handled = true
      else if key = m.constants.remote_keys.RIGHT
        if m.seriesList.hasFocus()
          handled = true
        else
          handled = componentFocusHandler(key, focusMap, currentFocusMap)
        end if
      else if key = m.constants.remote_keys.DOWN
        applyFocus(m.seriesList, true, "onNavigateTo() - SeriesController.brs")
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
  focusMap[m.sliderTiles.id] = { up: invalid, down: m.seriesList.id, Left: m.leftMenu.id, Right: invalid }
  focusMap[m.seriesList.id] = { up: m.sliderTiles.id, down: invalid, Left: m.leftMenu.id, Right: invalid }

  return focusMap

end function

