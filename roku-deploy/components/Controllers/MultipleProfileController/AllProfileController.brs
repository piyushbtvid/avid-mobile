sub init()
    m.top.id = "AllProfileController"
    m.scene = getScene()
    m.constants = GetConstants()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)
    m.localStorage = LocalStorage()

    m.manageProfiles = m.top.findNode("manageProfiles")

    m.profileGrid = m.top.findNode("profileGrid")
    m.profileGrid.observeField("itemSelected", "onProfileSelected")

    m.title = m.top.findNode("title")

    m.leftMenu = m.scene.findNode("leftMenu")
    m.leftMenu.visible = false

end sub

sub onNavigateTo(params as object)

    if isValid(params)
        m.viaLogin = params.viaLogin
        if isValid(params.deeplink) and isValid(params.deeplinkmediatype) and isValid(params.deeplinkcontentid)
            m.deeplink = params.deeplink
            m.deeplinkcontentid = params.deeplinkcontentid
            m.deeplinkmediatype = params.deeplinkmediatype
        end if
        if isValid(params.editprofile)
            m.editProfileFlag = params.editprofile
            m.title.text = "Edit Profile"
        else
            m.title.text = "Who's watching?"
            m.manageProfiles.imgSrc = { id: "manageProfiles", label: "Profile", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
            m.manageProfiles.observeField("buttonSelected", "manageProfilesFun")
            m.manageProfiles.translation = [(1920 - m.manageProfiles.imgSrc.width) / 2, 700]

        end if
    end if

    pageState = params.controllerState
    if isValid(pageState)
        applyFocus(m.profileGrid, true, "onNavigateTo() - AllProfileController.brs")
    end if
    getAllProfileList()
end sub

sub getAllProfileList()
    startSpinner(m.top)
    login_Token = LocalStorage().getValueForKey("login_Token")
    creatorPageContent = TVService(m.apiProvider).getAllProfile(login_Token)
    creatorPageContent.httpResponse.observeField("response", "onAllProfileResponse")
    m.http.sendRequest(creatorPageContent)
end sub

sub onAllProfileResponse(event)
    stopSpinner()
    response = event.getData()
    login_Token = LocalStorage().getValueForKey("login_Token")
    expire_date = LocalStorage().getValueForKey("expire_date")
    if response.code = 401
        LocalStorage().removeKey("login_Token")
        LocalStorage().removeKey("expire_date")

        userConfigData = m.scene.userConfigData

        enable_qrlogin = invalid
        enable_login = invalid


        if userConfigData <> invalid
            enable_qrlogin = userConfigData.enable_qrlogin
            enable_login = userConfigData.enable_login
        end if

        if enable_qrlogin = "true" then
            m.scene.loadController = { page: m.constants.CONTROLLERS.QRCODE, params: {} }
        else if enable_login = "true" then
            m.scene.loadController = { page: m.constants.CONTROLLERS.LOGIN, params: {} }
        else
            m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
        end if
        return
    else

        if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
            contentResponse = response.data.data.data
            content = CreateObject("roSGNode", "ContentNode")

            for each profile in contentResponse
                node = CreateObject("roSGNode", "ContentNode")
                node.setFields({
                    id: profile.id,
                    title: profile.name,
                    url: profile.avatar_img
                })
                content.appendChild(node)
            end for

            if m.editProfileFlag <> true and contentResponse.count() < 4
                addNode = CreateObject("roSGNode", "ContentNode")
                addNode.addField("isAddProfile", "boolean", false)
                addNode.setFields({
                    title: "Add Profile",
                    url: "pkg:/images/AddProfile.png",
                    isAddProfile: true
                })
                content.appendChild(addNode)
            end if

            if m.viaLogin = false
                m.top.signalBeacon("AppLaunchComplete")
            end if

            m.profileGrid.content = content
            applyFocus(m.profileGrid, true, "onNavigateTo() - AllProfileController.brs")

            rowBounds = m.profileGrid.BoundingRect()
            rowWidth = rowBounds.width

            centerX = (1920 - rowWidth) / 2
            m.profileGrid.translation = [centerX, 400]


            if isValid(m.deeplink) and isValid(m.deeplinkcontentid) and isValid(m.deeplinkmediatype) and m.deeplink = "true"
                firstProfileId = m.profileGrid.content.getChild(0).id
                getSetProfileApi(firstProfileId)
            end if
        end if
    end if

end sub


sub onProfileSelected(event as object)
    selectedIndex = event.getData()
    selectedItem = m.profileGrid.content.getChild(selectedIndex)

    if selectedItem.Lookup("isAddProfile") = true
        pageInfo = createPageInfo(m.constants.CONTROLLERS.CREATEPROFILE, {})
        m.scene.loadController = pageInfo
    else
        if m.editProfileFlag = true
            m.profile_id = selectedItem.id
            getSingleProfileApi(selectedItem.id)
        else
            getSetProfileApi(selectedItem.id)
        end if
    end if
end sub


sub getSetProfileApi(id)
    login_Token = LocalStorage().getValueForKey("login_Token")
    creatorPageContent = TVService(m.apiProvider).getSetProfile(login_Token, id)
    creatorPageContent.httpResponse.observeField("response", "onSetProfileResponse")
    m.http.sendRequest(creatorPageContent)
end sub


sub onSetProfileResponse(event)
    userConfigData = m.scene.userConfigData
    response = event.getData()
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and response.data.data.status = "success"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.HOME, { deeplink: m.deeplink, deeplinkcontentid: m.deeplinkcontentid, deeplinkmediatype: m.deeplinkmediatype })
        m.scene.loadController = pageInfo
    end if
end sub


sub manageProfilesFun()
    pageInfo = createPageInfo(m.constants.CONTROLLERS.ALLPROFILESHOW, { editProfile: true })
    m.scene.loadController = pageInfo
end sub


sub getSingleProfileApi(id)
    login_Token = LocalStorage().getValueForKey("login_Token")
    creatorPageContent = TVService(m.apiProvider).getSingleProfile(login_Token, id)
    creatorPageContent.httpResponse.observeField("response", "onSingleProfileResponse")
    m.http.sendRequest(creatorPageContent)
end sub


sub onSingleProfileResponse(event)
    response = event.getData()

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        data = response.data.data.data
        pageInfo = createPageInfo(m.constants.CONTROLLERS.CREATEPROFILE, { data: data, profile_id: m.profile_id, editProfile: "true" })
        m.scene.loadController = pageInfo
    end if
end sub

function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false

    if (press)
        if key = m.constants.remote_keys.BACK
            if m.editProfileFlag = true
                goBackInHistory(m.scene)
                handled = true
            else

                errDialog = CreateObject("roSGNode", "Dialog")
                errDialog.title = "Quit"
                errDialog.message = "Are you sure you want to exit?"
                errDialog.buttons = ["Cancel", "Exit"]
                errDialog.observeField("buttonSelected", "onButtonSelected")
                m.scene.dialog = errDialog

                handled = true
            end if
        else if key = m.constants.remote_keys.RIGHT

        else if key = m.constants.remote_keys.LEFT
            handled = true
        else if key = m.constants.remote_keys.DOWN
            if m.profileGrid.hasFocus() and m.editProfileFlag <> true
                applyFocus(m.manageProfiles, true, "onNavigateTo() - AllProfileController.brs")
                handled = true
            end if
            handled = true

        else if key = m.constants.remote_keys.UP
            if m.manageProfiles.hasFocus()
                applyFocus(m.profileGrid, true, "onNavigateTo() - AllProfileController.brs")
                handled = true
            end if
            handled = true
        end if

    end if
    return handled
end function

function onButtonSelected(event)
    m.scene.dialog.close = true
    if event.getData() = 1
        m.scene.exitApp = true
    else
    end if

end function