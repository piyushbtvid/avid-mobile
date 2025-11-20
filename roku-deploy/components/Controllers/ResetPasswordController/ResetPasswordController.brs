function init()
  m.top.id = "ResetPasswordController"

  m.constants = GetConstants()
  m.scene = getScene()
  m.http = httpClient()
  m.apiProvider = APIProvider(m.constants)

  m.topNav = m.scene.findNode("topNav")
  m.topNav.visible = true
  m.topNav.observeFieldScoped("escape", "onTopNavFocusEscape")
  m.topNav.focusedItem = 5

  m.resetCont = m.top.findNode("resetCont")
  m.titleLabel = m.top.findNode("titleLabel")
  m.submitButton = m.top.findNode("submitButton")
  m.msgLabel = m.top.findNode("msgLabel")
  m.submitButton.observeField("buttonSelected", "onSubmitButtonSelected")
  m.loginButton = m.top.findNode("loginButton")
  m.loginButton.observeField("buttonSelected", "onLoginButtonSelected")
  m.userEmail = m.top.findNode("userEmail")
  m.userEmail.fontSize = 35
  m.keyboardDialog = m.top.findNode("signinKeyboard")
  m.keyboardDialog.observeFieldScoped("buttonSelected", "dismissDialog")
  m.keyboardDialog.observeFieldScoped("text", "handleTextEdit")

  m.submitButton.focusedTextFont.uri = "pkg:/images/font/Montserrat-Regular.ttf"
  m.loginButton.focusedTextFont.uri = "pkg:/images/font/Montserrat-Regular.ttf"

  m.submitButton.textFont.uri = "pkg:/images/font/Montserrat-Regular.ttf"
  m.loginButton.textFont.uri = "pkg:/images/font/Montserrat-Regular.ttf"

  centerX = (1920 - 800) / 2
  centerY = (1080 - 620) / 2
  m.resetCont.translation = [centerX, centerY]

  X = (800 - 441) / 2
  Y = (800 - 375) / 2 - 30
  m.titleLabel.translation = [X, 20]
  m.loginButton.translation = [Y, 450]
  m.selectedObj = "email"

end function


function onNavigateTo(param as object)

  applyFocus(m.userEmail, true, "onNavigateTo() - LandingController.brs")

  m.userEmail.active = true
  m.userEmail.text = m.top.email
  if m.userEmail.text = ""
    m.selectedObj = "email"
    m.userEmail.setFocus(true)
  end if


end function

sub handleTextEdit(msg)
  if m.selectedObj = "email"
    m.userEmail.text = m.keyboardDialog.text
  end if
end sub

function onTopNavFocusEscape(event as object) as void
  escapeStatus = event.getData()
  if not escapeStatus then return
  onKeyEvent(m.constants.REMOTE_KEYS.RIGHT, true)
end function

sub setUpEditEmail()
  m.keyboardDialog.textEditBox.secureMode = false
  m.keyboardDialog.keyboardDomain = "email"
  m.keyboardDialog.title = "Email entry"
  m.keyboardDialog.message = ["EMAIL"]
  m.keyboardDialog.buttons = ["OK"]
  m.keyboardDialog.textEditBox.hintText = "Enter a valid email address..."
  m.keyboardDialog.text = m.userEmail.text
end sub

sub dismissDialog()
  print "called dismissDialog"
  m.keyboardDialog.close = true
  if m.selectedObj = "email"
    m.userEmail.setFocus(true)
  end if
  m.keyboardDialog.visible = false
end sub

function onKeyEvent(key as string, press as boolean) as boolean
  handled = false
  ? "signinpage key= "; key; " press= "; press
  if press then
    if key = m.constants.REMOTE_KEYS.BACK then
      goBackInHistory(m.scene)
      handled = true
    else if key = m.constants.REMOTE_KEYS.DOWN
      if m.userEmail.hasFocus()
        m.userEmail.active = false
        m.submitButton.setFocus(true)
        handled = true
      else if m.submitButton.hasFocus()
        m.loginButton.setFocus(true)
        m.selectedObj = "button"
        handled = true
      else if m.loginButton.hasFocus()
        handled = false
      else
        m.userEmail.setFocus(true)
        m.userEmail.active = true
        handled = true
      end if
    else if key = "up"
      if m.loginButton.hasFocus()
        m.submitButton.setFocus(true)
        handled = true
      else if m.submitButton.hasFocus()
        m.userEmail.setFocus(true)
        m.userEmail.active = true
        m.selectedObj = "email"
        handled = true
      else if m.userEmail.hasFocus()
        m.topNav.setFocus(true)
        m.userEmail.active = false
        handled = true
      end if
    else if key = "OK"
      if m.userEmail.hasFocus()
        setupEditEmail()
      end if
      m.keyboardDialog.visible = true
      m.keyboardDialog.setFocus(true)
    end if
  end if
  return handled
end function

function onSubmitButtonSelected()

  showSpinner(m.top)

  email = m.userEmail.text

  resetPassRequest = TVService(m.apiProvider).forgotPassword(email)
  resetPassRequest.httpResponse.observeField("response", "onResetPasswordResponse")
  m.http.sendRequest(resetPassRequest)

end function

function onResetPasswordResponse(event)

  response = event.getData().data
  if isValid(response)
  hideSpinner()
    status = response.status
    data = response.data
    msg = response.msg

      m.msgLabel.visible = true
      m.msgLabel.text = response.msg
      bound = m.msgLabel.boundingRect()
      a = bound.width
      m.msgLabel.translation = [(1920 - a) / 2, 900]
      m.submitButton.setFocus(true)
  end if


end function

function onLoginButtonSelected()

  pageInfo = createPageInfo(m.constants.CONTROLLERS.LOGIN, {})
  m.scene.loadController = pageInfo

end function

