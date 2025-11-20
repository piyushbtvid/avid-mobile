function init()
    m.top.focusable = true
    m.top.id = "settingsMenu"
    m.constants = GetConstants()
    m.scene = getScene()

    m.navLogo = m.top.findNode( "navLogo" )
    m.navButtons = m.top.findNode( "navButtons" )
    m.slideAnimation = m.top.findNode( "slideAnimation" )
    m.slideInterpolator = m.slideAnimation.findNode( "slideInterpolator" )
    m.top.observeField( "focusedChild", "onFocus" )

    m.returnedFromLegalAndSupport = false
  end function

  function onFocus( event )

    navBarHasFocus = m.top.hasFocus()

    if ( navBarHasFocus )
        m.navButtons.setFocus( true )
    end if

    if isValid(m.scene.currentController) AND isValid(m.scene.loadController)
      if m.scene.loadController.page = m.constants.CONTROLLERS.LEGAL_AND_SUPPORT AND m.scene.currentController.id <> m.constants.CONTROLLERS.LEGAL_AND_SUPPORT AND m.returnedFromLegalAndSupport
        accountSettingsView = m.scene.findNode("accountSettingsView")
        accountSettingsView.hide = false
        m.returnedFromLegalAndSupport = false
      end if
    end if

  end function

  function onNavItemsChange( event as Object )

    navItems = m.top.navItems

    totalButtons = m.navButtons.getChildCount()
    m.navButtons.removeChildrenIndex( totalButtons, 0 )

    totalButtonsWidth = 0
    for i = 0 to navItems.count() - 1
      navItem = navItems[i]
      btn = createNavButton( navItem, i )
      totalButtonsWidth = totalButtonsWidth + btn.minWidth
      m.navButtons.appendChild( btn )
    end for

    ' Size Screen + nav right padding + item spacings + nav left padding
    profileItemSpacing = 1920 - 200 - 80 - totalButtonsWidth - 50
    m.navButtons.itemSpacings = [20, 20, 20, 20, 20 ]

    ' Restoring the previous focus position during top nav refresh
    if isValid( m.top.focusedItem ) and m.top.focusedItem <> -1 then m.top.focusedItem = m.top.focusedItem

  end function

  '***** Helpers ******

  ' Creates a nav button to be added on to the button group
  ' @param object name
  ' @return object Button
  function createNavButton( imgConfig as Object, index as integer)

    labelButton = CreateObject("roSGNode", "MenuButton")
    labelButton.id = imgConfig.id
    labelButton.index = index
    labelButton.label = imgConfig.text
    labelButton.icon = imgConfig.icon

    return labelButton

  end function


  function onFocusedItemChanged( nodeEvent as object )
    focusedItem = nodeEvent.getData()
    for i = 0 to m.navButtons.getChildCount() - 1
      button = m.navButtons.getChild(i)
      if button.subtype() <> "Timer"
        button.selectButton = focusedItem = button.index
        button.isKidsMode = m.top.isKidsMode
      end if
    end for
  end function

  ' function onTopNavItemSelected(event as object) as void

  '   selectedIndex = event.getData()

  '   selectedItem = m.top.navItems[selectedIndex]

  '   if selectedIndex <> 6
  '     m.top.focusedItem = selectedIndex
  '   end if

  '   if(selectedIndex = 0)
  '     pageInfo = createPageInfo(m.constants.CONTROLLERS.LOGIN, {
  '     })
  '     if pageInfo.page = m.scene.currentController.id then return
  '     m.scene.loadController = pageInfo

  '   end if

  ' end function
