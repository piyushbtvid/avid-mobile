' Code Behind for BaseScene'
function Init()

    m.top.id = "BaseScene"

    m.controllerView = m.top.findNode("controllerView")

    m.constants = GetConstants()
    m.scene = getScene()
    m.apiProvider = APIProvider(m.constants)
    m.localStorage = LocalStorage()
    SetupHttpListener()
    loadConfiqApi()

    m.LiveTopMenu = m.top.findNode("LiveTopMenu")
    m.LiveTopMenu.visible = false

    m.BrowseSelectTopMenu = m.top.findNode("BrowseSelectTopMenu")
    m.BrowseSelectTopMenu.visible = false

    m.leftMenu = m.top.findNode("leftMenu")
    m.leftMenu.isExpanded = false
    m.leftMenu.observeField("isExpanded", "onMenuExpanded")
    m.backShadow = m.top.findNode("backShadow")
    m.backShadow.visible = false

    m.navigationStack = []

    m.InputTask = createObject("roSgNode", "inputTask")
    m.InputTask.observefield("inputData", "handleInputEvent")
    m.InputTask.control = "RUN"

    m.top.backgroundColor = "#000000"
    m.top.backgroundUri = ""

    m.lastKeyPressTime = 0
    m.lastKeyCode = ""
    m.doubleClickThreshold = 1000

    m.isLaunchFirstTime = true

    m.refereceTokenTimer = m.top.findNode("refereceTokenTimer")
    m.refereceTokenTimer.ObserveField("fire", "checkTokenExpiry")


end function

function loadConfiqApi()
    confiqContent = TVService(m.apiProvider).getConfiq()
    confiqContent.httpResponse.observeField("response", "onGetCofiqContentResponse")
    httpClient().sendRequest(confiqContent)
end function

sub onGetCofiqContentResponse(event)
    response = event.getData()

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentData = response.data.data.data

        userConfigData = {
            enable_ads: contentData.enable_ads.toStr(),
            enable_subscription: contentData.enable_subscription.toStr(),
            enable_qrlogin: contentData.enable_qrlogin.toStr(),
            enable_login: contentData.enable_login.toStr(),
            enable_creator: contentData.enable_creator.toStr(),
            enable_movie: contentData.enable_movie.toStr(),
            enable_series: contentData.enable_series.toStr(),
            enable_live: contentData.enable_live.toStr()
        }
        m.top.userConfigData = userConfigData

        enable_qrlogin = invalid
        enable_login = invalid
        enable_creator = invalid
        enable_movie = invalid
        enable_series = invalid
        enable_live = invalid

        if userConfigData <> invalid
            enable_qrlogin = userConfigData.enable_qrlogin
            enable_login = userConfigData.enable_login
            enable_creator = userConfigData.enable_creator
            enable_movie = userConfigData.enable_movie
            enable_series = userConfigData.enable_series
            enable_live = userConfigData.enable_live
        end if

        items = []
        items.push({ "title": "Search", "image": "pkg:/images/menu-search.png" })

        items.push({ "title": "Home", "image": "pkg:/images/menu-home.png" })

        if enable_qrlogin = "true" or enable_login = "true"
            items.push({ "title": "My List", "image": "pkg:/images/menu-mylist.png" })
        end if
        if enable_creator = "true"
            items.push({ "title": "Creators", "image": "pkg:/images/menu-creator.png" })
        end if
        if enable_series = "true"
            items.push({ "title": "Series", "image": "pkg:/images/menu-series.png" })
        end if
        if enable_movie = "true"
            items.push({ "title": "Movies", "image": "pkg:/images/menu-movie.png" })
        end if

        if enable_qrlogin = "true" or enable_login = "true"
            items.push({ "title": "My Account", "image": "" })
            items.push({ "title": "Log Out", "image": "" })
        end if

        m.leftMenu.navItems = items

        m.enable_qrlogin = userConfigData.enable_qrlogin
        m.enable_login = userConfigData.enable_login
    end if

    checkTokenExpiry()
end sub

sub onMenuExpanded()
    m.backShadow.visible = m.leftMenu.isExpanded
end sub

sub checkTokenExpiry()
    expire_date = LocalStorage().getValueForKey("expire_date")
    if expire_date = invalid or expire_date = ""
        LocalStorage().removeKey("login_Token")
        LocalStorage().removeKey("refresh_token")
        LocalStorage().removeKey("expire_date")
        if m.top.deeplinkProccessed <> true
            if m.enable_qrlogin = "true" then
                m.scene.loadController = { page: m.constants.CONTROLLERS.QRCODE, params: {} }
            else if m.enable_login = "true" then
                m.scene.loadController = { page: m.constants.CONTROLLERS.LOGIN, params: {} }
            else
                m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
            end if
        end if
        return
    end if
    login_Token = LocalStorage().getValueForKey("login_Token")
    current_time = getCurrentUnixTimestamp()
    time_until_expiry = expire_date.toInt() - current_time

    if time_until_expiry < (60 * 60)
        loadRefreshToken()
    else
        if m.isLaunchFirstTime = true
            m.isLaunchFirstTime = false
            if m.top.deeplinkProccessed <> true
                if m.enable_qrlogin = "true" or m.enable_login = "true"
                    m.scene.loadController = { page: m.constants.CONTROLLERS.ALLPROFILESHOW, params: { viaLogin: false } }
                    loadSubscriptionDetailApi()
                    m.refereceTokenTimer.control = "start"
                else
                    LocalStorage().removeKey("login_Token")
                    LocalStorage().removeKey("refresh_token")
                    LocalStorage().removeKey("expire_date")
                    m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
                end if
            end if
        end if
    end if
end sub


function loadRefreshToken()
    http = httpClient()
    refresh_token = ""
    refresh_token = LocalStorage().getValueForKey("refresh_token")
    refereceTokenContent = TVService(m.apiProvider).getRefreshToken(refresh_token)
    refereceTokenContent.httpResponse.observeField("response", "handleRefreshTokenResponse")
    http.sendRequest(refereceTokenContent)
end function

function handleRefreshTokenResponse(response as object) as void
    response = response.getData()

    if response <> invalid and response.data <> invalid and response.data.data <> invalid
        json = response.data.data
        m.localStorage.setValueForKey(json.token.toStr(), "login_Token")
        m.localStorage.setValueForKey(json.refresh_token.toStr(), "refresh_token")
        m.localStorage.setValueForKey(json.expire_date.toStr(), "expire_date")
        if m.isLaunchFirstTime = true
            m.isLaunchFirstTime = false

            if m.enable_qrlogin = "true" or m.enable_login = "true"
                m.scene.loadController = { page: m.constants.CONTROLLERS.ALLPROFILESHOW, params: { viaLogin: false } }
                loadSubscriptionDetailApi()
                m.refereceTokenTimer.control = "start"
            else
                LocalStorage().removeKey("login_Token")
                LocalStorage().removeKey("refresh_token")
                LocalStorage().removeKey("expire_date")
                m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
            end if
        end if
    else
        LocalStorage().removeKey("login_Token")
        LocalStorage().removeKey("refresh_token")
        LocalStorage().removeKey("expire_date")

        if m.enable_qrlogin = "true" then
            m.scene.loadController = { page: m.constants.CONTROLLERS.QRCODE, params: {} }
        else if m.enable_login = "true" then
            m.scene.loadController = { page: m.constants.CONTROLLERS.LOGIN, params: {} }
        else
            m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
        end if

    end if

end function


'Creates a HTTP Task
function SetupHttpListener()
    httpTask = CreateObject("roSGNode", "HttpTask")
    httpTask.control = "RUN"
    m.top.httpTask = httpTask
end function

sub handleInputEvent(msg)
    if type(msg) = "roSGNodeEvent" and msg.getField() = "inputData"
        deeplink = msg.getData()
        if deeplink <> invalid
            m.top.inputEventDetected = true
            handleDeepLink(deeplink)
        end if
    end if
end sub

function handleDeepLink(deeplink as object)
    if deeplink.type <> invalid
        m.leftMenu.isExpanded = false
        login_Token = LocalStorage().getValueForKey("login_Token")
        if login_Token <> invalid and login_Token <> ""

            m.scene.loadController = {
                page: m.constants.CONTROLLERS.DETAIL
                params: {
                    deepLink: "true"
                    deepLinkContentId: deeplink.id
                    deepLinkMediaType: deeplink.type
                    rowItem_Slug: deeplink.id
                    content_Type: deeplink.type
                }
            }
        else
            m.scene.loadController = {
                page: m.constants.CONTROLLERS.QRCODE
                params: {
                    deepLink: "true"
                    deepLinkContentId: deeplink.id
                    deepLinkMediaType: deeplink.type
                }
            }
        end if
    end if
end function

function onKeyEvent(key as string, press as boolean) as boolean
    handled = false
    login_Token = LocalStorage().getValueForKey("login_Token")
    if (press)
        ?"Scene Onkey Event"
        if key = m.constants.REMOTE_KEYS.BACK
            m.top.backNavEscape = true
            errDialog = CreateObject("roSGNode", "Dialog")
            errDialog.title = "Quit"
            errDialog.message = "Are you sure you want to exit?"
            errDialog.buttons = ["Cancel", "Exit"]
            errDialog.observeField("buttonSelected", "onButtonSelected")
            m.scene.dialog = errDialog
            handled = true
        else if key = m.constants.REMOTE_KEYS.LEFT
            m.leftMenu.isExpanded = true
            applyFocus(m.leftMenu, true, "onNavigateTo() - HomeController.brs")
            handled = true
        else if key = m.constants.REMOTE_KEYS.RIGHT
            m.leftMenu.isExpanded = false
            m.top.leftNavEscape = true
            handled = true
        else if key = m.constants.REMOTE_KEYS.UP and not m.LiveTopMenu.visible and not m.leftMenu.isExpanded and login_Token <> invalid
            currentTime = createObject("roTimespan").totalMilliseconds()

            if m.lastKeyCode = m.constants.REMOTE_KEYS.UP and (currentTime - m.lastKeyPressTime) < m.doubleClickThreshold
                m.scene.loadController = { page: m.constants.CONTROLLERS.STREAM, params: {} }
                m.lastKeyPressTime = 0
                m.lastKeyCode = ""
                handled = true
            else
                m.lastKeyPressTime = currentTime
                m.lastKeyCode = m.constants.REMOTE_KEYS.UP
                m.top.upNavEscape = true
                handled = true
            end if
        else if key = m.constants.REMOTE_KEYS.DOWN
            m.top.downNavEscape = true
            handled = true
        else
            m.lastKeyCode = ""
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

sub stopRefreshTimer()
    m.refereceTokenTimer.control = "stop"
end sub

sub startRefreshTimer()
    m.refereceTokenTimer.control = "start"
end sub

function loadSubscriptionDetailApi()
    login_Token = LocalStorage().getValueForKey("login_Token")
    subscriptionPageContent = TVService(m.apiProvider).getSubscriptionDetail(login_Token)
    subscriptionPageContent.httpResponse.observeField("response", "onGetContentResponse")
    httpClient().sendRequest(subscriptionPageContent)
end function

sub onGetContentResponse(event)
    response = event.getData()
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        userData = response.data.data.data
        userType = userData.user.user_type

        subscriptionPlanDetail = userData.subscription
        m.scene.subscriptionPlanDetail = subscriptionPlanDetail

        user_type = userType.toStr()
        m.scene.user_type = user_type
    end if
end sub
