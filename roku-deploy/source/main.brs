' Entry point for App'
function Main(args as dynamic)
  constants = GetConstants()

  port = CreateObject("roMessagePort")
  screen = CreateObject("roSGScreen")
  screen.setMessagePort(port)
  roInput = CreateObject("roInput")
  roInput.SetMessagePort(port)
  scene = screen.CreateScene("BaseScene")
  screen.show()
  m.global = screen.getGlobalNode()

  scene.observeField("exitApp", port)

  if (args.contentId <> invalid) and (args.mediaType <> invalid)
    if (args.contentId <> "" and args.contentId.len() > 0) and (args.mediaType <> "" and args.mediaType.len() > 0)
      scene.deeplinkProccessed = true

      if getValueForKey("login_Token") <> invalid and getValueForKey("login_Token") <> ""
        'if args.mediaType <> "season"
        scene.loadController = {
          page: m.constants.CONTROLLERS.DETAIL
          params: {
            deepLink: "true"
            deepLinkContentId: args.contentId
            deepLinkMediaType: args.mediaType
            rowItem_Slug: args.contentId
            content_Type: args.mediaType
          }
        }
        ' else
        '   scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
        'end if
    else
      ?"vvvvvvvvvvvvvvvvvvvvvvvvvvvv"
      scene.loadController = {
        page: m.constants.CONTROLLERS.QRCODE
        params: {
          deepLink: "true"
          deepLinkContentId: args.contentId
          deepLinkMediaType: args.mediaType
        }
      }
    end if
  else
    scene.loadController = { page: m.constants.CONTROLLERS.QRCODE, params: {} }
  end if
end if



mainEventLoop(port, scene)

end function

function mainEventLoop(port, scene as object)

  while (true)

    msg = wait(0, port)
    msgType = type(msg)

    if msgType = "roInputEvent" and msg.isInput()
      if msg.GetInfo().ContentId <> invalid and msg.GetInfo().mediaType <> invalid
        deepLink = "true"
        deepLinkContentId = msg.GetInfo().ContentId
        deepLinkMediaType = msg.GetInfo().mediaType
        scene.loadController = { page: m.constants.CONTROLLERS.QRCODE, params: { deepLink: deepLink, deepLinkContentId: deepLinkContentId, deepLinkMediaType: deepLinkMediaType } }
      end if
    end if


    ' Handle all the needed events here
    if (msgType = "roSGScreenEvent")
      if msg.isScreenClosed() then exit while
    else if (msgType = "roSGNodeEvent")
      field = msg.getField()
      if field = "exitApp" then
        return true
      end if
    end if

  end while

end function


function getValueForKey(key)
  m.localStorage = CreateObject("roRegistrySection", "com.app.ReDiscoverTv")
  if m.localStorage.exists(key)
    return m.localStorage.read(key)
  end if
  return invalid
end function