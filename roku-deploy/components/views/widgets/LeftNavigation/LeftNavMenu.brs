sub init()
    m.navList = m.top.findNode("navList")
    m.backgroundExpand = m.top.findNode("backgroundExpand")
    m.backgroundColapse = m.top.findNode("backgroundColapse")
    m.scene = getScene()
    m.constants = GetConstants()

    m.top.observeFieldScoped("focusedChild", "onFocusChange")

    m.navList.observeField("itemFocused", "onItemFocused")
    m.navList.observeField("itemSelected", "onItemSelected")
    m.navListCollapse = m.top.findNode("navListCollapse")
    m.collapseTimer = m.top.findNode("collapseTimer")
    m.collapseTimer.observeField("fire", "onCollapseFinish")

    m.expandAnim = m.top.findNode("expandAnim")
    m.collapseAnim = m.top.findNode("collapseAnim")
    m.navListExpand = m.top.findNode("navListExpand")
    m.scene.observeField("userConfigData", "handleConfigData")

end sub

sub handleConfigData()

    items = m.top.navItems
    content = CreateObject("roSGNode", "ContentNode")
    userConfigData = m.scene.userConfigData


    for each item in items
        child = content.createChild("MenuViewModel")
        child.title = item.title
        child.image = item.image
        child.expanded = true
        child.isSelected = false
    end for

    m.navList.content = content
    m.top.isExpanded = false
    m.navList.jumpToItem = 1

    creatorEnabled = (userConfigData.enable_creator = "true")
    seriesEnabled = (userConfigData.enable_series = "true")
    movieEnabled = (userConfigData.enable_movie = "true")


    if not creatorEnabled and not seriesEnabled and not movieEnabled
        ' all disabled
        m.navList.rowHeights = "[78,78,594,78]"

    else if creatorEnabled and movieEnabled and not seriesEnabled
        ' only series disabled
        m.navList.rowHeights = "[78,78,78,78,438,78]"

    else if creatorEnabled and seriesEnabled and not movieEnabled
        ' only movie disabled
        m.navList.rowHeights = "[78,78,78,78,438,78]"

    else if not creatorEnabled and seriesEnabled and movieEnabled
        ' only creator disable
        m.navList.rowHeights = "[78,78,78,78,438,78]"

    else if creatorEnabled and not seriesEnabled and not movieEnabled
        ' only creator enable
        m.navList.rowHeights = "[78,78,78,516,78]"

    else if not creatorEnabled and seriesEnabled and not movieEnabled
        ' only series enable
        m.navList.rowHeights = "[78,78,78,516,78]"

    else if not creatorEnabled and not seriesEnabled and movieEnabled
        ' only movie enable
        m.navList.rowHeights = "[78,78,78,516,78]"

    else
        ' all enabled
        m.navList.rowHeights = "[78,78,78,78,78,360,78]"
    end if
    homeMenuFirstLaunchSelected(1)
end sub

function onFocusChange(event)
    response = event.getData()
    if m.top.hasFocus() = true
        m.navList.setFocus(true)
        m.top.isExpanded = true
        m.navList.jumpToItem = response.itemSelected
    end if
end function

function onExpanded()
    if m.top.isExpanded
        m.backgroundExpand.visible = true
        m.backgroundColapse.visible = false
        m.expandAnim.control = "start"
        m.navListExpand.control = "start"
    else
        m.collapseAnim.control = "start"
        m.navListCollapse.control = "start"
        m.collapseTimer.control = "start" ' Wait 300ms then hide expanded
    end if

    if m.navList.content <> invalid
        for i = 0 to m.navList.content.getChildCount() - 1
            item = m.navList.content.getChild(i)
            item.expanded = m.top.isExpanded
        end for
    end if
end function

sub onCollapseFinish()
    m.backgroundExpand.visible = false
    m.backgroundColapse.visible = true
end sub

sub onItemFocused()
    focusedIndex = m.navList.itemFocused
end sub

sub homeMenuFirstLaunchSelected(index)
    m.top.itemSelected = index
    for i = 0 to m.navList.content.getChildCount() - 1
        item = m.navList.content.getChild(i)
        item.isSelected = (i = index)
    end for
end sub

sub onItemSelected(event)
    m.top.itemSelected = event.getData()
    selectedItem = m.navList.content.getChild(m.top.itemSelected)
    for i = 0 to m.navList.content.getChildCount() - 1
        item = m.navList.content.getChild(i)
        item.isSelected = false
    end for
    m.navList.content.getChild(event.getData()).isSelected = true
    index = event.getData()

    homeMenuFirstLaunchSelected(index)
    if selectedItem.title = "Search"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.SEARCH, {})
        m.scene.loadController = pageInfo
    else if selectedItem.title = "Home"
        m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: { sectionID: 1 } }
    else if selectedItem.title = "My List"
        m.scene.loadController = { page: m.constants.CONTROLLERS.MYLIST, params: {} }
    else if selectedItem.title = "Creators"
        m.scene.loadController = { page: m.constants.CONTROLLERS.CREATOR, params: {} }
    else if selectedItem.title = "Series"
        m.scene.loadController = { page: m.constants.CONTROLLERS.SERIES, params: {} }
    else if selectedItem.title = "Movies"
        m.scene.loadController = { page: m.constants.CONTROLLERS.MOVIES, params: {} }
    else if selectedItem.title = "My Account"
        m.scene.loadController = { page: m.constants.CONTROLLERS.ACCOUNT, params: {} }
    else if selectedItem.title = "Log Out"
        logoutDialog = CreateObject("roSGNode", "Dialog")
        logoutDialog.title = "Logout"
        logoutDialog.message = "Are you sure you want to logout?"
        logoutDialog.buttons = ["Cancel", "Logout"]
        logoutDialog.observeField("buttonSelected", "onButtonSelected")
        m.scene.dialog = logoutDialog
    end if
    if selectedItem.title <> "Log Out"
        m.top.isExpanded = false
    end if
end sub

sub onButtonSelected(event)
    m.scene.dialog.close = true
    if event.getData() = 1
        homeMenuFirstLaunchSelected(1)
        getLogout()
        m.scene.findNode("backShadow").visible = false
        m.top.isExpanded = false
    end if
end sub

function onKeyEvent(key as string, press as boolean) as boolean
    if press
        if key = "back"
            ' Handle back key if needed
            return false
        end if
    end if
    return false
end function

sub isMenuItemSelected()
    if m.top.itemSelected <> invalid and m.top.itemSelected >= 0
        selectedIndex = m.top.itemSelected
        contentGroup = m.navList.content

        if contentGroup <> invalid
            for i = 0 to contentGroup.getChildCount() - 1
                item = contentGroup.getChild(i)
                item.isSelected = (i = selectedIndex)
            end for
        end if
    end if
end sub

function getLogout()
    login_Token = LocalStorage().getValueForKey("login_Token")
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)
    logoutPageContent = TVService(m.apiProvider).getLogout(login_Token)
    logoutPageContent.httpResponse.observeField("response", "onLogoutResponse")
    m.http.sendRequest(logoutPageContent)
    m.scene.callfunc("stopRefreshTimer")
end function

sub onLogoutResponse(event)
    response = event.getData()
    LocalStorage().removeKey("login_Token")
    LocalStorage().removeKey("refresh_token")
    LocalStorage().removeKey("expire_date")
    LocalStorage().removeKey("user_type")

    userConfigData = m.scene.userConfigData

    enable_qrlogin = invalid
    enable_login = invalid
    enable_creator = invalid

    if userConfigData <> invalid
        enable_qrlogin = userConfigData.enable_qrlogin
        enable_login = userConfigData.enable_login
        enable_creator = userConfigData.enable_creator
    end if

    if enable_qrlogin = "true" then
        m.scene.loadController = { page: m.constants.CONTROLLERS.QRCODE, params: {} }
    else if enable_login = "true" then
        m.scene.loadController = { page: m.constants.CONTROLLERS.LOGIN, params: {} }
    else
        m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
    end if
end sub