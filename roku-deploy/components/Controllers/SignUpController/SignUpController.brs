sub init()
  m.top.id = "LoginController"

  m.constants = GetConstants()
  m.scene = getScene()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)
  m.localStorage = LocalStorage()

  m.leftmenu = m.scene.findNode("leftMenu")
  m.leftmenu.visible = false

  m.bgautoLogin = m.top.findNode("bgautoLogin")
  m.title = m.top.findNode("title")
  m.errorText = m.top.findNode("errorText")
  m.spinner = m.top.findNode("spinner")
  m.errorText = m.top.findNode("errorText")
  m.errorTextTimer = m.top.findNode("errorTextTimer")

  ' Form fields
  m.emailField = m.top.findNode("emailField")
  m.passwordField = m.top.findNode("passwordField")
  m.nameField = m.top.findNode("nameField")

  m.nameBgFocus = m.top.findNode("nameBgFocus")
  m.emailBgFocus = m.top.findNode("emailBgFocus")
  m.passwordFocus = m.top.findNode("passwordFocus")

  ' Buttons
  m.nextButton = m.top.findNode("nextButton")
  m.nextButton.imgSrc = {
    id: "nextLoginBtn",
    label: "       Sign Up       ",
    unFocusImg: "pkg://images/loginBtn_unfocus.png",
    focusImg: "pkg://images/creatorDetailBtnFocus.png",
    width: 357,
    height: 87
  }

  m.keyboardDialog = m.top.findNode("signinKeyboard")
  m.channelStore = m.top.findNode("store")

  setupObservers()
end sub

sub setupObservers()
  m.keyboardDialog.observeFieldScoped("buttonSelected", "dismissDialog")
  m.keyboardDialog.observeFieldScoped("text", "handleTextEdit")
  m.nextButton.observeField("buttonSelected", "onNextButtonSelected")
  m.errorTextTimer.ObserveField("fire", "hideError")
  m.channelStore.observefield("userData", "OnUserDataResponse")

  m.nameField.observeField("focusedChild", "onFieldFocusChange")
  m.emailField.observeField("focusedChild", "onFieldFocusChange")
  m.passwordField.observeField("focusedChild", "onFieldFocusChange")
  m.nextButton.observeField("focusedChild", "onButtonFocusChange")
end sub

sub onFieldFocusChange(event as object)
  field = event.getRoSGNode()
  if field.hasFocus() then
    if field.id = "nameField"
      m.selectedObj = "name"
      field.active = true
      m.nameBgFocus.visible = true
      field.hintTextColor = "#B5883B"
      field.textColor = "#B5883B"
      m.nameBgFocus.visible = true
    else if field.id = "emailField"
      m.selectedObj = "email"
      field.active = true
      field.hintTextColor = "#B5883B"
      field.textColor = "#B5883B"
      m.emailBgFocus.visible = true
    else if field.id = "passwordField"
      m.selectedObj = "password"
      field.active = true
      field.hintTextColor = "#B5883B"
      field.textColor = "#B5883B"
      m.passwordFocus.visible = true
    end if
  else
    field.active = false
    field.hintTextColor = "#DEDEDE"
    field.textColor = "#DEDEDE"
  end if
end sub

sub onButtonFocusChange(event as object)
  button = event.getRoSGNode()
  if not button.hasFocus() and m.selectedObj <> invalid then
    if m.selectedObj = "name"
      setFocusToField(m.nameField, "name")
    else if m.selectedObj = "email"
      setFocusToField(m.emailField, "email")
    else if m.selectedObj = "password"
      setFocusToField(m.passwordField, "password")
    end if
  end if
end sub

function onNavigateTo(params as object)

  if m.nameField.text = ""
    setFocusToField(m.nameField, "name")
  else
    setFocusToField(m.emailField, "email")
  end if

  GetUserData()
end function

function setFocusToField(field as object, fieldType as string)
  m.selectedObj = fieldType
  field.setFocus(true)
end function

sub clearFormFields()
  m.nameField.text = ""
  m.emailField.text = ""
  m.passwordField.text = ""
end sub

function GetUserData()
  userInfo = CreateObject("roSGNode", "ContentNode")
  userInfo.addFields({ context: "signup" })
  m.channelStore.requestedUserDataInfo = userInfo
  m.channelStore.requesteduserdata = "firstname, lastname, email, password"
  m.channelStore.command = "getUserData"
end function

function OnUserDataResponse()
  if m.channelStore.userData <> invalid
    firstName = m.channelStore.userData.firstname
    lastName = m.channelStore.userData.lastname
    m.nameField.text = firstName + " " + lastName
    m.emailField.text = m.channelStore.userData.email
    m.passwordField.text = m.channelStore.userData.password
    ' m.nameField.textColor = "#B5883B"
    m.nameField.active = false
    m.nameBgFocus.visible = false
    m.passwordFocus.visible = true
    setFocusToField(m.passwordField, "password")
  end if
end function

sub setupEditField(fieldType as string)
  if fieldType = "name"
    m.keyboardDialog.textEditBox.secureMode = false
    m.keyboardDialog.keyboardDomain = "name"
    m.keyboardDialog.title = "Enter Your Name"
    m.keyboardDialog.message = ["It is easier to share name Id using RFI"]
    m.keyboardDialog.text = m.nameField.text
  else if fieldType = "email"
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
  m.keyboardDialog.setFocus(true)
end sub

sub handleTextEdit()
  if m.keyboardDialog = invalid then return

  if m.selectedObj = "name"
    m.nameField.text = m.keyboardDialog.text
    m.nameField.cursorPosition = len(m.keyboardDialog.text)
  else if m.selectedObj = "email"
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

  if m.selectedObj = "name"
    m.nameField.cursorPosition = len(m.nameField.text)
    setFocusToField(m.nameField, "name")
  else if m.selectedObj = "email"
    m.emailField.cursorPosition = len(m.emailField.text)
    setFocusToField(m.emailField, "email")
  else if m.selectedObj = "password"
    m.passwordField.cursorPosition = len(m.passwordField.text)
    setFocusToField(m.passwordField, "password")
  end if
end sub

function onNextButtonSelected()
  hideError()
  m.spinner.visible = true

  if not validateForm()
    m.spinner.visible = false
    return false
  end if

  signUpApi()
  return true
end function

function validateForm() as boolean
  if m.nameField.text = ""
    showError("Please enter Name")
    setFocusToField(m.nameField, "name")
    return false
  else if m.emailField.text = ""
    showError("Please enter Email")
    setFocusToField(m.emailField, "email")
    return false
  else if not isValidEmail(m.emailField.text)
    showError("Please enter a valid Email")
    setFocusToField(m.emailField, "email")
    return false
  else if m.passwordField.text = ""
    showError("Please enter Password")
    setFocusToField(m.passwordField, "password")
    return false
  else if m.passwordField.text.len() < 6
    showError("Password must be at least 6 characters")
    setFocusToField(m.passwordField, "password")
    return false
  end if

  return true
end function

function isValidEmail(email as string) as boolean
  regex = CreateObject("roRegex", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$", "")
  return regex.IsMatch(email)
end function

sub signUpApi()
  signUpData = {
    name: m.nameField.text,
    email: m.emailField.text,
    password: m.passwordField.text
  }

  signUpRequest = TVService(m.apiProvider).getRegister(signUpData)
  signUpRequest.httpResponse.observeField("response", "handleRegister")
  m.http.sendRequest(signUpRequest)
end sub

function handleRegister(event)
  resultInfo = event.getData()
  m.spinner.visible = false

  if isValid(resultInfo) and isValid(resultInfo.data) and isValid(resultInfo.data.status) and isValid(resultInfo.data.token) and resultInfo.data.status = "success"
    handleSuccessfulLogin(resultInfo.data.data)
  else if resultInfo.code = 500
    showError("Internal server error")
  else
    showError("This email is already registered")
  end if
end function

sub handleSuccessfulLogin(data as object)
  if data <> invalid and data.token <> invalid
    m.localStorage.setValueForKey(data.user.email, "email")
    m.localStorage.setValueForKey(data.user.name, "name")
    m.localStorage.setValueForKey(data.token.toStr(), "login_Token")
    m.localStorage.setValueForKey(data.refresh_token.toStr(), "refresh_token")
    m.localStorage.setValueForKey(data.expire_date.toStr(), "expire_date")

    m.scene.subscriptionPlanDetail = data.subscription

    user_type= data.user.user_type.toStr()
    m.scene.user_type = user_type
    pageInfo = createPageInfo(m.constants.CONTROLLERS.ALLPROFILESHOW, { deeplink: m.deeplink, deeplinkcontentid: m.deeplinkcontentid, deeplinkmediatype: m.deeplinkmediatype})
    m.scene.loadController = pageInfo
  end if
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
  else if key = "OK"
    return handleOkKey()
  else if key = "left"
    return true 
  end if

  return false
end function

function handleBackKey() as boolean
  if m.keyboardDialog <> invalid and m.keyboardDialog.visible = true
    dismissDialog()
    return true
  else
    goBackInHistory(m.scene)
    return true
  end if
end function

function handleDownKey() as boolean
  if m.nameField.hasFocus()
    m.nameBgFocus.visible = false
    m.emailBgFocus.visible = true
    m.emailField.cursorPosition = len(m.emailField.text)
    setFocusToField(m.emailField, "email")
    return true
  else if m.emailField.hasFocus()
    m.emailBgFocus.visible = false
    m.passwordFocus.visible = true
    m.passwordField.cursorPosition = len(m.passwordField.text)
    setFocusToField(m.passwordField, "password")
    return true
  else if m.passwordField.hasFocus()
    m.passwordFocus.visible = false
    m.nextButton.setFocus(true)
    return true
  else if m.nextButton.hasFocus()
    return true
  end if
  return false
end function

function handleUpKey() as boolean
  if m.nextButton.hasFocus()
    m.passwordFocus.visible = true
    m.passwordField.cursorPosition = len(m.passwordField.text)
    setFocusToField(m.passwordField, "password")
    return true
  else if m.passwordField.hasFocus()
    m.emailBgFocus.visible = true
    m.passwordFocus.visible = false
    m.emailField.cursorPosition = len(m.emailField.text)
    setFocusToField(m.emailField, "email")
    return true
  else if m.emailField.hasFocus()
    m.emailBgFocus.visible = false
    m.nameBgFocus.visible = true
    m.nameField.cursorPosition = len(m.nameField.text)
    setFocusToField(m.nameField, "name")
    return true
  else if m.nameField.hasFocus()
    return true
  end if
  return false
end function

function handleOkKey() as boolean
  if m.nameField.hasFocus()
    setupEditField("name")
    return true
  else if m.emailField.hasFocus()
    setupEditField("email")
    return true
  else if m.passwordField.hasFocus()
    setupEditField("password")
    return true
  else if m.nextButton.hasFocus()
    onNextButtonSelected()
    return true
  end if

  return false
end function