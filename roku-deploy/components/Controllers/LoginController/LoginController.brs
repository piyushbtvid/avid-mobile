sub init()
    m.top.id = "LoginController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)
    m.localStorage = LocalStorage()

    initUIElements()

    setupObservers()
end sub

sub initUIElements()
    m.leftmenu = m.scene.findNode("leftMenu")
    m.leftmenu.visible = false

    m.bgautoLogin = m.top.findNode("bgautoLogin")
    m.title = m.top.findNode("title")
    m.title.text = "Login"
    m.errorText = m.top.findNode("errorText")
    m.spinner = m.top.findNode("spinner")
    m.errorText = m.top.findNode("errorText")
    m.errorTextTimer = m.top.findNode("errorTextTimer")

    ' Form fields
    m.emailField = m.top.findNode("emailField")
    m.passwordField = m.top.findNode("passwordField")

    m.SigninEmailBgFocus = m.top.findNode("SigninEmailBgFocus")
    m.SigninPasswordBgFocus = m.top.findNode("SigninPasswordBgFocus")


    ' Buttons
    m.nextButton = m.top.findNode("nextButton")
    m.nextButton.imgSrc = {
        id: "nextLoginBtn",
        label: "       Login       ",
        unFocusImg: "pkg://images/loginBtn_unfocus.png",
        focusImg: "pkg://images/creatorDetailBtnFocus.png",
        width: 357,
        height: 87
    }

    m.signUpButton = m.top.findNode("signUpButton")
    m.signUpButton.imgSrc = {
        id: "SignUp",
        label: "       Sign Up       ",
        unFocusImg: "pkg://images/loginBtn_unfocus.png",
        focusImg: "pkg://images/creatorDetailBtnFocus.png",
        width: 357,
        height: 87
    }

    m.keyboardDialog = m.top.findNode("signinKeyboard")
    m.channelStore = m.top.findNode("store")
end sub

sub setupObservers()
    m.keyboardDialog.observeFieldScoped("buttonSelected", "dismissDialog")
    m.keyboardDialog.observeFieldScoped("text", "handleTextEdit")
    m.nextButton.observeField("buttonSelected", "onNextButtonSelected")
    m.errorTextTimer.ObserveField("fire", "hideError")
    m.signUpButton.observeField("buttonSelected", "onSignUpButtonSelected")
    m.channelStore.observefield("userData", "OnUserDataResponse")
end sub

sub setInitialFocus()
    m.emailField.active = true
    m.emailField.hintTextColor = "#B5883B"
    m.SigninEmailBgFocus.visible = true
    applyFocus(m.emailField, true, "setInitialFocus()")
    m.selectedObj = "email"
end sub

function onNavigateTo(params as object)
    login_Token = m.localStorage.getValueForKey("login_Token")

    if isValid(params) and isValid(params.deeplink) and isValid(params.deeplinkmediatype) and isValid(params.deeplinkcontentid)
        m.deeplink = params.deeplink
        m.deeplinkcontentid = params.deeplinkcontentid
        m.deeplinkmediatype = params.deeplinkmediatype
    end if


    if login_Token <> invalid and Len(login_Token) > 0
        autoLogin()
    else
        manualLogin(params)
    end if
end function

sub autoLogin()
    m.spinner.visible = true
    m.bgautoLogin.visible = true
    loginApi()
end sub

sub manualLogin(params as object)
    m.bgautoLogin.visible = false
    setInitialFocus()

    if params <> invalid
        if params.clearForm = true
            clearFormFields()
        end if

        if params.email <> invalid and params.email <> ""
            m.emailField.text = params.email
            applyFocus(m.passwordField, true, "manualLogin()")
            m.selectedObj = "password"
        end if
    end if

    GetUserData()
end sub

sub clearFormFields()
    m.emailField.text = ""
    m.passwordField.text = ""
end sub

function GetUserData()
    userInfo = CreateObject("roSGNode", "ContentNode")
    userInfo.addFields({ context: "signin" })
    m.channelStore.requestedUserDataInfo = userInfo
    m.channelStore.requesteduserdata = "email, password"
    m.channelStore.command = "getUserData"
end function

function OnUserDataResponse()
    if m.channelStore.userData <> invalid
        m.emailField.text = m.channelStore.userData.email
        m.emailField.cursorPosition = len(m.channelStore.userData.email)
        m.emailField.active = true
        m.emailField.textColor = "#B5883B"
    else
        m.emailField.active = true
    end if
end function

sub setupEditField(fieldType as string)
    if fieldType = "email"
        m.keyboardDialog.textEditBox.secureMode = false
        m.keyboardDialog.keyboardDomain = "email"
        m.keyboardDialog.title = "Enter Your Email"
        m.keyboardDialog.message = ["It is easier to share email Id using RFI"]
        m.keyboardDialog.text = m.emailField.text
    else if fieldType = "password"
        m.keyboardDialog.textEditBox.secureMode = true
        m.keyboardDialog.keyboardDomain = "password"
        m.keyboardDialog.title = "Enter Your Password"
        m.keyboardDialog.message = ["It is easier to share password Id using RFI"]
        m.keyboardDialog.text = m.passwordField.text
    end if

    m.keyboardDialog.buttons = ["OK"]
    m.keyboardDialog.textEditBox.hintText = ""
    m.keyboardDialog.visible = true
end sub

sub handleTextEdit()
    if m.keyboardDialog = invalid then return

    if m.selectedObj = "email"
        m.emailField.text = m.keyboardDialog.text
        m.emailField.cursorPosition = len(m.keyboardDialog.text)
    else if m.selectedObj = "password"
        m.passwordField.text = m.keyboardDialog.text
        m.passwordField.cursorPosition = len(m.keyboardDialog.text)
    end if
end sub

sub dismissDialog()
    if m.keyboardDialog = invalid then return

    m.keyboardDialog.visible = false
    if m.selectedObj = "email"
        applyFocus(m.emailField, true, "dismissDialog()")
    else if m.selectedObj = "password"
        applyFocus(m.passwordField, true, "dismissDialog()")
    end if
end sub

function onNextButtonSelected()
    hideError()
    m.spinner.visible = true

    if not validateForm()
        m.spinner.visible = false
        return false
    end if

    loginApi()
    return true
end function

sub onSignUpButtonSelected()
    pageInfo = createPageInfo(m.constants.CONTROLLERS.SIGNUP, {})
    m.scene.loadController = pageInfo
end sub

function validateForm() as boolean
    if m.emailField.text = ""
        showError("Please enter Email")
        focusField("email")
        return false
    else if not isValidEmail(m.emailField.text)
        showError("Please enter a valid Email")
        focusField("email")
        return false
    else if m.passwordField.text = ""
        showError("Please enter Password")
        focusField("password")
        return false
    else if m.passwordField.text.len() < 6
        showError("Password must be at least 6 characters")
        focusField("password")
        return false
    end if

    return true
end function

sub focusField(fieldType as string)
    if fieldType = "email"
        applyFocus(m.emailField, true, "focusField(email)")
        m.emailField.active = true
        m.emailField.textColor = "#B5883B"
        m.emailField.hintTextColor = "#B5883B"
        m.SigninEmailBgFocus.visible = true
        m.selectedObj = "email"
    else if fieldType = "password"
        applyFocus(m.passwordField, true, "focusField(password)")
        m.passwordField.active = true
        m.passwordField.textColor = "#B5883B"
        m.passwordField.hintTextColor = "#B5883B"
        m.selectedObj = "password"
    end if
end sub

function isValidEmail(email as string) as boolean
    regex = CreateObject("roRegex", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$", "")
    return regex.IsMatch(email)
end function

sub loginApi()
    loginData = {
        email: m.emailField.text, '   "bh@gmail.comii1" "mishra@gmail.com" '
        password: m.passwordField.text '"12345678" '"123456" '
    }

    request = TVService(m.apiProvider).doLogin(loginData)
    request.httpResponse.observeField("response", "handleLogin")
    m.http.sendRequest(request)
end sub

function handleLogin(event)
    resultInfo = event.getData()
    ?"Login API Response: "; resultInfo
    m.spinner.visible = false

    if resultInfo.data <> invalid and resultInfo.data.status = "success"
        handleSuccessfulLogin(resultInfo.data.data)
    else
        handleLoginError(resultInfo)
    end if
end function

sub handleSuccessfulLogin(data as object)
    ?"datadatadatadatadatadatadatadata " data.subscription
    if data <> invalid and data.token <> invalid
        m.localStorage.setValueForKey(data.user.email, "email")
        m.localStorage.setValueForKey(data.user.name, "name")
        m.localStorage.setValueForKey(data.token.toStr(), "login_Token")
        m.localStorage.setValueForKey(data.refresh_token.toStr(), "refresh_token")
        m.localStorage.setValueForKey(data.expire_date.toStr(), "expire_date")

        user_type = data.user.user_type.toStr()
        m.scene.user_type = user_type

        m.scene.subscriptionPlanDetail = data.subscription

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
end sub

sub handleLoginError(resultInfo as object)
    if resultInfo.data <> invalid and resultInfo.data.errors <> invalid
        handleValidationErrors(resultInfo.data.errors)
    else if resultInfo.data <> invalid and resultInfo.data.message <> invalid
        handleValidationErrors(resultInfo.data.message)
    else if resultInfo.msg <> invalid
        if resultInfo.msg = "User not found" or resultInfo.msg = "Not found"
            showError("User Not Found")
        else
            showError("Invalid email or password")
        end if
    else
        showError("Invalid response from server")
    end if
end sub

function handleValidationErrors(errors as object)
    errorMessages = []
    fieldsToClear = []

    if errors.email <> invalid
        for each error in errors.email
            errorMessages.push("Email: " + error)
        end for
        fieldsToClear.push("email")
    end if

    if errors.password <> invalid
        for each error in errors.password
            errorMessages.push("Password: " + error)
        end for
        fieldsToClear.push("password")
    end if

    if errorMessages.count() > 0
        clearFields(fieldsToClear)
        showError(errorMessages.join("\n"))
    else
        showError("Invalid login credentials")
    end if
end function

sub clearFields(fields as object)
    for each field in fields
        if field = "email"
            m.emailField.text = ""
            m.emailField.hintTextColor = "#B5883B"
            m.SigninEmailBgFocus.visible = true
            m.emailField.textColor = "#B5883B"
            applyFocus(m.emailField, true, "clearFields()")
            m.selectedObj = "email"
        else if field = "password"
            m.passwordField.text = ""
            m.passwordField.hintTextColor = "#B5883B"
            m.passwordField.textColor = "#B5883B"
            applyFocus(m.passwordField, true, "clearFields()")
            m.selectedObj = "password"
        end if
    end for
end sub

function showError(text as string)
    m.spinner.visible = false
    m.errorText.text = text
    m.errorText.visible = true
    m.errorTextTimer.control = "start"
end function

function hideError()
    m.errorText.text = ""
    m.errorText.visible = false
    m.errorTextTimer.control = "stop"
end function

function onKeyEvent(key as string, press as boolean) as boolean
    if not press then return false

    if key = "back"
        return handleBackKey()
    else if key = "down"
        return handleDownKey()
    else if key = "up"
        return handleUpKey()
    else if key = "left"
        return true
    else if key = "OK"
        return handleOkKey()
    end if

    return false
end function

function handleBackKey() as boolean
    if m.keyboardDialog <> invalid and m.keyboardDialog.visible = true
        dismissDialog()
        return true
    else
        if m.scene.userConfigData <> invalid
            if m.scene.userConfigData.enable_qrlogin <> invalid and m.scene.userConfigData.enable_qrlogin = "true"
                goBackInHistory(m.scene)
                return true
            end if
        end if
    end if
end function

function handleDownKey() as boolean
    if m.signUpButton.hasFocus()
        m.emailField.textColor = "#B5883B"
        m.emailField.hintTextColor = "#B5883B"
        m.SigninEmailBgFocus.visible = true
        m.emailField.active = true
        m.emailField.cursorPosition = len(m.emailField.text)
        applyFocus(m.emailField, true, "handleDownKey()")
        return true
    else if m.emailField.hasFocus()
        focusField("password")
        m.emailField.hintTextColor = "#DEDEDE"
        m.emailField.textColor = "#DEDEDE"
        m.SigninPasswordBgFocus.visible = true
        m.SigninEmailBgFocus.visible = false
        m.emailField.active = false
        m.passwordField.cursorPosition = len(m.passwordField.text)

        return true
    else if m.passwordField.hasFocus()
        applyFocus(m.nextButton, true, "handleDownKey()")
        m.passwordField.hintTextColor = "#DEDEDE"
        m.passwordField.textColor = "#DEDEDE"
        m.SigninPasswordBgFocus.visible = false
        m.passwordField.active = false
        return true
    end if
    return false
end function

function handleUpKey() as boolean
    if m.nextButton.hasFocus()
        m.SigninPasswordBgFocus.visible = true
        m.passwordField.cursorPosition = len(m.passwordField.text)
        focusField("password")
        return true
    else if m.passwordField.hasFocus()
        m.passwordField.hintTextColor = "#DEDEDE"
        m.passwordField.textColor = "#DEDEDE"
        m.passwordField.active = false
        m.SigninPasswordBgFocus.visible = false
        m.emailField.cursorPosition = len(m.emailField.text)
        focusField("email")
        return true
    else if m.emailField.hasFocus()
        applyFocus(m.signUpButton, true, "handleDownKey()")
        m.emailField.hintTextColor = "#DEDEDE"
        m.emailField.textColor = "#DEDEDE"
        m.SigninEmailBgFocus.visible = false
        m.emailField.active = false
        return true
    end if
    return false
end function

function handleOkKey() as boolean
    if m.emailField.hasFocus()
        setupEditField("email")
    else if m.passwordField.hasFocus()
        setupEditField("password")
    end if

    if m.keyboardDialog.visible = true
        applyFocus(m.keyboardDialog, true, "handleOkKey()")
        return true
    end if
    return false
end function


