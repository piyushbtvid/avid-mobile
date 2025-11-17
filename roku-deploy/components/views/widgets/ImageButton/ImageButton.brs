function init()

  m.top.id = "ImageButton"
  m.img = m.top.findNode("img")
  m.btnLabel = m.top.findNode("btnLabel")
  m.lockImg = m.top.findNode("lockImg")
  m.top.minWidth = 100
  m.top.observeField("focusedChild", "onFocusChange")
  setDefaults()

end function

'***** Handlers *****

function onImageSourceChange(event as object)

  imgSrc = event.getData()

  if(isValid(imgSrc.label))
    m.btnLabel.text = imgSrc.label
    m.btnLabel.id = imgSrc.id
    if m.btnLabel.id = "access_premium"
      m.lockImg.uri = "pkg://images/access-lock.png" 'imgSrc.accessLock
    else if m.btnLabel.id = "CreateProfielBtn" or m.btnLabel.id = "manageProfiles" or m.btnLabel.id = "switchProfiles" or m.btnLabel.id="subscribe"
      m.btnLabel.color = "#000000"
    else if m.btnLabel.id = "chooseMonthly"
      m.img.blendColor = "0x000000FF"
    else if m.btnLabel.id = "signIn_qrBtn"
      m.btnLabel.color = "#ffffff"
      m.btnLabel.font.uri = "pkg:/images/font/Montserrat-Bold.ttf"
      m.lockImg.uri = "pkg://images/login_qrBtn.png"
      m.lockImg.width = 30
      m.btnLabel.font.size = 25
    else if imgSrc.id = "nextLoginBtn"
      m.btnLabel.color = "#ffffff"
      m.btnLabel.font.size = 40
    end if
  end if

  if(isValid(imgSrc.width))
    m.img.width = imgSrc.width
    m.img.loadWidth = imgSrc.width
  end if

  if(isValid(imgSrc.height))
    m.img.height = imgSrc.height
    m.img.loadHeight = imgSrc.height
  end if

  if m.btnLabel.text <> ""
    m.btnLabel.width = m.btnLabel.boundingRect().width
    m.btnLabel.height = m.btnLabel.boundingRect().height
    m.btnLabel.translation = [(m.img.width - m.btnLabel.width) / 2, (m.img.height - m.btnLabel.height) / 2]
  end if

  m.img.loadDisplayMode = "limitSize"

  if m.btnLabel.text = "Access Premium"
    m.btnLabel.translation = [(m.img.width - m.btnLabel.width + 20) / 2, (m.img.height - m.btnLabel.height) / 2]
  end if

  onFocusChange()

end function

function onFocusChange()

  imgSrc = m.top.imgSrc
  if (m.top.hasFocus())
    if isValid(imgSrc)
      m.img.uri = ""
      m.img.uri = imgSrc.focusImg

      if m.btnLabel.id = "chooseMonthly"
        m.img.blendColor = "#B87333"
      else
        m.img.blendColor = "#D9D2B8"
      end if
      if isValid(imgSrc.focusedWidth) and isValid(imgSrc.focusedHeight)
        m.img.width = imgSrc.focusedWidth
        m.img.height = imgSrc.focusedHeight
        m.img.translation = [-20, -20]
      else if m.btnLabel.id = "CreateProfielBtn" or m.btnLabel.id = "manageProfiles" or m.btnLabel.id = "switchProfiles" or m.btnLabel.id="subscribe"
        m.btnLabel.color = "#ffffff"
      end if
    end if
  else
    if m.btnLabel.id = "CreateProfielBtn" or m.btnLabel.id = "manageProfiles" or m.btnLabel.id = "switchProfiles" or m.btnLabel.id="subscribe"
      m.btnLabel.color = "#000000"
    end if
    if isValid(imgSrc)
      m.img.uri = ""
      m.img.uri = imgSrc.unFocusImg
      if m.btnLabel.id = "chooseMonthly"
        m.img.blendColor = "0x000000FF"
      else
        m.img.blendColor = -1
      end if
      if isValid(imgSrc.width) and isValid(imgSrc.height)
        m.img.width = imgSrc.width
        m.img.height = imgSrc.height
        m.img.translation = [0, 0]
      end if
    end if
  end if

end function

'***** Helpers *****

'Configures all default values as needed for the image button
function setDefaults()

  m.top.focusedIconUri = "pkg:/images/transparent_image.png"
  m.top.iconUri = "pkg:/images/transparent_image.png"
  m.top.focusFootprintBitmapUri = "pkg:/images/transparent_image.png"
  m.top.focusBitmapUri = "pkg:/images/transparent_image.png"

end function
