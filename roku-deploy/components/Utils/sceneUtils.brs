
'-------------------------------------------------------
' Contains General Scene Utility methods'
'-------------------------------------------------------

' Returns the rootScene Object
' @return rootScene
function getScene()

  if (not isValid( m.scene ))
    m.scene = m.top.getScene()
  end if

  return m.scene

end function

' Returns the loadingSpinner Object
' @return loadingSpinner
function getLoadingSpinner()
  scene = getScene()
  loadingSpinner = scene.findNode("loadingSpinner")

  return loadingSpinner
end function

function getSpinner()
  scene = getScene()
  Spinner = scene.findNode("Spinner")

  return Spinner
end function


' Starts the loadingSpinner
' @param Object currentFocusItem
function startSpinner( currentFocusItem as Object )
  loadingSpinner = getLoadingSpinner()
  loadingSpinner.parentFocusItem = currentFocusItem
  loadingSpinner.startSpinner = true
end function

function showSpinner( currentFocusItem as Object )
  Spinner = getSpinner()
  Spinner.parentFocusItem = currentFocusItem
  Spinner.startSpinner = true
end function

' Stops loadingSpinner
function stopSpinner()
  getLoadingSpinner().stopSpinner = true
end function

function hideSpinner()
  getSpinner().stopSpinner = true
end function

' Creates a page navigation object that could be used to navigate into a page
function createPageInfo( page as String, params as Object, skipHistory = false as Boolean )
  return {
    page: page,
    params: params,
    skipHistory: skipHistory
  }
end function

' Loads a page from the given page info details
' @param object pageInfo object
' @param scene object
function loadPage( pageInfo as Object, scene = getScene() as Object )
  scene.loadPage = pageInfo
end function

' goes back one level in history
function goBackInHistory( scene = getScene() as Object )
  scene.unloadController = true
end function

function sethomePageItem( homePageItems as Object, scene = GetScene() as Object )
  scene.homePageItems = homePageItems
end function

' returns the userProfile
function getHomePageItems( scene = GetScene() )
  return scene.homePageItems
end function

function setHomeData( homeData as Object, scene = GetScene() as Object )
  scene.homeData = homeData
end function

function getHomeData( scene = GetScene() )
  return scene.homeData
end function

function setMoviesPageItem( moviesPageItems as Object, scene = GetScene() as Object )
  scene.moviesPageItems = moviesPageItems
end function

function getMoviesPageItems( scene = GetScene() )
  return scene.moviesPageItems
end function

function setSeriesPageItem( seriesPageItems as Object, scene = GetScene() as Object )
  scene.seriesPageItems = seriesPageItems
end function

function getSeriesPageItems( scene = GetScene() )
  return scene.seriesPageItems
end function

function setLivePageItem( livePageItems as Object, scene = GetScene() as Object )
  scene.livePageItems = livePageItems
end function

function getLivePageItems( scene = GetScene() )
  return scene.livePageItems
end function


function setLocalContent( localContent as Object, scene = GetScene() as Object )
  scene.localContent = localContent
end function

' returns the userProfile
function getLocalContent( scene = GetScene() )
  return scene.localContent
end function

function setSliderContent( sliderContent as Object, scene = GetScene() as Object )
  scene.sliderContent  = sliderContent
end function

function getSliderContent( scene = GetScene() )
  return scene.sliderContent
end function


function setLoginInfo(loginInfo,  scene = GetScene() as Object)
  scene.loginInfo  = loginInfo
end function

function getLoginInfo( scene = GetScene())
  return scene.loginInfo
end function

function isUserSubscribed() 
  userInfo =  getLoginInfo()
  if userInfo <> invalid AND (userInfo.isPremiumUser = true)
    return true
  end if 
  return false
end function

function setRokuSubscribed(rokuSubscription,  scene = GetScene() as Object)
  scene.isRokuSubscription  = rokuSubscription
end function

function isRokuSubscribed(scene = getScene() ) 
  return scene.isRokuSubscription
end function

function isLogeedIn() 
  userInfo =  getLoginInfo()
  if userInfo <> invalid AND userInfo.isLogin = true
    return true
  end if 
  return false
end function