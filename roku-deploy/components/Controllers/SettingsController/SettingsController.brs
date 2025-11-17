function Init()
  m.top.id = "SettingsController"

  m.constants = GetConstants()
  m.scene = getScene()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)
  m.topNav = m.scene.findNode("topNav")
  m.topNav.visible = true
  m.topNav.focusedItem = 6

  m.menu = m.top.findNode("menu")
  m.menu.observeFieldScoped("selectedItemIndex", "onMenuItemSelected")
  m.menu.focusedItem = 0

  m.profileGroup = m.top.findNode("profileGroup")
  m.aboutUsGroup = m.top.findNode("aboutUsGroup")
  m.termsGroup = m.top.findNode("termsGroup")
  m.privacyGroup = m.top.findNode("privacyGroup")
  m.contactGroup = m.top.findNode("contactGroup")
  m.boundingRect = m.top.findNode("boundingRect")
  m.switchProfButton = m.top.findNode("switchProfButton")
  m.switchProfButton.observeFieldScoped("buttonSelected", "onSwitchProfButtonSelected")
  m.profileNameValue = m.top.findNode("profileNameValue")
  m.profileEmailValue = m.top.findNode("profileEmailValue")
  m.profileMobileValue = m.top.findNode("profileMobileValue")

  m.topNav.observeFieldScoped("escape", "onTopNavFocusEscape")
  m.menu.observeFieldScoped("escape", "onMenuFocusEscape")
  m.menu.observeFieldScoped("escapeRight", "onMenuFocusEscapeRight")

  m.isPremiumUser = false

end function

function setupMenu(userdetails)

  m.isUserLoggedIn = false
  m.isPremiumUser = false
  m.isUserRokuPremium = false
   loginInfo = getLoginInfo()
   if loginInfo <> invalid
    m.isUserLoggedIn = loginInfo.isLogin
    m.isPremiumUser = loginInfo.isPremiumUser
   end if 

   m.isUserRokuPremium = isRokuSubscribed()
   
  if m.isUserRokuPremium = true or m.isPremiumUser = true
    if m.isUserLoggedIn = true
      setUpUserProfile(LocalStorage().getValueForKey("userProfileInfo"))
      m.menu.navItems = m.constants.SETTINGS_MENU_BUTTON_ICONS_LOGGEDIN_PREMIUM
      m.menu.refresh = true
      m.menu.translation = [-620, 415]
    else
      m.menu.navItems = m.constants.ROKU_PREMIUM_SETTINGS_MENU_BUTTON_ICONS
      m.menu.refresh = true
      m.menu.translation = [-620, 370]
    end if
  else
    if m.isUserLoggedIn = true
      setUpUserProfile(LocalStorage().getValueForKey("userProfileInfo"))
      m.menu.navItems = m.constants.SETTINGS_MENU_BUTTON_ICONS_LOGGEDIN_NOTPREMIUM
      m.menu.refresh = true
      m.menu.translation = [-620, 450]
    else
      m.menu.navItems = m.constants.SETTINGS_MENU_BUTTON_ICONS
      m.menu.refresh = true
      m.menu.translation = [-620, 415]
    end if

  end if

end function

sub setUpUserProfile(info)

  profileName = LocalStorage().getValueForKey("profileName")
  if info <> invalid
    data = ParseJson(info)
    if data <> invalid
      m.profileNameValue.text = profileName
      m.profileEmailValue.text = data.email
      m.profileMobileValue.text = data.phone
    end if
  end if
end sub

function onNavigateTo(param as object)

  userdetails = param.userdetails

  setupMenu(userdetails)

  applyFocus(m.menu, true, "onNavigateTo() - SettingsController.brs")

end function

function onTopNavFocusEscape(event as object) as void

  escapeStatus = event.getData()
  if not escapeStatus then return
  focusMap = createFocusMap()
  currentFocusedItem = getCurrentFocusItem(m.scene)
  componentFocusHandler(m.constants.REMOTE_KEYS.RIGHT, focusMap, currentFocusedItem)

end function

function onMenuFocusEscape(event as object) as void

  escapeStatus = event.getData()
  if not escapeStatus then return
  focusMap = createFocusMap()
  currentFocusedItem = getCurrentFocusItem(m.scene)
  componentFocusHandler(m.constants.REMOTE_KEYS.LEFT, focusMap, currentFocusedItem)

end function

function onMenuFocusEscapeRight(event as object) as void

  escapeStatus = event.getData()
  if not escapeStatus then return
    if m.profileGroup.visible = true
      focusMap = createFocusMap()
      currentFocusedItem = getCurrentFocusItem(m.scene)
      componentFocusHandler(m.constants.REMOTE_KEYS.RIGHT, focusMap, currentFocusedItem)
    end if
end function

function onMenuItemSelected(event as object) as void

  selectedIndex = event.getData()


  if selectedIndex <> 8
    m.menu.focusedItem = selectedIndex
  end if

  if m.isUserRokuPremium = true or m.isPremiumUser= true

    if m.isUserLoggedIn = true
      if(selectedIndex = 0)

        m.profileGroup.visible = false
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = true
        m.privacyGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true

      else if(selectedIndex = 1)

        m.profileGroup.visible = true
        m.boundingRect.visible = true
        m.aboutUsGroup.visible = false
        m.termsGroup.visible = false
        m.privacyGroup.visible = false
        m.contactGroup.visible = false

      else if (selectedIndex = 2)

        m.profileGroup.visible = false
        m.privacyGroup.visible = false
        m.termsGroup.visible = true
        m.aboutUsGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true

      else if (selectedIndex = 3)

        m.profileGroup.visible = false
        m.privacyGroup.visible = true
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true

      else if (selectedIndex = 4)

        m.profileGroup.visible = false
        m.contactGroup.visible = true
        m.privacyGroup.visible = false
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.boundingRect.visible = true

      else if (selectedIndex = 5)
        signOutDialog = CreateObject("roSGNode", "Dialog")

        signOutDialog.title = "Sign Out"
        signOutDialog.message = "Are you sure you want to SignOut?"
        signOutDialog.buttons = ["Cancel", "Yes"]
        signOutDialog.observeField("buttonSelected", "onButtonSelected")
        m.scene.dialog = signOutDialog

      else if (selectedIndex = 6)
        pageInfo = createPageInfo(m.constants.CONTROLLERS.FAQ, {})
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      end if
    else
      if(selectedIndex = 0)

        m.boundingRect.visible = true
        m.aboutUsGroup.visible = true
        m.termsGroup.visible = false
        m.privacyGroup.visible = false
        m.contactGroup.visible = false

      else if(selectedIndex = 1)

        m.termsGroup.visible = true
        m.aboutUsGroup.visible = false
        m.privacyGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true

      else if (selectedIndex = 2)

        m.privacyGroup.visible = true
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true

      else if (selectedIndex = 3)

        m.contactGroup.visible = true
        m.privacyGroup.visible = false
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.boundingRect.visible = true

      else if(selectedIndex = 4)

        pageInfo = createPageInfo(m.constants.CONTROLLERS.LOGIN, {})
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      else if (selectedIndex = 5)
        pageInfo = createPageInfo(m.constants.CONTROLLERS.FAQ, {})
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      end if
    end if
  else
    if m.isUserLoggedIn = true
      if(selectedIndex = 0)

        m.boundingRect.visible = true
        m.aboutUsGroup.visible = true
        m.termsGroup.visible = false
        m.privacyGroup.visible = false
        m.contactGroup.visible = false
        m.profileGroup.visible = false

      else if(selectedIndex = 1)

        m.profileGroup.visible = true
        m.boundingRect.visible = true
        m.aboutUsGroup.visible = false
        m.termsGroup.visible = false
        m.privacyGroup.visible = false
        m.contactGroup.visible = false

      else if (selectedIndex = 2)

        m.privacyGroup.visible = false
        m.termsGroup.visible = true
        m.aboutUsGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true
        m.profileGroup.visible = false

      else if (selectedIndex = 3)

        m.contactGroup.visible = false
        m.privacyGroup.visible = true
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.boundingRect.visible = true
        m.profileGroup.visible = false

      else if(selectedIndex = 4)

        m.contactGroup.visible = true
        m.privacyGroup.visible = false
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.boundingRect.visible = true
        m.profileGroup.visible = false

      else if(selectedIndex = 5)

        signOutDialog = CreateObject("roSGNode", "Dialog")
        signOutDialog.title = "Sign Out"
        signOutDialog.message = "Are you sure you want to SignOut?"
        signOutDialog.buttons = ["Cancel", "Yes"]
        signOutDialog.observeField("buttonSelected", "onButtonSelected")
        m.scene.dialog = signOutDialog

      else if(selectedIndex = 6)
        comingFromSetting = "true"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.PREMIUM, { fromSetting: comingFromSetting })
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      else if (selectedIndex = 7)
        pageInfo = createPageInfo(m.constants.CONTROLLERS.FAQ, {})
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      end if
    else
      if(selectedIndex = 0)

        m.boundingRect.visible = true
        m.aboutUsGroup.visible = true
        m.termsGroup.visible = false
        m.privacyGroup.visible = false
        m.contactGroup.visible = false

      else if(selectedIndex = 1)

        m.termsGroup.visible = true
        m.aboutUsGroup.visible = false
        m.privacyGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true

      else if (selectedIndex = 2)

        m.privacyGroup.visible = true
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.contactGroup.visible = false
        m.boundingRect.visible = true

      else if (selectedIndex = 3)

        m.contactGroup.visible = true
        m.privacyGroup.visible = false
        m.termsGroup.visible = false
        m.aboutUsGroup.visible = false
        m.boundingRect.visible = true

      else if(selectedIndex = 4)

        pageInfo = createPageInfo(m.constants.CONTROLLERS.LOGIN, {})
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      else if(selectedIndex = 5)

        pageInfo = createPageInfo(m.constants.CONTROLLERS.LOGIN, {})
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      else if (selectedIndex = 6)
        pageInfo = createPageInfo(m.constants.CONTROLLERS.FAQ, {})
        if pageInfo.page = m.scene.currentController.id then return
        m.scene.loadController = pageInfo

      end if
    end if
  end if

end function

function onButtonSelected(event)

  buttonIndex = event.getData()

  if buttonIndex = 0
    m.scene.dialog.close = true
  else
    LocalStorage().removeKey("status")
    LocalStorage().removeKey("token")
    LocalStorage().removeKey("isPremiumUser")
    LocalStorage().removeKey("userProfileInfo")
    LocalStorage().removeKey("selectedProfile")
    LocalStorage().removeKey("profileName")
    setLoginInfo(invalid)
    pageInfo = createPageInfo(m.constants.CONTROLLERS.SETTINGS, {})
    m.scene.loadController = pageInfo
    m.scene.dialog.close = true

  end if

end function

function onSwitchProfButtonSelected()
  pageInfo = createPageInfo(m.constants.CONTROLLERS.PROFILE, {})
  m.scene.loadController = pageInfo
end function

function OnKeyEvent(key as string, press as boolean) as boolean

  handled = false
  focusMap = createFocusMap()
  currentFocusMap = getCurrentFocusItem(m.scene)

  if (press)

    if not handled
      if key = m.constants.REMOTE_KEYS.BACK
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

  focusMap[m.menu.id] = { up: invalid, down: invalid, Left: m.topNav.id: Right: m.switchProfButton.id }
  focusMap[m.topNav.id] = { up: invalid, down: invalid, Left: invalid: Right: m.menu.id }
  focusMap[m.switchProfButton.id] = { up: invalid, down: invalid, Left: m.menu.id: Right: invalid }
  return focusMap

end function