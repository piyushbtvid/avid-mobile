sub init()
  m.top.observeField("focusedChild", "onFocusChange")

  m.browseTopLiveMenu = m.top.findNode("browseTopLiveMenu")
  m.background = m.top.findNode("background")
  m.scene = getScene()
  m.constants = GetConstants()

  m.browseTopLiveMenu.observeField("rowItemFocused", "onItemFocused")
  m.browseTopLiveMenu.observeField("rowItemSelected", "onItemSelected")

  m.profileButton = m.top.findNode("profileButton")
  m.profileButton.imgSrc = { label: "", unFocusImg: "pkg://images/profile_icon.png", focusImg: "pkg://images/focus_profile_icon.png", width: 30, height: 30 }

  m.micButton = m.top.findNode("micButton")
  m.searchButton = m.top.findNode("searchButton")
  m.micButton.imgSrc = { label: "", unFocusImg: "pkg://images/macIcon.png", focusImg: "pkg://images/focusMacIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }
  m.searchButton.imgSrc = { label: "", unFocusImg: "pkg://images/searchIcon.png", focusImg: "pkg://images/FocusSearchIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }

  m.leftMenu = m.scene.findNode("leftMenu")
  m.leftMenu.visible = false

  ' Create menu content
  createMenuContent()
end sub

sub createMenuContent()
  content = CreateObject("roSGNode", "ContentNode")
  row = content.createChild("ContentNode")
  items = [
    { "title": "LIVE" },
    { "title": "GUIDE" },
    { "title": "MY CHANNEL" }
  ]

  for each item in items
    child = row.createChild("MenuViewModel")
    child.addField("FHDItemWidth", "float", false)
    child.title = item.title
    child.isSelected = false

    textLength = item.title.len()
    if textLength <= 4
      baseWidth = 100
    else if textLength <= 6
      baseWidth = 100
    else if textLength <= 10
      baseWidth = 150
    else
      baseWidth = 269
    end if

    child.FHDItemWidth = baseWidth
  end for
  m.browseTopLiveMenu.content = content
  m.browseTopLiveMenu.jumpToRowItem = [0, 0]

  ' Set initial focus
  applyFocus(m.browseTopLiveMenu, true, "onNavigateTo() - BrowseTvController.brs")
end sub

function onFocusChange(event)
  response = event.getData()
  if m.top.hasFocus() and not m.browseTopLiveMenu.hasFocus() and not m.micButton.hasFocus() and not m.searchButton.hasFocus()
    applyFocus(m.browseTopLiveMenu, true, "onNavigateTo() - LiveTvController.brs")
    for r = 0 to m.browseTopLiveMenu.content.getChildCount() - 1
      row = m.browseTopLiveMenu.content.getChild(r)
      for i = 0 to row.getChildCount() - 1
        item = row.getChild(i)
        if (item.isSelected) then m.browseTopLiveMenu.jumpToRowItem = [r, i]
      end for
    end for
  end if
end function

sub onItemFocused()
  focusedIndex = m.browseTopLiveMenu.itemFocused
  ' Clear focus from all items first
  m.top.rightNavEscape = true
end sub

sub onItemSelected(event as object)
  selectedIndex = event.getData()
  rowIndex = selectedIndex[0]
  itemIndex = selectedIndex[1]

  ' Clear selection from all items
  for r = 0 to m.browseTopLiveMenu.content.getChildCount() - 1
    row = m.browseTopLiveMenu.content.getChild(r)
    for i = 0 to row.getChildCount() - 1
      item = row.getChild(i)
      item.isSelected = false
    end for
  end for

  ' Set selection on the new item
  selectedRow = m.browseTopLiveMenu.content.getChild(rowIndex)
  if selectedRow <> invalid
    selectedItem = selectedRow.getChild(itemIndex)
    if selectedItem <> invalid
      selectedItem.isSelected = true
      print "Selected item: "; selectedItem.title

      ' Handle navigation
      if selectedItem.title = "LIVE"
        m.scene.loadController = { page: m.constants.CONTROLLERS.LIVETVSTREAM, params: {} }
        m.top.itemSelected = 0
        m.leftMenu.itemSelected = 1
      else if selectedItem.title = "GUIDE"
        m.scene.loadController = { page: m.constants.CONTROLLERS.GUIDE, params: {} }
      else if selectedItem.title = "MY CHANNEL"
        'm.scene.loadController = { page: m.constants.CONTROLLERS.GUIDE, params: {} }
      end if
    end if
    applyFocus(m.browseTopLiveMenu, true, "onNavigateTo() - LiveTvController.brs")
  end if
end sub


function onKeyEvent(key as string, press as boolean) as boolean
  if not press then return false

  handled = false

  if key = m.constants.remote_keys.RIGHT
    if m.browseTopLiveMenu.hasFocus()
      applyFocus(m.micButton, true, "onNavigateTo() - LiveTopMenu.brs")
      m.top.rightNavEscape = true
      handled = true
    else if m.micButton.hasFocus()
      m.top.rightNavEscape = true
      applyFocus(m.searchButton, true, "onNavigateTo() - LiveTopMenu.brs")
      handled = true
    else if m.searchButton.hasFocus()
      applyFocus(m.profileButton, true, "onNavigateTo() - LiveTopMenu.brs")
    end if
  else if key = m.constants.remote_keys.LEFT
    if m.profileButton.hasFocus()
      applyFocus(m.searchButton, true, "onNavigateTo() - LiveTopMenu.brs")
      handled = true
    else if m.searchButton.hasFocus()
      m.top.rightNavEscape = true
      applyFocus(m.micButton, true, "onNavigateTo() - LiveTopMenu.brs")
      handled = true
    else if m.micButton.hasFocus()
      applyFocus(m.browseTopLiveMenu, true, "onNavigateTo() - LiveTopMenu.brs")
      handled = true
    else if m.browseTopLiveMenu.hasFocus()
      handled = true
    end if
  else if key = m.constants.remote_keys.DOWN
    m.top.downTopNavEscape = true
    handled = true
  else if key = m.constants.remote_keys.UP
    m.scene.loadController = { page: m.constants.CONTROLLERS.STREAM, params: {} }
    handled = true
  else if key = m.constants.remote_keys.BACK
    goBackInHistory(m.scene)
    ' m.scene.loadController = { page: m.constants.CONTROLLERS.STREAM, params: {} }
    handled = true
  end if

  return handled
end function

sub onLiveVideoPlay()
  if m.top.itemSelected <> invalid and m.top.itemSelected >= 0
    selectedIndex = m.top.itemSelected
    contentGroup = m.browseTopLiveMenu.content.getChild(0)

    for i = 0 to contentGroup.getChildCount() - 1
      item = contentGroup.getChild(i)
      item.isSelected = (i = selectedIndex)
    end for
  end if
end sub
