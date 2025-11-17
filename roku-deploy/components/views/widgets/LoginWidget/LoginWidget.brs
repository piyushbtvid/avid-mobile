function init()

  m.top.id = "LoginWidget"

  m.scene = getScene()
  m.constants = GetConstants()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.top.observeField("focusedChild", "onFocus")

  m.title = m.top.findNode("title")
  m.emailEditBox = m.top.findNode("emailEditBox")
  m.passwordEditBox = m.top.findNode("passwordEditBox")
  m.bottomKeyboard = m.top.findNode("keyboard")
  m.inputTypeTitle = m.top.findNode("inputTypeTitle")

  m.layoutButtonGroup = m.top.findNode("layoutButtonGroup")

  m.nextButton = m.top.findNode("continueButton")
  m.nextButton.observeField("buttonSelected", "onNextButtonHandler")

  m.forgotButton = m.top.findNode("forgotButton")
  m.forgotButton.observeField("buttonSelected", "onForgotButtonSelected")

  m.signupButton = m.top.findNode("signupButton")
  m.signupButton.observeField("buttonSelected", "onSignUpButtonSelected")

  m.bottomKeyboard.observeField("text", "handleKeyboard")
  m.bottomKeyboard.textEditBox.observeField("cursorPosition", "handleCursorPositionChange")
  m.bottomKeyboard.textEditBox.observeField("focusedChild", "handleKeyBoardFocus")

  m.inputTypeTitle.text = "Email"
  m.title.text = "Enter your email address"
  setTitlePosition()

  m.bottomKeyboard.keyGrid.horizWrap = false
  m.bottomKeyboard.textEditBox.visible = false
  m.bottomKeyboard.domain = "email"
  m.bottomKeyboard.textEditBox.voiceEnabled = true

  m.emailEditBox.observeField("text", "onEditTextChanged")
  m.passwordEditBox.observeField("text", "onEditTextChanged")

  m.btnFocusIndex = 0

  m.nextButton.buttonConfig = {
    text: "Next"
    backGroundColor: "#FFFFFF"
    FocusbackGroundColor: "#FF0000"
    textWidth: 400
    height: 84
    width: 732
    fontsize: 24
  }

  m.forgotButton.buttonConfig = {
    text: "Forgot Password"
    backGroundColor: "#FFFFFF"
    FocusbackGroundColor: "#00000000"
    height: 30
    width: 200
    fontsize: 24
    footPrint: false
  }

  m.signupButton.buttonConfig = {
    text: "Sign Up"
    backGroundColor: "#FFFFFF"
    FocusbackGroundColor: "#00000000"
    height: 30
    width: 110
    fontsize: 24
    footPrint: false
  }


  m.nextButton.translation = "[630, 800]"
  setEmailTitle()

end function

function onEditTextChanged(event)
  m.bottomKeyboard.text = event.getData()
end function

function handleKeyBoardFocus()
  if m.bottomKeyboard.isInFocusChain()
    applyFocus(m.bottomKeyboard)
  end if
end function

function onFocus()
  if (m.top.hasFocus())
    applyFocus(m.bottomKeyboard, true, " onFocus - Login.brs", true)
    m.nextButton.setFocus(false)
    loadEmailView(false)
  end if
end function

function onDialogButtonSelected(event as object)
  lengthButtons = m.errorDialog.buttons.count()
  selectdButtonIndex = event.getData()

  if m.errorDialog.buttons[selectdButtonIndex] = m.constants.DIALOGS_BUTTONS.OK_TRY_AGAIN
    m.errorDialog.close = true
  else if m.errorDialog.buttons[selectdButtonIndex] = m.constants.DIALOGS_BUTTONS.GO_BACK
    m.errorDialog.close = true
  else if m.errorDialog.buttons[selectdButtonIndex] = m.constants.DIALOGS_BUTTONS.LOG_OUT
    m.errorDialog.close = true
    m.top.confirmLogOut = true
  end if

end function

function loadEmailView(maintainText as boolean)

  m.inputTypeTitle.text = "Email"
  m.title.text = "Enter your email address"
  setTitlePosition()
  m.passwordEditBox.text = ""
  m.passwordEditBox.visible = false
  m.passwordEditBox.active = false
  m.nextButton.labelText = "Next"

  setEmailTitle()

  m.emailEditBox.visible = true
  m.emailEditBox.active = true
  m.emailEditBox.cursorPosition = len(m.emailEditBox.text)

  if maintainText
    m.emailEditBox.text = m.typeEmailText
  else
    m.emailEditBox.text = ""
    m.typeEmailText = " "
  end if
  m.bottomKeyboard.text = m.emailEditBox.text
  m.bottomKeyboard.textEditBox.cursorPosition = len(m.emailEditBox.text)
  m.btnFocusIndex = 0

  applyFocus(m.bottomKeyboard, true, " onFocus - Login.brs", true)

end function

function handleKeyboard(event as object)

  text = event.getData()

  if m.emailEditBox.visible = true

    movedPositions = len(text) - len(m.emailEditBox.text)
    isCursorAtEnd = len(m.emailEditBox.text) = m.emailEditBox.cursorPosition
    m.emailEditBox.text = text
    m.typeEmailText = m.emailEditBox.text
    m.emailEditBox.active = true
    ' if not isCursorAtEnd
    '   m.emailEditBox.cursorPosition = m.emailEditBox.cursorPosition + movedPositions
    ' else
    '   m.emailEditBox.cursorPosition = len( m.emailEditBox.text)
    ' end if

    m.emailEditBox.cursorPosition = len(m.emailEditBox.text)


  else if m.passwordEditBox.visible = true

    movedPositions = len(text) - len(m.passwordEditBox.text)
    isCursorAtEnd = len(m.passwordEditBox.text) = m.passwordEditBox.cursorPosition
    m.passwordEditBox.text = text
    m.passwordEditBox.active = true
    if not isCursorAtEnd
      m.passwordEditBox.cursorPosition = m.passwordEditBox.cursorPosition + movedPositions
    else
      m.passwordEditBox.cursorPosition = len(m.passwordEditBox.text)
    end if

  end if

end function

function onNextButtonHandler()
  if m.emailEditBox.visible = true and m.emailEditBox.text = ""
    showErrorMessage(m.constants.DIALOGS_TITLES.INVALD_INPUT, m.constants.LOGIN_ERRORS.EMPTY_EMAIL, [m.constants.DIALOGS_BUTTONS.OK_TRY_AGAIN])
  else if m.passwordEditBox.visible = true and m.passwordEditBox.text = ""
    showErrorMessage(m.constants.DIALOGS_TITLES.INVALD_INPUT, m.constants.LOGIN_ERRORS.EMPTY_PASSWORD, [m.constants.DIALOGS_BUTTONS.OK_TRY_AGAIN])
  else if m.emailEditBox.visible = true and not isValidEmail(m.emailEditBox.text)
    showErrorMessage(m.constants.DIALOGS_TITLES.INVALD_INPUT, m.constants.LOGIN_ERRORS.INVALID_EMAIL, [m.constants.DIALOGS_BUTTONS.OK_TRY_AGAIN])
  else if m.emailEditBox.visible and isValidEmail(m.emailEditBox.text)

    if not isValidEmail(m.bottomKeyboard.text) then m.bottomKeyboard.text = m.emailEditBox.text

    loadPasswordView()

  else if Len(m.passwordEditBox.text) > 0 and m.passwordEditBox.visible = true
    userLogin()
  else
    showErrorMessage(m.constants.DIALOGS_TITLES.INVALD_INPUT, m.constants.LOGIN_ERRORS.EMPTY_FIELDS, [m.constants.DIALOGS_BUTTONS.OK_TRY_AGAIN])
  end if
end function

function loadPasswordView()

  m.inputTypeTitle.text = "Password"
  m.title.text = "Enter your password"
  setTitlePosition()
  m.passwordEditBox.visible = true
  m.emailEditBox.visible = false
  m.bottomKeyboard.text = ""
  m.nextButton.labelText = "Next"
  applyFocus(m.bottomKeyboard, true, " onFocus - Login.brs", true)

  setPasswordTitle()
end function

function onNavigateTo(_param as object)

  applyFocus(m.emailLoginWidget, true, "onNavigateTo() - LoginController.brs")

end function


sub handleCursorPositionChange(event)
  currentEdit = getCurrentEditBox()
  currentEdit.cursorPosition = event.getData()
end sub

function getCurrentEditBox()
  if m.emailEditBox.visible = true
    return m.emailEditBox
  else if m.passwordEditBox.visible = true
    return m.passwordEditBox
  end if
end function


function OnKeyEvent(key as string, press as boolean) as boolean

  handled = false
  focusMap = createFocusMap()
  currentFocusComp = getCurrentFocusItem(m.scene, true)

  if (press)
    if key = m.constants.REMOTE_KEYS.BACK
      if m.passwordEditBox.visible = true
        loadEmailView(true)
        return true
      else if m.emailEditBox.visible = true
        m.top.backPressed = true
        return true
      end if
    end if
    if m.emailEditBox.isInFocusChain() and m.bottomKeyboard.text <> m.emailEditBox.text
      m.bottomKeyboard.text = m.emailEditBox.text
      m.emailEditBox.cursorPosition = len(m.emailEditBox.text)
      m.emailEditBox.active = true
      m.bottomKeyboard.textEditBox.cursorPosition = m.emailEditBox.cursorPosition
    else if m.passwordEditBox.isInFocusChain() and m.bottomKeyboard.text <> m.passwordEditBox.text
      m.bottomKeyboard.text = m.passwordEditBox.text
      m.passwordEditBox.cursorPosition = len(m.passwordEditBox.text)
      m.passwordEditBox.active = true
      m.bottomKeyboard.textEditBox.cursorPosition = m.passwordEditBox.cursorPosition
    end if
    handled = componentFocusHandler(key, focusMap, currentFocusComp, true)
    currentFocusComp = getCurrentFocusItem(m.scene, true)
  end if

  return handled

end function

function createFocusMap()

  focusMap = {}

  focusMap[m.bottomKeyboard.id] = { up: invalid, down: m.nextButton.id, left: invalid, right: invalid }
  focusMap[m.nextButton.id] = { up: m.bottomKeyboard.id, down: invalid, left: invalid, right: invalid }

  return focusMap
end function

function showErrorMessage(title as string, message as string, buttons as object)
  m.errorDialog = createObject("roSGNode", "Dialog")
  m.errorDialog.width = 850

  m.errorDialog.messageFont = "pkg://images/font/Montserrat-Bold.ttf"
  ' m.errorDialog.message.font.size = "30"
  m.errorDialog.messageColor = "#FFFFFF"

  m.errorDialog.title = title
  m.errorDialog.message = message
  m.errorDialog.buttons = buttons


  m.errorDialog.titleFont = "pkg://images/font/Montserrat-Bold.ttf"
  ' m.errorDialog.titleFont.size = "36"
  m.errorDialog.iconUri = "-"
  m.errorDialog.titleColor = "#FFFFFF"


  m.errorDialog.buttonGroup.textColor = "#FFFFFF"
  m.errorDialog.buttonGroup.focusedTextColor = "#303672"
  m.errorDialog.buttonGroup.textFont = "pkg://images/font/ViaplaySans-Medium.ttf"
  ' m.errorDialog.buttonGroup.textFont.size = "36"
  m.errorDialog.buttonGroup.focusedTextFont = "pkg://images/font/ViaplaySans-Medium.ttf"
  ' m.errorDialog.buttonGroup.focusedTextFont.size = "36"
  m.errorDialog.buttonGroup.focusBitmapUri = "pkg://images/timePoster.png"
  m.errorDialog.buttonGroup.iconUri = "-"
  m.errorDialog.buttonGroup.focusedIconUri = "-"
  m.errorDialog.buttonGroup.itemSpacings = [20]
  m.errorDialog.backgroundUri = "pkg://images/errorDialog.png"

  m.errorDialog.observeField("buttonSelected", "onDialogButtonSelected")

  m.scene.dialog = m.errorDialog

end function

function setEmailTitle()

  m.nextButton.labelText = "Next"
  m.passwordEditBox.voiceEnabled = false
  m.emailEditBox.voiceEnabled = true

end function

function setPasswordTitle()

  m.nextButton.buttonConfig = {
    text: "Next"
    backGroundColor: "#FFFFFF"
    FocusbackGroundColor: "#7566ff"
    textWidth: 400
    height: 84
    width: 732
    fontsize: 24
  }
  m.emailEditBox.voiceEnabled = false
  m.passwordEditBox.voiceEnabled = true
end function
 
function userLogin()
  loginData = {
    email: "vedTomer@gmail.com",
    password: 12345678,
  }
  ' loginData = {
  '   email: m.emailEditBox.text,
  '   password: m.passwordEditBox.text,
  ' }
  request = TVService(m.apiProvider).doLogin(loginData)
  request.httpResponse.observeField("response", "handleLogin")
  m.http.sendRequest(request)
end function

function handleLogin(event)
  resultInfo = event.getData()
  if(resultInfo.data <> invalid and resultInfo.data.data <> invalid)
    response = resultInfo.data.data
    if (response.token <> invalid and response.token <> "")
      login_Token = response.token
      userInfo = FormatJSON(response.user)
      LocalStorage().setValueForKey(login_Token, "login_Token")
      LocalStorage().setValueForkey(userInfo, "userProfileInfo")

      pageInfo = createPageInfo(m.constants.CONTROLLERS.ACCOUNTS, {})
      m.scene.loadController = pageInfo

    end if
  else
    resultError = resultInfo.error
    resultMsg = resultInfo.msg
    if resultMsg <> invalid
      if resultMsg = "Not authorized"
        showErrorMessage(m.constants.DIALOGS_TITLES.LOGIN_FAILED, m.constants.LOGIN_ERRORS.UNSUCCESFULL_LOGIN, [m.constants.DIALOGS_BUTTONS.OK_TRY_AGAIN])
      end if
    end if
  end if
end function

function setTitlePosition()
  bound = m.title.boundingrect()
  x = ((1920 - bound.width) / 2)
  m.title.translation = [x, 155]
end function