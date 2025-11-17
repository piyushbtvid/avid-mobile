function init()

    m.top.id = "DetailController"

    m.scene = getScene()
    m.constants = GetConstants()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.keepFirstEpisodeData = {}

    m.backGroundImage = m.top.findNode("backGroundImage")
    m.title = m.top.findNode("title")
    m.contentLayout = m.top.findNode("contentLayout")

    m.moviesDetail = m.top.findNode("moviesDetail")

    m.multiStylelbl = m.top.findNode("multiStylelbl")
    m.multiStylelbl.drawingStyles = {
        "GothamBlueBold": { "fontUri": "pkg:/images/font/Montserrat-Bold.ttf", "fontSize": 20, "color": "#ffffff" }
    }

    m.lblDescription = m.top.findNode("lblDescription")

    m.btnLayOut = m.top.findNode("btnLayOut")
    m.playButton = m.top.findNode("playButton")
    m.playButton.observeField("buttonSelected", "onPlayButtonSelected")
    m.playButton.imgSrc = { label: "Watch Now", unFocusImg: "pkg://images/creatorButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }

    m.addButton = m.top.findNode("addButton")
    m.addButton.observeField("buttonSelected", "addMyListButtonSelectedFun")
    m.thumbUpButton = m.top.findNode("thumbUpButton")
    m.thumbUpButton.observeField("buttonSelected", "addThumbUpBtnSelectedFun")
    m.thumbDownButton = m.top.findNode("thumbDownButton")
    m.thumbDownButton.observeField("buttonSelected", "addThumbDownBtnSelectedFun")

    m.micButton = m.top.findNode("micButton")
    m.searchButton = m.top.findNode("searchButton")
    m.searchButton.observeField("buttonSelected", "onSearchButtonSelected")

    m.micButton.imgSrc = { label: "", unFocusImg: "pkg://images/macIcon.png", focusImg: "pkg://images/focusMacIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }
    m.searchButton.imgSrc = { label: "", unFocusImg: "pkg://images/searchIcon.png", focusImg: "pkg://images/FocusSearchIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }

    m.addButton.imgSrc = { label: "", unFocusImg: "pkg://images/add.png", focusImg: "pkg://images/focusAddIcon.png", width: 65, height: 65 }
    m.thumbUpButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumb-up.png", focusImg: "pkg://images/focusThumbUpIcon.png", width: 65, height: 65 }
    m.thumbDownButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumbs-down.png", focusImg: "pkg://images/focus-thumb-down.png", width: 65, height: 65 }

    m.relatedFocusDetail = m.top.findNode("relatedFocusDetail")
    m.relatedTitle = m.top.findNode("relatedTitle")
    m.relatedDescription = m.top.findNode("relatedDescription")
    m.multiStyleRelatedlbl = m.top.findNode("multiStyleRelatedlbl")
    m.multiStyleRelatedlbl.drawingStyles = {
        "GothamBlueBold": { "fontUri": "pkg:/images/font/Montserrat-Bold.ttf", "fontSize": 20, "color": "#FFFFFF" }
    }

    m.seasonTitle = m.top.findNode("seasonTitle")
    m.seasonRowList = m.top.findNode("seasonRowList")

    m.TopMenu = m.scene.findNode("LiveTopMenu")
    m.TopMenu.visible = false

    m.detailsRowList = m.top.findNode("detailsRowList")
    m.detailsRowList.ObserveField("rowItemSelected", "onDetailRowItemSelected")
    m.detailsRowList.ObserveField("rowItemFocused", "onDetailRowItemFocused")
    m.detailsRowList.rowLabelFont.uri = "pkg:/images/font/Montserrat-Medium.ttf"
    m.detailsRowList.rowLabelFont.size = 30

    m.leftmenu = m.scene.findNode("leftMenu")
    m.leftmenu.visible = false
    m.likeList = false
    m.isMoreLikeThis = false
end function


function onNavigateTo(params as object)

    pageState = params.controllerState
    m.selectedItemId = invalid
    m.rowListIndex = invalid
    m.seasonRowItemIndex = invalid
    m.seasonEpisodeRowItemIndex = invalid
    m.is_Season_Content = false
    m.is_Related_content = false

    if isvalid(params)
        m.playButton.visible = true
        m.shouldAutoPlay = false

        if isValid(params.deeplink) and isValid(params.deeplinkcontentid) and isValid(params.deeplinkmediatype)
            m.deeplink = params.deeplink
            m.deeplinkcontentid = params.deeplinkcontentid
            m.deeplinkmediatype = params.deeplinkmediatype
            m.shouldAutoPlay = true
        end if

        if m.shouldAutoPlay and not isValid(pageState)
            m.shouldAutoPlay = true
        else
            m.shouldAutoPlay = false
        end if

        if isValid(params.content_Type) and params.content_Type.len() > 0 then m.contentType = params.content_Type
        if isValid(params.rowItem_Slug) then loadContent(params.rowItem_Slug)
        m.isMoreLikeThis = false

        applyFocus(m.playButton, true, "onNavigateTo() - DetailController.brs")
    end if

    if isValid(pageState) and isValid(pageState.rowListIndex) then m.rowListIndex = pageState.rowListIndex
    if isValid(pageState) and isValid(pageState.seasonRowItemIndex) then m.seasonEpisodeRowItemIndex = pageState.seasonRowItemIndex

end function

function onNavigateAway()
    m.top.controllerState = { rowListIndex: m.selectedItemId, seasonRowItemIndex: m.seasonRowItemIndex }
end function

function loadContent(video_id)
    startSpinner(m.top)
    login_Token = LocalStorage().getValueForKey("login_Token")
    detailsPageContent = TVService(m.apiProvider).getDetails(video_id, login_Token)
    detailsPageContent.httpResponse.observeField("response", "onGetContentResponse")
    m.http.sendRequest(detailsPageContent)
end function


function onGetContentResponse(event)

    stopSpinner()
    loadLikedApi()
    loadDisLikedApi()
    response = event.getData()
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        videoDetails = response.data.data.data

        if isValid(videoDetails)

            m.currentVideoId = videoDetails.slug
            if videoDetails.landscape <> invalid and videoDetails.landscape.len() > 0
                m.backGroundImage.uri = videoDetails.landscape
            end if

            if isValid(videoDetails.name)
                m.title.text = videoDetails.name
            end if

            if isValid(videoDetails.description)
                m.lblDescription.text = videoDetails.description
            end if

            if isValid(videoDetails.trailer) and isValid(videoDetails.trailer.play_link) then addTrailerButton(videoDetails.trailer.play_link)

            if isValid(videoDetails.progress_seconds) and videoDetails.progress_seconds > 0 then addResumeButton(invalid, invalid)

            m.videoContent = videoDetails


            genre = ""

            genres = isValidArray(videoDetails.genres)
            access = isValidString(videoDetails.access)

            if genres.count() > 0 then genre = isValidString(genres[0].name)

            uploadedYear = isValidString(videoDetails.uploadedYear)

            rating = isValidString(videoDetails.rating)

            metaData = []

            if uploadedYear <> "" then
                metaData.push("<GothamBlueBold>" + uploadedYear + "</GothamBlueBold>")
            end if

            if genre <> "" then
                metaData.push("<GothamBlueBold>" + genre + "</GothamBlueBold>")
            end if

            if rating <> "" then
                metaData.push("<GothamBlueBold>" + rating + "</GothamBlueBold>")
            end if

            if access <> "" then
                metaData.push("<GothamBlueBold>" + access + "</GothamBlueBold>")
            end if

            ' Join the non-empty metaData with "  |  "
            if metaData.count() > 0 then
                text = metaData.join("<GothamBlueBold>  |  </GothamBlueBold>")
            else
                text = ""
            end if

            if text <> "" then m.multiStylelbl.text = text


            m.related_content = isValidArray(videoDetails.related_content)
            if m.related_content.count() > 0 then m.is_Related_Content = true

            if isValid(videoDetails.resumeInfo) and isValid(videoDetails.resumeInfo.progress_seconds) and videoDetails.resumeInfo.progress_seconds > 0 then addResumeButton(videoDetails.resumeInfo.season_number, videoDetails.resumeInfo.episode_number)

            m.seasons = isValidArray(videoDetails.seasons)


            if m.seasons.count() > 0 then m.is_Season_Content = true

            if isValid(videoDetails.content_type) and Lcase(videoDetails.content_type) <> "series"
                if m.is_Related_Content then relatedDataUIFunc(m.related_content)
            else
                if m.is_Related_Content and m.is_Season_Content
                    seasonList(m.seasons, m.related_content)
                else if m.is_Related_Content and not m.is_Season_Content
                    seasonList(invalid, m.related_content)
                else if m.is_Season_Content and not m.is_Related_Content
                    seasonList(m.seasons, invalid)
                end if
            end if
        end if
    end if

    if m.shouldAutoPlay = true
        if m.videoContent <> invalid
            if (Lcase(m.videoContent.content_type) = Lcase(m.deeplinkmediatype) and m.videoContent.slug = m.deeplinkcontentid)
                if m.deeplinkmediatype = "series"
                    m.selectedItemId = invalid
                    initiateVideoPlayBack(false)
                else
                    initiateVideoPlayBack(true)
                end if
            end if
        else
            goBackInHistory()
        end if
        m.shouldAutoPlay = false
    end if

    m.contentLayout.itemSpacings = [20, 20, 70, 20]
    loadMyList()

end function


sub addResumeButton(season_number = invalid, episode_number = invalid)
    if m.btnLayOut.getChild(2) = invalid
        resumeBtn_Text = "Resume"

        if isvalid(season_number) and isValid(episode_number) then resumeBtn_Text = "Resume S" + season_number.toStr() + " E" + episode_number.toStr()

        resumeButton = CreateObject("roSGNode", "ImageButton")
        resumeButton.id = "resumeButton"
        resumeButton.imgSrc = {
            label: resumeBtn_Text,
            unFocusImg: "pkg:/images/creatorButton.png",
            focusImg: "pkg:/images/creatorDetailBtnFocus.png",
            width: 269,
            height: 76
        }

        resumeButton.observeField("buttonSelected", "onResumeButtonSelected")
        m.btnLayOut.appendChild(resumeButton)
    end if
end sub

sub addTrailerButton(play_url)
    m.play_url = play_url

    trailerBtn_Text = "Play Trailer"

    trailerButton = CreateObject("roSGNode", "ImageButton")
    trailerButton.id = "trailerButton"

    trailerButton.imgSrc = {
        label: trailerBtn_Text,
        unFocusImg: "pkg:/images/creatorButton.png",
        focusImg: "pkg:/images/creatorDetailBtnFocus.png",
        width: 269,
        height: 76
    }

    trailerButton.observeField("buttonSelected", "onTrailerButtonSelected")
    m.btnLayOut.appendChild(trailerButton)
end sub

sub onTrailerButtonSelected(event as object)
    if isValid(m.play_url)
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { content: { play_link: m.play_url }, isTrailer: true })
        m.scene.loadController = pageInfo
    end if
end sub


sub relatedDataUIFunc(related_content)
    rowItemsContent = CreateObject("roSGNode", "ContentNode")
    newRow = CreateObject("roSGNode", "ContentNode")
    newRow.title = "Related Video"
    for each item in related_content
        itemNode = CreateObject("roSGNode", "feedListItemDataModel")
        itemNode.callFunc("parseData", item)
        newRow.appendChild(itemNode)
    end for

    rowItemsContent.appendChild(newRow)
    m.detailsRowList.content = rowItemsContent
    m.detailsRowList.visible = true

    if isValid(m.rowListIndex)
        m.moviesDetail.visible = false
        m.detailsRowList.jumpToRowItem = m.rowListIndex
        m.seasonTitle.translation = [70, 70]
        m.seasonRowList.translation = [255, 70]
        m.detailsRowList.translation = [70, 100]
        applyFocus(m.detailsRowList, true, "onNavigateTo() - DetailController.brs")
    end if
end sub


sub seasonList(seasons, related_content)

    rowItemsContent = CreateObject("roSGNode", "ContentNode")
    feedListRow = rowItemsContent.createChild("ContentNode")

    firstSeasonEpisodeInfo = invalid

    if isValid(seasons)
        for each season in seasons
            if isValid(season) and isValid(season.trailer) and isValid(season.trailer.play_link) then addTrailerButton(season.trailer.play_link)
            if isvalid(season.episodes) and season.episodes.count() > 0
                if firstSeasonEpisodeInfo = invalid then firstSeasonEpisodeInfo = season
                seasonNode = bind_Season_RelatedData(season)
                feedListRow.appendChild(seasonNode)
            end if
        end for
    end if

    if isValid(related_content)
        if firstSeasonEpisodeInfo = invalid then firstSeasonEpisodeInfo = related_content
        moreLikeThis_Tab = bind_Season_RelatedData({
            season_number: "More like this",
            episodes: related_content
        })

        feedListRow.appendChild(moreLikeThis_Tab)
    end if


    if firstSeasonEpisodeInfo <> invalid and type(firstSeasonEpisodeInfo) <> "roArray" and firstSeasonEpisodeInfo.episodes <> invalid and type(firstSeasonEpisodeInfo.episodes) = "roArray" and firstSeasonEpisodeInfo.episodes.count() > 0

        m.seasonRowList.content = rowItemsContent
        m.seasonRowList.visible = true
        m.seasonTitle.visible = true

        m.seasonRowList.observeField("rowItemSelected", "onSeasonRowItemSelected")
        m.seasonRowList.observeField("rowItemFocused", "onSeasonRowItemFocused")


        if m.seasonEpisodeRowItemIndex <> invalid and m.seasonRowList.content <> invalid
            focusedContent = m.seasonRowList.content.getChild(0).getChild(m.seasonEpisodeRowItemIndex)

            if focusedContent <> invalid and focusedContent.episodes <> invalid and type(focusedContent.episodes) = "roArray" and focusedContent.episodes.count() > 0 and focusedContent.seasonid <> invalid
                showEpisodes(focusedContent.episodes, focusedContent.seasonid)
            else
                showEpisodes(firstSeasonEpisodeInfo.episodes, firstSeasonEpisodeInfo.season_number.tostr())
            end if
        else
            showEpisodes(firstSeasonEpisodeInfo.episodes, firstSeasonEpisodeInfo.season_number.tostr())
        end if

    else
        goBackInHistory()
        ? "Error: firstSeasonEpisodeInfo or its episodes are invalid or empty"
    end if


end sub


function bind_Season_RelatedData(season)

    seasonNode = CreateObject("roSGNode", "feedListItemDataModel")
    seasonNode.addField("FHDItemWidth", "float", false)

    season_Number = season.season_number

    if not isString(season_number) then season_Number = season_number.toStr()

    seasonNode.seasonid = season_Number
    if season_Number.len() <= 2
        baseWidth = 50
    else
        baseWidth = 300
    end if

    seasonNode.FHDItemWidth = baseWidth
    seasonNode.addFields({ episodes: season.episodes })
    return seasonNode
end function


sub showEpisodes(episodeInfo, seasonNumber)

    rowItemsContent = CreateObject("roSGNode", "ContentNode")
    row = rowItemsContent.createChild("ContentNode")

    if Lcase(seasonNumber) = "more like this" then m.isMoreLikeThis = true

    episodeInfo.sortBy("episode_number")

    feedListData = []

    for each episode in episodeInfo
        episodeNode = row.createChild("feedListItemDataModel")
        episodeNode.callFunc("parseData", episode)
        feedListData.push(episodeNode)
    end for

    row.appendChildren(feedListData)
    m.detailsRowList.content = rowItemsContent
    m.detailsRowList.visible = true

    if isValid(m.rowListIndex)
        m.moviesDetail.visible = false
        m.relatedFocusDetail.visible = true
        m.detailsRowList.jumpToRowItem = m.rowListIndex
        m.seasonTitle.translation = [65, 70]
        m.seasonRowList.translation = [255, 70]
        m.detailsRowList.translation = [70, 100]
        applyFocus(m.detailsRowList, true, "onNavigateTo() - DetailController.brs")
        m.rowListIndex = invalid
    end if
end sub


sub onSeasonRowItemSelected(event)
    selectedIndex = event.getData()
    content = m.seasonRowList.content.getChild(0)
    selectedSeason = content.getChild(selectedIndex[1])

    if selectedSeason <> invalid and selectedSeason.episodes <> invalid
        showEpisodes(selectedSeason.episodes, selectedSeason.seasonid)
    end if
end sub


sub onSeasonRowItemFocused(event)
    focusedData = event.getData()
    rowIndex = focusedData[0]
    itemIndex = focusedData[1]

    m.seasonRowItemIndex = itemIndex
    focusedContent = m.seasonRowList.content.getChild(rowIndex).getChild(itemIndex)

    if focusedContent <> invalid and focusedContent.episodes <> invalid
        showEpisodes(focusedContent.episodes, focusedContent.seasonid)
    end if
end sub

sub onDetailRowItemSelected(event)
    data = event.getData()

    rowIndex = data[0]
    rowItemIndex = data[1]

    selectedRow = event.getRoSGNode().content.getChild(rowIndex)
    rowItem = selectedRow.getChild(rowItemIndex)
    m.selectedItemId = data

    if m.seasonRowList.visible and not m.isMoreLikeThis
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: rowItem.series_slug, isResume: true, content_Id: rowItem.id, content_Type: m.contentType, content: invalid, currentIndex: rowItemIndex, continueWatch: false })
    else
        pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: rowItem.slug })
    end if
    m.scene.loadController = pageInfo
end sub

sub onDetailRowItemFocused(event)
    focusedData = event.getData()
    rowIndex = focusedData[0]
    itemIndex = focusedData[1]
    focusedContent = m.detailsRowList.content.getChild(rowIndex).getChild(itemIndex)

    if focusedContent <> invalid
        m.relatedFocusDetail.visible = true
        m.relatedTitle.text = focusedContent.title
        m.relatedDescription.text = focusedContent.description
        genre = invalid
        if isValid(focusedContent.genre) and focusedContent.genre.count() > 0
            genre = focusedContent.genre[0].name
        end if

        if isValid(genre) then m.multiStyleRelatedlbl.text = "<GothamBlueBold>" + focusedContent.uploadedYear + "  |  </GothamBlueBold>" + "<GothamBlueBold>" + genre + "  |  </GothamBlueBold>" + "<GothamBlueBold>" + focusedContent.rating + " </GothamBlueBold>" '+ "<GothamBlueBold>" + m.itemData.content_type + "  </GothamBlueBold>"
    else
        m.relatedFocusDetail.visible = false
    end if
end sub

function ConvertTimestampToDate(timestamp as integer) as string
    dt = CreateObject("roDateTime")

    dt.FromSeconds(timestamp)

    formattedDate = dt.GetYear().tostr()

    return formattedDate
end function


function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false
    focusMap = createFocusMap()
    currentFocusComp = getCurrentFocusItem(m.scene)

    if (press)

        if (not handled)
            if (key = m.constants.REMOTE_KEYS.BACK)
                if m.deeplink = "true"
                    m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
                else
                    goBackInHistory(m.scene)
                end if
            else if key = m.constants.REMOTE_KEYS.UP

                if m.detailsRowList.hasFocus()
                    if not m.seasonRowList.visible
                        m.relatedFocusDetail.visible = false
                        m.detailsRowList.translation = [65, 570]
                        m.moviesDetail.visible = true
                        applyFocus(m.playButton, true, "onNavigateTo() - DetailController.brs")
                    else
                        m.relatedFocusDetail.visible = false
                        applyFocus(m.seasonRowList, true, "onNavigateTo() - DetailController.brs")
                    end if
                else if m.seasonRowList.hasFocus()
                    m.seasonTitle.translation = [70, 568]
                    m.seasonRowList.translation = [255, 560]
                    m.detailsRowList.translation = [70, 570]
                    m.moviesDetail.visible = true
                    m.relatedFocusDetail.visible = false
                    applyFocus(m.playButton, true, "onNavigateTo() - DetailController.brs")
                else
                    applyFocus(m.addButton, true, "onNavigateTo() - DetailController.brs")
                end if
            else if key = m.constants.REMOTE_KEYS.DOWN
                resumeButton = m.btnLayOut.getChild(2)
                if (m.playButton.hasFocus() or (resumeButton <> invalid and resumeButton.hasFocus()) or (m.trailerButton <> invalid and m.trailerButton.hasFocus()))
                    if not m.seasonRowList.visible and m.detailsRowList.visible
                        m.moviesDetail.visible = false
                        m.relatedFocusDetail.visible = true
                        m.detailsRowList.translation = [70, 100]
                        applyFocus(m.detailsRowList, true, "onNavigateTo() - DetailController.brs")
                    else if m.seasonRowList.visible
                        m.relatedFocusDetail.visible = false
                        applyFocus(m.seasonRowList, true, "onNavigateTo() - DetailController.brs")
                    end if
                else if m.seasonRowList.hasFocus()
                    m.moviesDetail.visible = false
                    m.relatedFocusDetail.visible = true
                    m.seasonTitle.translation = [65, 70]
                    m.seasonRowList.translation = [255, 70]
                    m.detailsRowList.translation = [70, 100]
                    applyFocus(m.detailsRowList, true, "onNavigateTo() - DetailController.brs")
                else
                    handled = componentFocusHandler(key, focusMap, currentFocusComp)
                end if
            end if
        end if
        handled = true
    else
        handled = componentFocusHandler(key, focusMap, currentFocusComp)
    end if

    return handled

end function

function createFocusMap()
    focusMap = {}
    m.trailerButton = m.btnLayOut.getChild(1)
    m.resumeButton = m.btnLayOut.getChild(2)

    focusMap[m.addButton.id] = { up: invalid, down: m.playButton.id, left: invalid, right: m.thumbUpButton.id }
    focusMap[m.thumbUpButton.id] = { up: invalid, down: m.playButton.id, left: m.addButton.id, right: m.thumbDownButton.id }
    focusMap[m.thumbDownButton.id] = { up: invalid, down: m.playButton.id, left: m.thumbUpButton.id, right: m.micButton.id }
    focusMap[m.micButton.id] = { up: invalid, down: m.addButton.id, left: m.thumbDownButton.id, right: m.searchButton.id }
    focusMap[m.searchButton.id] = { up: invalid, down: m.addButton.id, left: m.micButton.id, right: invalid }

    if m.resumeButton <> invalid and m.trailerButton <> invalid
        focusMap[m.playButton.id] = { up: invalid, down: invalid, left: invalid, right: m.trailerButton.id }
        focusMap[m.resumeButton.id] = { up: invalid, down: invalid, left: m.trailerButton.id, right: invalid }
        focusMap[m.trailerButton.id] = { up: invalid, down: invalid, left: m.playButton.id, right: m.resumeButton.id }
    else if m.resumeButton <> invalid
        focusMap[m.playButton.id] = { up: invalid, down: invalid, left: invalid, right: m.resumeButton.id }
        focusMap[m.resumeButton.id] = { up: invalid, down: invalid, left: m.playButton.id, right: invalid }
    else if m.trailerButton <> invalid
        focusMap[m.playButton.id] = { up: invalid, down: invalid, left: invalid, right: m.trailerButton.id }
        focusMap[m.trailerButton.id] = { up: invalid, down: invalid, left: m.playButton.id, right: invalid }
    else
        focusMap[m.playButton.id] = { up: invalid, down: invalid, left: invalid, right: invalid }
    end if

    return focusMap
end function



function onPlayButtonSelected(event as object)
    m.selectedItemId = invalid
    initiateVideoPlayBack(false)
end function


sub onResumeButtonSelected(event as object)
    m.selectedItemId = invalid
    btnInfo = event.getRoSGNode()
    initiateVideoPlayBack(true)
end sub


sub initiateVideoPlayBack(isResume as boolean)
    relatedData = invalid
    seasonData = invalid
    pageInfo = invalid

    if m.is_Season_Content then seasonData = m.seasons
    if m.is_Related_Content then relatedData = m.related_content

    if isResume
        if m.seasonRowList.visible
            pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: m.videoContent.resumeInfo.series_slug, isResume: isResume, content_Id: m.videoContent.resumeInfo.id, content_Type: m.contentType, content: invalid, currentIndex: (m.videoContent.resumeInfo.season_number - 1), continueWatch: false, deeplink: m.deeplink })
        else
            pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { content: m.videoContent, isResume: isResume, relatedData: relatedData, seasonData: seasonData, currentIndex: invalid, deeplink: m.deeplink })
        end if
    else
        if m.seasonRowList.visible
            pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: m.videoContent.slug, isResume: isResume, content_Id: invalid, content_Type: m.contentType, content: invalid, currentIndex: 0, continueWatch: false, deeplink: m.deeplink })
        else
            pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { content: m.videoContent, isResume: isResume, relatedData: relatedData, seasonData: seasonData, currentIndex: 0 })
        end if
    end if
    m.scene.loadController = pageInfo
end sub

sub onSearchButtonSelected()
    pageInfo = createPageInfo(m.constants.CONTROLLERS.SEARCH, { goBackToSliderSearch: true })
    m.scene.loadController = pageInfo
end sub