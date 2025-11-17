function init()
    m.top.focusable = true
    m.top.id = "SeasonsNavigationBar"
    m.constants = GetConstants()
    m.scene = getScene()

    m.navButtons = m.top.findNode( "seasonNavButtons" )
    m.slideAnimation = m.top.findNode( "slideAnimation" )
    m.slideInterpolator = m.slideAnimation.findNode( "slideInterpolator" )
    m.top.observeField( "focusedChild", "onFocus" )

    m.returnedFromLegalAndSupport = false

    m.top.observeFieldScoped("selectedItemIndex", "onTopNavItemSelected")

  end function


  function onFocus( event )

    navBarHasFocus = m.top.hasFocus()

    if ( navBarHasFocus )
        m.navButtons.setFocus( true )
    end if

    ' if m.top.focusedItemIndex= 4
    '   m.top.translation= [-1500,80]
    ' end if

    ' if m.top.focusedItemIndex= 3
    '   m.top.translation= [150,80]
    ' end if

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
      if navItem.name <> invalid
      navItem.name = navItem.name
      else
      navItem.name = navItem.id
      end if
      btn = createNavButton( navItem, i )
      totalButtonsWidth = totalButtonsWidth + btn.minWidth
      m.navButtons.appendChild( btn )
    end for

    'Size Screen + nav right padding + item spacings + nav left padding
    profileItemSpacing = 1920 - 200 - 80 - totalButtonsWidth - 100
    m.navButtons.itemSpacings = [30]

    ' Restoring the previous focus position during top nav refresh
    if isValid( m.top.focusedItem ) and m.top.focusedItem <> -1 then m.top.focusedItem = m.top.focusedItem

  end function

  '***** Helpers ******

  ' Creates a nav button to be added on to the button group
  ' @param object name
  ' @return object Button
  function createNavButton( imgConfig as Object, index as integer)

    labelButton = CreateObject("roSGNode", "SeasonsNavButton")
    labelButton.id = imgConfig.id
    labelButton.index = index
    labelButton.label = imgConfig.name
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

  function onTopNavItemSelected(event as object) as void

    selectedIndex = event.getData()

    selectedItem = m.top.navItems[selectedIndex]

    ' profile= m.top.getParent().getChild(4)
    ' support= m.top.getParent().getChild(5)
    ' privacy_Policy= m.top.getParent().getChild(6)
    ' terms_Conditions= m.top.getParent().getChild(7)
    ' about= m.top.getParent().getChild(8)
    ' cookie_Policy= m.top.getParent().getChild(9)
    ' packageGrid= m.top.getParent().getChild(11)
    ' packageGridText= m.top.getParent().getChild(12)


    if selectedIndex <> 7
      m.top.focusedItem = selectedIndex
    end if
    ' if(selectedIndex = 0)
    '   profile.visible= true
    '   support.visible= false
    '   privacy_Policy.visible= false
    '   cookie_Policy.visible= false
    '   terms_Conditions.visible= false
    '   about.visible= false
    '   packageGrid.visible= false
    '   packageGridText.visible= false
    ' else if (selectedIndex = 1)
    '   profile.visible= false
    '   support.visible= true
    '   privacy_Policy.visible= false
    '   cookie_Policy.visible= false
    '   terms_Conditions.visible= false
    '   about.visible= false
    '   packageGrid.visible= false
    '   packageGridText.visible= false
    ' else if (selectedIndex = 2)
    '   packageGridText.visible= true
    '   packageGrid.visible= true
    '   profile.visible= false
    '   support.visible= false
    '   privacy_Policy.visible= false
    '   cookie_Policy.visible= false
    '   terms_Conditions.visible= false
    '   about.visible= false
    ' else if (selectedIndex = 3)
    '   profile.visible= false
    '   support.visible= false
    '   privacy_Policy.visible= true
    '   cookie_Policy.visible= false
    '   terms_Conditions.visible= false
    '   about.visible= false
    '   packageGrid.visible= false
    '   packageGridText.visible= false
    ' else if (selectedIndex = 4)
    '   profile.visible= false
    '   support.visible= false
    '   privacy_Policy.visible= false
    '   cookie_Policy.visible= true
    '   terms_Conditions.visible= false
    '   about.visible= false
    '   packageGrid.visible= false
    '   packageGridText.visible= false
    ' else if (selectedIndex = 5)
    '   profile.visible= false
    '   support.visible= false
    '   privacy_Policy.visible= false
    '   cookie_Policy.visible= false
    '   terms_Conditions.visible= true
    '   about.visible= false
    '   packageGrid.visible= false
    '   packageGridText.visible= false
    ' else if (selectedIndex = 6)
    '   profile.visible= false
    '   support.visible= false
    '   privacy_Policy.visible= false
    '   cookie_Policy.visible= false
    '   terms_Conditions.visible= false
    '   about.visible= true
    '   packageGrid.visible= false
    '   packageGridText.visible= false
    ' else if (selectedIndex = 7)


    ' end if

  end function
