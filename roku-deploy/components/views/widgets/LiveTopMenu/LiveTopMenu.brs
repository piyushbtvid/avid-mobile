sub init()
  m.top.observeField("focusedChild", "onFocusChange")

  m.topLiveMenu = m.top.findNode("topLiveMenu")
  m.background = m.top.findNode("background")
  m.scene = getScene()
  m.constants = GetConstants()

  m.topLiveMenu.observeField("rowItemFocused", "onItemFocused")
  m.topLiveMenu.observeField("rowItemSelected", "onItemSelected")

  m.profileButton = m.top.findNode("profileButton")
  m.profileButton.imgSrc = { label: "", unFocusImg: "pkg://images/profile_icon.png", focusImg: "pkg://images/focus_profile_icon.png", width: 30, height: 30 }

  m.micButton = m.top.findNode("micButton")
  m.searchButton = m.top.findNode("searchButton")
  m.micButton.imgSrc = { label: "", unFocusImg: "pkg://images/macIcon.png", focusImg: "pkg://images/focusMacIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }
  m.searchButton.imgSrc = { label: "", unFocusImg: "pkg://images/searchIcon.png", focusImg: "pkg://images/FocusSearchIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }

  m.leftMenu = m.scene.findNode("leftMenu")
  m.leftMenu.visible = false

  createMenuContent()
end sub

sub createMenuContent()
  content = CreateObject("roSGNode", "ContentNode")
  row = content.createChild("ContentNode")
  items = [
    { "title": "STREAM" },
    { "title": "LIVE" },
    { "title": "BROWSE" }
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
      baseWidth = 140
    else
      baseWidth = 269
    end if

    child.FHDItemWidth = baseWidth
  end for
  m.topLiveMenu.content = content
  m.topLiveMenu.jumpToRowItem = [0, 0]

  ' Set initial focus
  applyFocus(m.topLiveMenu, true, "onNavigateTo() - LiveTvController.brs")
end sub

function onFocusChange(event)
  response = event.getData()
  if m.top.hasFocus() and not m.topLiveMenu.hasFocus() and not m.micButton.hasFocus() and not m.searchButton.hasFocus()
    applyFocus(m.topLiveMenu, true, "onNavigateTo() - LiveTvController.brs")
    for r = 0 to m.topLiveMenu.content.getChildCount() - 1
      row = m.topLiveMenu.content.getChild(r)
      for i = 0 to row.getChildCount() - 1
        item = row.getChild(i)
        if (item.isSelected) then m.topLiveMenu.jumpToRowItem = [r, i]
      end for
    end for
  end if
end function

sub onItemFocused()
  focusedIndex = m.topLiveMenu.itemFocused
  ' Clear focus from all items first
  m.top.rightNavEscape = true
end sub

sub onItemSelected(event as object)
  selectedIndex = event.getData()
  rowIndex = selectedIndex[0]
  itemIndex = selectedIndex[1]

  ' Clear selection from all items
  for r = 0 to m.topLiveMenu.content.getChildCount() - 1
    row = m.topLiveMenu.content.getChild(r)
    for i = 0 to row.getChildCount() - 1
      item = row.getChild(i)
      item.isSelected = false
    end for
  end for

  selectedRow = m.topLiveMenu.content.getChild(rowIndex)
  if selectedRow <> invalid
    selectedItem = selectedRow.getChild(itemIndex)
    if selectedItem <> invalid
      if m.top.itemSelected <> -1 then selectedItem.isSelected = true
      m.top.stopVideo = true


      if selectedItem.title = "STREAM"
        m.scene.loadController = { page: m.constants.CONTROLLERS.STREAM, params: {} }
        m.top.itemSelected = 0
        m.leftMenu.itemSelected = 1
      else if selectedItem.title = "LIVE"
        m.scene.loadController = { page: m.constants.CONTROLLERS.LIVETVSTREAM, params: {} }
      else if selectedItem.title = "BROWSE"
        'm.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
      end if
    end if
    applyFocus(m.topLiveMenu, true, "onNavigateTo() - LiveTvController.brs")
  end if
end sub


function onKeyEvent(key as string, press as boolean) as boolean
  if not press then return false

  handled = false

  if key = m.constants.remote_keys.RIGHT
    if m.topLiveMenu.hasFocus()
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
      applyFocus(m.topLiveMenu, true, "onNavigateTo() - LiveTopMenu.brs")
      handled = true
    else if m.topLiveMenu.hasFocus()
      if m.scene.currentController.id = "VideoController"
        'm.top.stopVideo = true
        'goBackInHistory(m.scene)
        handled = true
      else
        m.top.stopVideo = true
        goBackInHistory(m.scene)
        handled = true
      end if
    end if
  else if key = m.constants.remote_keys.DOWN
    m.top.downTopNavEscape = true
    handled = true

  else if key = m.constants.remote_keys.BACK

    if m.scene.currentController.id = "VideoController"
      m.top.stopVideo = true
      goBackInHistory(m.scene)
      handled = true
    else
      m.top.stopVideo = true
        goBackInHistory(m.scene)
        handled = true
    end if
  end if

  return handled
end function

sub onLiveVideoPlay()
  if m.top.itemSelected <> invalid
    selectedIndex = m.top.itemSelected
    contentGroup = m.topLiveMenu.content.getChild(0)

    for i = 0 to contentGroup.getChildCount() - 1
      item = contentGroup.getChild(i)
      item.isSelected = (i = selectedIndex)
    end for
  end if
end sub
