sub manageMyListlistFeature()
    '? "m.watchlistArrId" m.currentVideoId, m.myListArrIdGlobal

    if ValueExistsInArray(m.myListArrIdGlobal, m.currentVideoId)
        m.isMyList = true
        m.addButton.imgSrc = { label: "", unFocusImg: "pkg://images/addedImg.png", focusImg: "pkg://images/addFocusImg.png", width: 65, height: 65 }
    else
        m.addButton.imgSrc = { label: "", unFocusImg: "pkg://images/add.png", focusImg: "pkg://images/focusAddIcon.png", width: 65, height: 65 }
        m.isMyList = false
    end if
end sub

sub addMyListButtonSelectedFun()

    login_Token = LocalStorage().getValueForKey("login_Token")
    if login_Token <> invalid
        if m.isMyList = false


            creatorPageContent = TVService(m.apiProvider).getAddMyListData(login_Token, m.currentVideoId)
            creatorPageContent.httpResponse.observeField("response", "onMyListPageResponse")
            m.http.sendRequest(creatorPageContent)
            m.addButton.imgSrc = { label: "", unFocusImg: "pkg://images/addedImg.png", focusImg: "pkg://images/addFocusImg.png", width: 65, height: 65 }

            m.isMyList = true
        else

            creatorPageContent = TVService(m.apiProvider).getDeleteMyListData(login_Token, m.currentVideoId)
            creatorPageContent.httpResponse.observeField("response", "onMyListPageResponse")
            m.http.sendRequest(creatorPageContent)
            m.addButton.imgSrc = { label: "", unFocusImg: "pkg://images/add.png", focusImg: "pkg://images/focusAddIcon.png", width: 65, height: 65 }

            m.isMyList = false
        end if
    end if
end sub

sub onMyListPageResponse(e)
    resJson = e.getData()
end sub

function loadMyList()
    login_Token = LocalStorage().getValueForKey("login_Token")

    creatorPageContent = TVService(m.apiProvider).getMyListData(login_Token)
    creatorPageContent.httpResponse.observeField("response", "onMyListScreenResponse")
    m.http.sendRequest(creatorPageContent)
end function

function onMyListScreenResponse(event)
    response = event.getData()
    feedList = createObject("RoSGNode", "ContentNode")
    itemArray = []
    idArray = []

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentResponse = response.data.data.data

        for each items in contentResponse
            for each item in items.content
                idArray.push(item.slug)

                contentNode = createObject("RoSGNode", "feedListItemDataModel")
                contentNode.callfunc("parseData", item)
                itemArray.push(contentNode)
            end for
        end for

        ' Update global ID array
        m.myListArrIdGlobal = idArray
    end if

    feedList.appendChildren(itemArray)
    manageMyListlistFeature()
end function


function addThumbUpBtnSelectedFun(event)

    button = event.getRoSGNode()
    btnid = button.id
    if btnid = "thumbUpButton"
        likeOrDislike = "like"
    else
        likeOrDislike = "dislike"
    end if
    m.likeList = true
    login_Token = LocalStorage().getValueForKey("login_Token")
    if login_Token <> invalid
        addLikeContent = TVService(m.apiProvider).doLikeOrDislikeContent(likeOrDislike, m.currentVideoId, login_Token)
        addLikeContent.httpResponse.observeField("response", "onAddToLikeResponse")
        m.http.sendRequest(addLikeContent)
    end if

end function

function addThumbDownBtnSelectedFun(event)

    button = event.getRoSGNode()
    btnid = button.id
    if btnid = "thumbDownButton"
        likeOrDislike = "dislike"
    else
        likeOrDislike = "like"
    end if
    m.disLikeList = true
    login_Token = LocalStorage().getValueForKey("login_Token")

    if login_Token <> invalid
        addLikeContent = TVService(m.apiProvider).doLikeOrDislikeContent(likeOrDislike, m.currentVideoId, login_Token)
        addLikeContent.httpResponse.observeField("response", "onAddToDisLikeResponse")
        m.http.sendRequest(addLikeContent)
    end if

end function


sub onAddToLikeResponse(event)
    response = event.getData()

    loadDisLikedApi()
    data = response.data.data
    if data.message = "Successfully reaction recorded"
        m.thumbUpButton.imgSrc = { label: "", unFocusImg: "pkg://images/focusThumbUpIcon.png", focusImg: "pkg://images/focusThumbUpIcon.png", width: 65, height: 65 }
    else if data.message = "Like removed"
        m.thumbUpButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumb-up.png", focusImg: "pkg://images/focusThumbUpIcon.png", width: 65, height: 65 }
    end if
end sub


function loadLikedApi()
    login_Token = LocalStorage().getValueForKey("login_Token")

    likedPageContent = TVService(m.apiProvider).getLikedListData(login_Token)
    likedPageContent.httpResponse.observeField("response", "onLikedListScreenResponse")
    m.http.sendRequest(likedPageContent)
end function

sub onLikedListScreenResponse(event)
    response = event.getData()
    feedList = createObject("RoSGNode", "ContentNode")
    itemArray = []
    idArray = []

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentResponse = response.data.data.data

        for each items in contentResponse
            for each item in items.content
                idArray.push(item.slug)

                contentNode = createObject("RoSGNode", "feedListItemDataModel")
                contentNode.callfunc("parseData", item)
                itemArray.push(contentNode)
            end for
        end for

        ' Update global ID array
        m.myListArrIdGlobal = idArray
    end if

    feedList.appendChildren(itemArray)
    manageLikedListFeature()
end sub

sub manageLikedListFeature()
    if ValueExistsInArray(m.myListArrIdGlobal, m.currentVideoId)
        m.likeList = true
        m.thumbUpButton.imgSrc = { label: "", unFocusImg: "pkg://images/focusThumbUpIcon.png", focusImg: "pkg://images/focusThumbUpIcon.png", width: 65, height: 65 }
    else
        m.thumbUpButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumb-up.png", focusImg: "pkg://images/focusThumbUpIcon.png", width: 65, height: 65 }
        m.likeList = false
    end if
end sub



sub onAddToDisLikeResponse(event)
    response = event.getData()
    loadLikedApi()
    data = response.data.data
    if data.message = "Successfully reaction recorded"
        m.thumbDownButton.imgSrc = { label: "", unFocusImg: "pkg://images/focus-thumb-down.png", focusImg: "pkg://images/focus-thumb-down.png", width: 65, height: 65 }
    else if data.message = "Dislike removed"
        m.thumbDownButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumbs-down.png", focusImg: "pkg://images/focus-thumb-down.png", width: 65, height: 65 }
    end if
end sub

function loadDisLikedApi()
    login_Token = LocalStorage().getValueForKey("login_Token")

    disLikedPageContent = TVService(m.apiProvider).getDisLikedListData(login_Token)
    disLikedPageContent.httpResponse.observeField("response", "onDisLikedListScreenResponse")
    m.http.sendRequest(disLikedPageContent)
end function

sub onDisLikedListScreenResponse(event)
    response = event.getData()
    feedList = createObject("RoSGNode", "ContentNode")
    itemArray = []
    idArray = []

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentResponse = response.data.data.data

        for each items in contentResponse
            for each item in items.content
                idArray.push(item.slug)

                contentNode = createObject("RoSGNode", "feedListItemDataModel")
                contentNode.callfunc("parseData", item)
                itemArray.push(contentNode)
            end for
        end for

        ' Update global ID array
        m.myListArrIdGlobal = idArray
    end if

    feedList.appendChildren(itemArray)
    manageDisLikedListFeature()
end sub

sub manageDisLikedListFeature()
    if ValueExistsInArray(m.myListArrIdGlobal, m.currentVideoId)
        m.disLikeList = true
        m.thumbDownButton.imgSrc = { label: "", unFocusImg: "pkg://images/focus-thumb-down.png", focusImg: "pkg://images/focus-thumb-down.png", width: 65, height: 65 }
    else
        m.thumbDownButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumbs-down.png", focusImg: "pkg://images/focus-thumb-down.png", width: 65, height: 65 }
        m.disLikeList = false
    end if
end sub