function init()

 m.constants = GetConstants()

 m.spinnerBackGround = m.top.findNode("spinnerBackGround")
 m.loadingSpinner = m.top.findNode("loadingSpinner")

 m.loadingSpinner.poster.observeField("loadStatus", "configureSpinner")

  m.loadingSpinner.poster.uri = "pkg:/images/LoadingIndicater.png"
 	m.loadingSpinner.poster.blendColor = "#B87333"

end function

function onKeyEvent( key as String, press as Boolean )
  handled = false

  if ( press )
    if ( key = m.constants.REMOTE_KEYS.BACK )
      if (isValid(m.top.parentFocusItem))
        applyFocus( m.top.parentFocusItem, true, "onKeyEvent() - LoadingSpinner.brs" )
        m.top.parentFocusItem = invalid
        stopSpinner()
        handled = true
      end if
    end if
  end if

  return handled

end function

'***** Handlers ******

' Starts loadingSpinner
function startSpinner()
  m.top.setFocus(true)

  m.spinnerBackGround.opacity = 0.8
  m.spinnerBackGround.visible = true
  m.loadingSpinner.visible = true
end function

' Stop loadingSpinner
function stopSpinner()
  m.spinnerBackGround.opacity = 0
  m.spinnerBackGround.visible = false
  m.loadingSpinner.visible = false
end function

'****** Helpers ******

' Configures the loadingSpinner object
function configureSpinner()
 if(m.loadingSpinner.poster.loadStatus = "ready")
   centerx = (1920 - m.loadingSpinner.poster.bitmapWidth) / 2
   centery = (1080 - m.loadingSpinner.poster.bitmapWidth) / 2
   m.loadingSpinner.translation = [ centerx, centery ]
 end if
end function
