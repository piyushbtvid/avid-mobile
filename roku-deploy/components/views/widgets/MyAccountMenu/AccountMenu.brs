sub init()
    m.AccountMenuList = m.top.findNode("AccountMenuList")
    m.scene = getScene()
    m.constants = GetConstants()

    m.AccountMenuList.observeField("itemFocused", "onItemFocused")
    m.AccountMenuList.observeField("itemSelected", "onItemSelected")

    content = CreateObject("roSGNode", "ContentNode")
    items = [
        { "title": "Continue Watching", "image": "pkg:/images/menu-continue.png" },
        { "title": "My List", "image": "pkg:/images/menu-mylist.png" },
        { "title": "Subscription", "image": "pkg:/images/menu-settings.png" },
        { "title": "Settings", "image": "pkg:/images/menu-settings.png" }
    ]

    for i = 0 to items.Count() - 1
        child = content.createChild("MenuViewModel")
        child.title = items[i].title
        child.image = items[i].image
        if i = 0 then child.isSelected = true else child.isSelected = false
    end for

    ' if content.getChildCount() > 0
    '     content.getChild(0).isSelected = true
    ' end if


    m.AccountMenuList.rowHeights = "[100,100,100]"
    m.AccountMenuList.content = content
    m.AccountMenuList.jumpToItem = 0

    m.top.observeFieldScoped("focusedChild", "onFocusChange")

    m.leftmenu = m.scene.findNode("leftMenu")
    m.leftmenu.visible = true
    m.navList = m.leftmenu.findNode("navList")
    m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")

    email = LocalStorage().getValueForKey("email")
    m.emailLabel = m.top.findNode("emailLabel")
    m.emailLabel.text = email

    name = LocalStorage().getValueForKey("name")
    m.profileShortName = m.top.findNode("profileShortName")
    m.nameLabel = m.top.findNode("nameLabel")
    m.nameLabel.text = name
    m.profileShortName.text = getProfileShortName(name)


end sub

function onFocusChange(event)
    response = event.getData()
    if m.top.hasFocus() = true
        m.AccountMenuList.jumpToItem = m.top.indexSelected
        m.AccountMenuList.setFocus(true)
    end if
end function

function getProfileShortName(name as string) as string
    shortName = ""

    if name <> invalid and name <> ""
        ' Trim extra spaces and split by spaces
        parts = name.trim().split(" ")

        if parts.count() > 1
            ' Use first letter of first and last name
            shortName = Left(parts[0], 1) + Left(parts[parts.count() - 1], 1)
        else
            ' Only one name – use first letter
            shortName = Left(parts[0], 1)
        end if

        shortName = UCase(shortName)
    end if

    return shortName
end function

sub homeMenuFirstLaunchSelected(index)
    m.top.itemSelected = index

    ?"indexindexindexindex"index

    for i = 0 to m.AccountMenuList.content.getChildCount() - 1
        item = m.AccountMenuList.content.getChild(i)
        item.isSelected = (i = index)
    end for
end sub

' sub onItemFocused()
'     ?"onItemFocused......."
'     focusedIndex = m.AccountMenuList.itemFocused
' end sub


sub onItemSelected(event)
    m.top.itemSelected = event.getData()

    for i = 0 to m.AccountMenuList.content.getChildCount() - 1
        item = m.AccountMenuList.content.getChild(i)
        item.isSelected = false
    end for

    m.AccountMenuList.content.getChild(event.getData()).isSelected = true
    index = event.getData()
    m.top.indexSelected = index
    homeMenuFirstLaunchSelected(m.top.indexSelected)
end sub

function onLeftNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.RIGHT, true)
    end if
end function

function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false

    if (press)

        if (not handled)
            if (key = m.constants.REMOTE_KEYS.BACK)
                if m.leftMenu.isExpanded = false
                    m.leftMenu.isExpanded = true
                    applyFocus(m.leftMenu, true, "onNavigateTo() - AccountMenu.brs")
                    handled = true
                end if
                handled = true
            else if key = m.constants.REMOTE_KEYS.LEFT
                if m.AccountMenuList.hasFocus()
                    applyFocus(m.leftmenu, true, "onNavigateTo() - AccountMenu.brs")
                    handled = true
                end if
                handled = true
            else if key = m.constants.REMOTE_KEYS.RIGHT
                m.top.leftNavEscape = true
                if m.navList.hasFocus()
                    applyFocus(m.AccountMenuList, true, "onNavigateTo() - AccountMenu.brs")
                    handled = true
                end if
            else
                handled = true
            end if
        end if
        handled = true
    else
        handled = true
    end if

    return handled

end function

sub isMyAccountMenuItemSelected(event)
    if m.top.itemSelected <> invalid and m.top.itemSelected >= 0
        selectedIndex = m.top.itemSelected
        contentGroup = m.AccountMenuList.content

        for i = 0 to contentGroup.getChildCount() - 1
            item = contentGroup.getChild(i)
            item.isSelected = (i = selectedIndex)
        end for
    end if
end sub