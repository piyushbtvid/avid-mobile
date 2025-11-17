function init()
  m.top.id = "SearchController"

  m.constants = GetConstants()
  m.scene = getScene()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.top.searchText = ""

  m.searchBox = m.top.findNode("searchBox")
  m.searchBox.focusable = true
  m.searchBox.ObserveField("text", "onSearchTextChanged")

  m.TopSearchList = m.top.findNode("TopSearchList")
  m.TopSearchList.ObserveField("itemSelected", "onTopSearchListSelected")

  m.searchList = m.top.findNode("searchList")
  m.searchList.observeField("itemSelected", "onSearchListItemSelected")
  m.searchList.observeField("itemFocused", "onSearchListitemFocused")

  m.searchTimer = m.top.findNode("searchTimer")
  m.searchTimer.observeFieldScoped("fire", "onSearchTimer")

  m.leftMenu = m.scene.findNode("leftMenu")
  m.leftMenu.visible = true

  m.TopMenu = m.scene.findNode("LiveTopMenu")
  m.TopMenu.visible = false
  m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")

  m.recentSearchList = m.top.findNode("recentSearchList")
  m.recentSearchList.observeField("itemSelected", "onRecentSearchListSelected")

  m.searchNotFoundLbl = m.top.findNode("searchNotFoundLbl")
  m.searchNotFoundLbl.visible = false

  m.isAlphabetMode = true

end function

function onNavigateTo(param as object)
  m.login_Token = LocalStorage().getValueForKey("login_Token")
  pageState = param.controllerState

  if isValid(param) and isValid(param.goBackToSliderSearch) then m.goBackToSliderSearch = param.goBackToSliderSearch

  if isValid(pageState)
    m.rowListIndex = pageState.rowListIndex
    m.searchBox.text = pageState.searchText
    makeSearch()
  end if
  recentSearchList()
  setSearchPage()
end function

function onNavigateAway()
  m.top.controllerState = {
    searchText: m.top.searchText,
    rowListIndex: m.selectedGridId
  }
end function

sub onDeleteBtnSelected()
  length = Len(m.searchBox.text)
  if length > 0
    str = Left(m.searchBox.text, length - 1)
    setSearchBoxData(str)
  end if
end sub


sub onToggleKeyModeClicked()
  m.isAlphabetMode = not m.isAlphabetMode

  if m.isAlphabetMode then
    setAlphabetMode()
  else
    setNumericMode()
  end if
end sub

sub onSpaceBtnClicked()
  setSearchBoxData(m.searchBox.text + " ")
end sub

function stopSearchTimer()
  m.searchTimer.control = "stop"
  m.searchTimer.duration = "0.5"
  cancelSearch()
end function

function cancelSearch() as void
  if not isValid(m.searchRequest) then return
  m.searchRequest.httpResponse.unobserveFieldScoped("response")
end function

function onSearchTimer() as void
  if not m.TopSearchList.isInFocusChain() then return
  cancelSearch()
  makeSearch()
end function

sub onTopSearchListSelected()
  if m.TopSearchList.content <> invalid and m.TopSearchList.content.getChildCount() > 0
    selectedIndex = m.TopSearchList.itemSelected
    selectedItem = m.TopSearchList.content.getChild(selectedIndex)

    searchText = selectedItem.searchText

    if searchText = "123" or searchText = "abc"
      onToggleKeyModeClicked()
    else if searchText = "Space"
      onSpaceBtnClicked()
    else if selectedItem.hasField("isDelete") and selectedItem.isDelete = true
      onDeleteBtnSelected()
    else
      setSearchBoxData(m.searchBox.text + searchText)
    end if
  end if
end sub

function setSearchBoxData(str as string)
  m.searchBox.text = str
  if m.searchBox.text.len() > 2 then
    restartSearchTimer()
  else
    stopSearchTimer()
    m.searchList.content = invalid
  end if
end function

function restartSearchTimer()
  stopSearchTimer()
  m.searchTimer.duration = "0.5"
  m.searchTimer.control = "start"
end function

function setSearchPage()
  if m.isAlphabetMode then
    setAlphabetMode()
  else
    setNumericMode()
  end if
end function

function setAlphabetMode()
  data = CreateObject("roSGNode", "ContentNode")
  columnWidths = []

  AddFeedItem(data, "123", 71.0, columnWidths)
  AddFeedItem(data, "Space", 102.0, columnWidths)

  for x = 97 to 122
    AddFeedItem(data, Chr(x), 42.0, columnWidths)
  end for

  AddFeedItem(data, "delete", 64.0, columnWidths)

  m.TopSearchList.content = data
  m.TopSearchList.columnWidths = columnWidths
  m.TopSearchList.setFocus(true)
end function

function setNumericMode()
  data = CreateObject("roSGNode", "ContentNode")
  columnWidths = []

  AddFeedItem(data, "abc", 70.0, columnWidths)
  AddFeedItem(data, "Space", 102.0, columnWidths)

  char = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", ",", ":", ";", "!", "@", "#", "$", "%", "&", "(", ")", "_", "-", "+", "?"]
  for each value in char
    AddFeedItem(data, value, 42.0, columnWidths)
  end for

  AddFeedItem(data, "delete", 64.0, columnWidths)

  m.TopSearchList.content = data
  m.TopSearchList.columnWidths = columnWidths
  m.TopSearchList.setFocus(true)
end function

function AddFeedItem(data as object, searchText as string, itemWidth as float, widths as object) as void
  dataItem = data.CreateChild("feedListItemDataModel")
  if not dataItem.hasField("FHDItemWidth")
    dataItem.addField("FHDItemWidth", "float", false)
  end if
  if searchText = "delete" then
    dataItem.searchText = ""
    dataItem.spaceIcon = "pkg://images/delete_icon.png"
    dataItem.addField("isDelete", "boolean", false)
    dataItem.isDelete = true
  else
    dataItem.searchText = searchText
  end if
  dataItem.FHDItemWidth = itemWidth

  widths.Push(itemWidth + 10)
end function

sub onSearchTextChanged(event as object)
  m.top.searchText = event.getData()
end sub


function recentSearchList()
  searchRequest = TVService(m.apiProvider).getRecentSearchList(m.login_Token)
  searchRequest.httpResponse.observeFieldScoped("response", "onRecentSearchList")
  m.http.sendRequest(searchRequest)
end function

sub onRecentSearchList(event)
  response = event.getData()
  content = CreateObject("roSGNode", "ContentNode")

  if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
    recentSearchListItem = response.data.data.data

    for each item in recentSearchListItem
      child = content.createChild("MenuViewModel")
      child.title = item.term
    end for
    m.recentSearchList.content = content
    m.recentSearchList.visible = true
  end if

end sub

function makeSearch()
  search_Text = m.top.searchText.trim()
  encodedSearchText = search_Text.toStr().replace(" ", "%20")
  m.searchRequest = TVService(m.apiProvider).getSearchResult(m.login_Token, encodedSearchText)
  m.searchRequest.httpResponse.observeFieldScoped("response", "onSearchResults")
  m.http.sendRequest(m.searchRequest)
end function

function onSearchResults(event)
  response = event.getData()
  feedList = createObject("RoSGNode", "ContentNode")

  if isValid(response) and isValid(response.data) and isValid(response.data.data)
    searchList = response.data.data

    m.searchList.content = invalid

    if searchList.count() > 0
      m.searchNotFoundLbl.visible = false

      for i = 0 to searchList.count() - 1
        contentNode = feedList.createChild("feedListItemDataModel")
        contentNode.callfunc("parseData", searchList[i])
      end for

      m.searchList.content = feedList
      m.searchList.visible = true

      if m.rowListIndex <> invalid
        m.searchList.jumpToItem = m.rowListIndex
        applyFocus(m.searchList, true, "onNavigateTo() - SearchController.brs")
        m.rowListIndex = invalid
      end if
    else
      showNoResultsMessage()
    end if
  else
    showNoResultsMessage()
  end if
end function

function showNoResultsMessage()
  m.searchList.content = invalid
  m.searchList.visible = false

  if m.searchNotFoundLbl = invalid
    m.searchNotFoundLbl = m.top.findNode("searchNotFoundLbl")
  end if
  m.searchNotFoundLbl.visible = true
end function

function onSearchListItemSelected(event as object)
  selectedIndex = event.getData()
  selectedItem = m.searchList.content.getChild(selectedIndex)
  m.selectedGridId = selectedIndex
  selectedId = selectedItem.id
  content_type = selectedItem.content_type

  postRecentSearchList(selectedId, content_type)

  if selectedItem.content_type = "Live Channel"
    pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: selectedItem.slug, isResume: true, content_Id: invalid, content_Type: selectedItem.content_Type, content: invalid, currentIndex: 0 })

  else if selectedItem.content_type = "Creator"
    pageInfo = createPageInfo(m.constants.CONTROLLERS.CREATORDETAIL, { creatorIndex: selectedId })
  else
    pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex, content_Type: selectedItem.content_Type })
  end if

  m.scene.loadController = pageInfo
end function

function postRecentSearchList(selectedId, content_type)
  searchRequest = TVService(m.apiProvider).postRecentSearchList(m.login_Token, selectedId, content_type)
  searchRequest.httpResponse.observeFieldScoped("response", "onPostRecentSearchList")
  m.http.sendRequest(searchRequest)
end function

sub onPostRecentSearchList(event)
  response = event.getData()
end sub

sub onRecentSearchListSelected(event)
  selectedIndex = event.getData()
  selectedItem = m.recentSearchList.content.getChild(selectedIndex)
  m.searchBox.text = selectedItem.title

  m.recentSearchList.jumpToItem = selectedIndex
  m.recentSearchList.setFocus(true)
  makeSearch()
end sub

function onLeftNavFocusEscape(event as object) as void
  escapeStatus = event.getData()
  if escapeStatus = true
    OnKeyEvent(m.constants.remote_keys.RIGHT, true)
  end if
end function

function OnKeyEvent(key as string, press as boolean) as boolean
  handled = false

  if press
    if key = m.constants.REMOTE_KEYS.BACK
      if m.goBackToSliderSearch = true
        goBackInHistory(m.scene)
      else if m.leftMenu.isExpanded = false
        m.leftMenu.isExpanded = true
        applyFocus(m.leftMenu, true, "onNavigateTo() - HomeController.brs")
        handled = true
      end if
      handled = true

    else if key = m.constants.remote_keys.RIGHT
      if m.TopSearchList.isInFocusChain() and m.TopSearchList.itemFocused = m.TopSearchList.content.getChildCount() - 1
        m.TopSearchList.jumpToItem = 0
        handled = true
      else if m.recentSearchList.hasFocus()
        applyFocus(m.searchList, true, "onNavigateTo() - SearchController.brs")
        handled = true
      else
        applyFocus(m.TopSearchList, true, "onNavigateTo() - SearchController.brs")
      end if

    else if key = m.constants.remote_keys.LEFT
      if m.TopSearchList.isInFocusChain() and m.TopSearchList.itemFocused = 0
        m.TopSearchList.jumpToItem = m.TopSearchList.content.getChildCount() - 1
        handled = true
      else if m.searchList.hasFocus()
        applyFocus(m.recentSearchList, true, "onNavigateTo() - SearchController.brs")
        handled = true
      end if

    else if key = m.constants.remote_keys.DOWN
      if (m.searchList.content <> invalid and m.searchList.content.getChild(0) <> invalid)
        if m.TopSearchList.hasFocus()
          applyFocus(m.searchList, true, "onNavigateTo() - SearchController.brs")
          handled = true
        else
          handled = true
        end if
      else
        applyFocus(m.recentSearchList, true, "onNavigateTo() - SearchController.brs")
        handled = true
      end if

    else if key = m.constants.remote_keys.UP
      if m.searchList.hasFocus() or m.recentSearchList.hasFocus()
        applyFocus(m.TopSearchList, true, "onNavigateTo() - SearchController.brs")
        handled = true
      end if

    end if
  end if

  return handled
end function