function init()

    m.top.id = "QrCodeScreenController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.qrCodeGroup = m.top.findNode("qrCodeGroup")
    m.visit_your_computer = m.top.findNode("visit_your_computer")
    m.visit_your_computer.drawingStyles = {
        "GothamBlueBold": { "fontUri": "pkg:/images/font/Montserrat-Medium.ttf", "fontSize": 25, "color": "#B87333" }
        "GothamSemiBold": { "fontUri": "pkg:/images/font/Montserrat-Medium.ttf", "fontSize": 25, "color": "#ffffff" }
    }


    m.qr_code_img = m.top.findNode("qr_code_img")
    m.counterText = m.top.findNode("counterText")
    m.counterText.drawingStyles = {
        "GothamBlueBold": { "fontUri": "pkg:/images/font/Montserrat-Bold.ttf", "fontSize": 25, "color": "#B87333" }
        "GothamSemiBold": { "fontUri": "pkg:/images/font/Montserrat-Bold.ttf", "fontSize": 25, "color": "#696969" }
    }

    m.helpVisit = m.top.findNode("helpVisit")
    m.helpVisit.drawingStyles = {
        "GothamBlueBold": { "fontUri": "pkg:/images/font/Montserrat-Bold.ttf", "fontSize": 25, "color": "#B87333" }
        "GothamSemiBold": { "fontUri": "pkg:/images/font/Montserrat-Bold.ttf", "fontSize": 25, "color": "#696969" }
    }
    m.helpVisit.text = "<GothamSemiBold>" + "Need help? Visit" + "  </GothamSemiBold>" + "<GothamBlueBold>" + "help.christianmovies.com " + " </GothamBlueBold>" + "<GothamSemiBold>" + "or call 400-CHRISTIAN" + " </GothamSemiBold>"

    m.qrCodeText = m.top.findNode("qrCodeText")

    m.signInBtn = m.top.findNode("signInBtn")
    m.signInBtn.imgSrc = { id: "signIn_qrBtn", label: "Sign in directly on this device", unFocusImg: "pkg://images/fill_r8.9.png", focusImg: "pkg://images/qrCodeLoginBtn.png", width: 469, height: 76 }
    m.signInBtn.observeField("buttonSelected", "signInBtnSelected")


    m.leftMenu = m.scene.findNode("leftMenu")
    m.leftMenu.visible = false

    m.bgautoLogin = m.top.findNode("bgautoLogin")

    ' Initialize countdown timer
    m.countdownTimer = createObject("roSGNode", "Timer")
    m.countdownTimer.id = "countdownTimer"
    m.countdownTimer.duration = 1
    m.countdownTimer.repeat = true
    m.countdownTimer.observeField("fire", "updateCountdownText")
    m.top.appendChild(m.countdownTimer)

    m.statusTimer = m.top.findNode("statusTimer")
    m.statusTimer.observeFieldScoped("fire", "onStatusTimer")

end function

function onNavigateTo(param as object)
    m.qrCodeGroup.visible = false
    if isValid(param) and isValid(param.deeplink) and isValid(param.deeplinkmediatype) and isValid(param.deeplinkcontentid)
        m.deeplink = param.deeplink
        m.deeplinkcontentid = param.deeplinkcontentid
        m.deeplinkmediatype = param.deeplinkmediatype
    end if

    login_Token = LocalStorage().getValueForKey("login_Token")
    if login_Token <> invalid and Len(login_Token) > 0
        m.statusTimer.control = "stop"
        getProfileApi()
    else
        ' if isValid(m.deeplink) and isValid(param.deeplinkmediatype) and isValid(param.deeplinkcontentid) and m.deeplink = "true"
        '     m.autoSignInTimer = createObject("roSGNode", "Timer")
        '     m.autoSignInTimer.duration = 0.5
        '     m.autoSignInTimer.repeat = false
        '     m.autoSignInTimer.observeField("fire", "onAutoSignInTimer")
        '     m.autoSignInTimer.control = "start"
        ' else
        loadQrCode()
        'end if
    end if
end function

' sub onAutoSignInTimer()
'     m.autoSignInTimer.control = "stop"
'     m.autoSignInTimer.unobserveField("fire")
'     m.autoSignInTimer = invalid
'     signInBtnSelected()
' end sub


sub loadHome()
    pageInfo = createPageInfo(m.constants.CONTROLLERS.ALLPROFILESHOW, {})
    m.scene.loadController = pageInfo
end sub

function getProfileApi()
    login_Token = LocalStorage().getValueForKey("login_Token")
    creatorPageContent = TVService(m.apiProvider).getProfile(login_Token)
    creatorPageContent.httpResponse.observeField("response", "onProfileResponse")
    m.http.sendRequest(creatorPageContent)
end function

sub onProfileResponse(event)
    response = event.getData()
    if response <> invalid and response.data <> invalid
        loadHome()
    end if
end sub

function loadQrCode()
    creatorPageContent = TVService(m.apiProvider).getQrCode()
    creatorPageContent.httpResponse.observeField("response", "onQrCodeResponse")
    m.http.sendRequest(creatorPageContent)
end function

sub onQrCodeResponse(event)
    response = event.getData()


    if isValid(response) and isValid(response.data) and isValid(response.data.data)
        m.qrCodeGroup.visible = true
        userData = response.data.data
        m.qr_code_img.uri = userData.qr_img
        m.visit_your_computer.text = "<GothamSemiBold>" + "Visit" + "  </GothamSemiBold>" + "<GothamBlueBold>" + userData.verification_url + " </GothamBlueBold>" + "<GothamSemiBold>" + " on your phone or computer" + " </GothamSemiBold>"
        if userData.activation_code <> invalid and userData.activation_code <> ""
            codeArray = userData.activation_code.Split("")
            spacedCode = ""
            for each ch in codeArray
                spacedCode = spacedCode + ch + " "
            end for
            spacedCode = spacedCode.Trim()
            m.qrCodeText.text = spacedCode
        end if
        now = CreateObject("roDateTime").AsSeconds()
        if userData.expires_in <> invalid then m.remainingTime = userData.expires_in - now
        updateCountdownText()

        if m.remainingTime <> invalid and m.remainingTime > 0
            m.countdownTimer.control = "start"
        else
            m.counterText.text = "<GothamBlueBold>" + "Code expired" + " </GothamBlueBold>"
            loadQrCode()
        end if
        m.statusTimer.control = "start"
        applyFocus(m.signInBtn, true, "onNavigateTo() - QrCodeScreenController.brs")
        m.top.signalBeacon("AppLaunchComplete")
    end if
end sub

function loadQrCodeStatus()
    creatorPageContent = TVService(m.apiProvider).getQrCodeStatus()
    creatorPageContent.httpResponse.observeField("response", "onQrCodeStatusResponse")
    m.http.sendRequest(creatorPageContent)
end function

sub onQrCodeStatusResponse(event)
    response = event.getData()

    if isValid(response) and isValid(response.data) and isValid(response.data.data)
        userData = response.data.data

        m.scene.subscriptionPlanDetail = userData.subscription

        if isValid(userData.activation_status) and userData.activation_status = "Activated"

            m.statusTimer.control = "stop"
            m.countdownTimer.control = "stop"
            token = userData.token
            LocalStorage().setValueForKey(token, "login_Token")
            LocalStorage().setValueForKey(userData.refresh_token.toStr(), "refresh_token")
            LocalStorage().setValueForKey(userData.expire_date.toStr(), "expire_date")

            user_type = userData.user.user_type.toStr()

            m.scene.user_type = user_type

            m.scene.callfunc("startRefreshTimer")
            if isValid(m.deeplink) and isValid(m.deeplinkmediatype) and isValid(m.deeplinkcontentid)
                m.scene.loadController = {
                    page: m.constants.CONTROLLERS.DETAIL
                    params: {
                        deepLink: "true"
                        deepLinkContentId: m.deeplinkcontentid
                        deepLinkMediaType: m.deeplinkmediatype
                        rowItem_Slug: m.deeplinkcontentid
                        content_Type: Lcase(m.deeplinkmediatype)
                    }
                }
            else
                pageInfo = createPageInfo(m.constants.CONTROLLERS.ALLPROFILESHOW, { deeplink: m.deeplink, deeplinkcontentid: m.deeplinkcontentid, deeplinkmediatype: m.deeplinkmediatype, viaLogin: true })
                m.scene.loadController = pageInfo
            end if
        end if
    end if
end sub

sub signInBtnSelected()
    ' Pass deep link parameters to login screen if they exist
    params = {}
    if isValid(m.deeplink) and m.deeplink = "true"
        params.deeplink = m.deeplink
        if isValid(m.deeplinkcontentid) then params.deeplinkcontentid = m.deeplinkcontentid
        if isValid(m.deeplinkmediatype) then params.deeplinkmediatype = m.deeplinkmediatype
    end if

    userConfigData = m.scene.userConfigData
    enable_login = userConfigData.enable_login
    if enable_login = "true"
        pageInfo = { page: m.constants.CONTROLLERS.LOGIN, params: params }
        m.scene.loadController = pageInfo
        m.countdownTimer.control = "stop"
        m.statusTimer.control = "stop"
    end if

end sub

sub onStatusTimer()
    loadQrCodeStatus()
end sub


sub updateCountdownText()
    if m.remainingTime <> invalid
        if m.remainingTime <= 0
            m.counterText.text = "<GothamBlueBold>" + "Code expired" + " </GothamBlueBold>"
            m.countdownTimer.control = "stop"
            loadQrCode()
            return
        end if

        minutes = Fix(m.remainingTime / 60)
        seconds = m.remainingTime mod 60

        minutesStr = minutes.ToStr()
        if minutes < 10 then minutesStr = "0" + minutesStr

        secondsStr = seconds.ToStr()
        if seconds < 10 then secondsStr = "0" + secondsStr

        m.counterText.text = "<GothamSemiBold>" + "Code expires in " + "  </GothamSemiBold>" + "<GothamBlueBold>" + minutesStr + ":" + secondsStr + " </GothamBlueBold>"

        m.remainingTime = m.remainingTime - 1
    end if
end sub

function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false

    if (press)

        if not handled
            if key = m.constants.REMOTE_KEYS.BACK
                errDialog = CreateObject("roSGNode", "Dialog")
                errDialog.title = "Quit"
                errDialog.message = "Are you sure you want to exit?"
                errDialog.buttons = ["Cancel", "Exit"]
                errDialog.observeField("buttonSelected", "onButtonSelected")
                m.scene.dialog = errDialog
                handled = true

            else if key = m.constants.remote_keys.RIGHT

            else if key = m.constants.remote_keys.LEFT
                handled = true

            else if key = m.constants.remote_keys.UP


                handled = true
            else if key = m.constants.remote_keys.DOWN

                handled = true
            else if key = "OK"

            end if
        end if

    end if
    return handled
end function

function onButtonSelected(event)
    m.scene.dialog.close = true
    if event.getData() = 1
        m.countdownTimer.control = "stop"
        m.statusTimer.control = "stop"
        m.scene.exitApp = true
    else
    end if
end function