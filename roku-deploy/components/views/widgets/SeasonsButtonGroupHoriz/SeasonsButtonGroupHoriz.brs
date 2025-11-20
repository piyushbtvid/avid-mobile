sub init()
    m.top.layoutDirection = "horiz"
    m.constants = GetConstants()
    m.scrollTimer = m.top.findNode("scrollTimer")
    m.scrollTimer.ObserveField("fire","scrollTo")
end sub

function onKeyEvent(key as string, press as boolean) as boolean

   key = LCase(key)
   if ( NOT press )
     m.scrollTimer.control ="stop"
     return press
   else

    m.top.direction = key
    m.scrollTimer.control ="start"

    if key = m.constants.REMOTE_KEYS.RIGHT
        moveRight()
        return true
    else if key = m.constants.REMOTE_KEYS.LEFT
        moveLeft()
        return true
    else if key = m.constants.REMOTE_KEYS.DOWN
        m.top.escapeDown = true
       return true
    else if  key = m.constants.REMOTE_KEYS.UP
        m.top.escapeUp = true
        return true
    end if

   end if

   return false
end function

function moveLeft()
  i = m.top.buttonFocused
  target = i - 1
  if target < 0 then return false
  m.top.focusButton = target
end function

function moveRight()
  i = m.top.buttonFocused
  target = i + 1
  if target >= m.top.getChildCount() return false
  m.top.focusButton = target
end function

'Smooth Scroll for button group'
function scrollTo()

  if m.top.direction = m.constants.REMOTE_KEYS.LEFT
    moveLeft()
  else if m.top.direction = m.constants.REMOTE_KEYS.RIGHT
    moveRight()
  else
    m.scrollTimer.control ="stop"
  end if

end function
