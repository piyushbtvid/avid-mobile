function TVService(APIServiceProvider as object)

    return {

        APIServiceProvider: APIServiceProvider,

        getConfigData: function()
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "config"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: { "content-type": "application/json" }
                modelType: "liveStreamModel",
            }

        end function,

        getContent: function(login_Token)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/home" '+ sectionID.tostr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }

        end function,

        getMoviesContent: function(login_Token)
            limit = 1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/movies?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }

        end function,

        getSeriesData: function(login_Token)
            limit = 1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/series?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel"
            }
        end function,

        getDetails: function(video_id, login_Token)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/content/" + video_id.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }

        end function,

        getLiveStreamContent: function()

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "active-channels-with-schedules"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                }
                modelType: "liveStreamModel",
            }
        end function,

        getGuideStreamContent: function()
            return {
                method: "GET",
                url: "https://api.airy.tv/api/v2.1.7/channels?device=roku" 'm.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "active-channels-with-schedules"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: { "content-type": "application/json" }
                modelType: "liveStreamModel",
            }
        end function,


        getMyListData: function(login_Token)
            limit=1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/my-list?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "liveStreamModel",
                'data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
            }
        end function,

        getContinueWatchListData: function(login_Token)
            limit=1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/continue-watching?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "liveStreamModel",
                headers: { "content-type": "application/json", "X-Device-Id": getDeviceId(), "X-Device-Type": "roku", "Authorization": "Bearer " + login_Token }
            }
        end function,

        getAddMyListData: function(login_Token, videoId)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/my-list/" + videoId
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "liveStreamModel",
                'data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
            }
        end function,

        getAddMyContinueWatching: function(login_Token, position, video_id, duration)

            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if

            body = { progress_seconds: position, slug: video_id, duration: duration }
            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/continue-watching"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "liveStreamModel",
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
            }
        end function,

        getDeleteMyListData: function(login_Token, videoId)

            return {
                method: "DELETE",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/my-list/" + videoId
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "liveStreamModel",
                'data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
            }
        end function,

        doLikeOrDislikeContent: function(likeOrDislike as string, videoId, login_Token)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if

            body = { type: likeOrDislike }

            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/like-dislike/" + videoId.tostr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                data: body,
                modelType: "liveStreamModel",
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
            }

        end function,

        getLikedListData: function(login_Token)
            limit=1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/liked?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }

        end function,

        getDisLikedListData: function(login_Token)
            limit=1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/disliked?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }

        end function,

        getCreatorsListData: function(login_Token)
            limit=1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/creators/list?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "CreatorDataResponseModel",
            }

        end function,

        getCreatorsDetailsData: function(creator_id, login_Token)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/creators/" + creator_id.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "CreatorDataResponseModel",
            }

        end function,


        getCreatorsContentListData: function(creator_id, login_Token)
            limit=1000
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/creators/" + creator_id.toStr() + "/content?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "CreatorDataResponseModel",
            }

        end function,

        getGenreByID: function(genre_id)
            limit = 1000
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/genres/" + genre_id.toStr() + "?limit=" + limit.toStr()
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                }
                modelType: "liveStreamModel",
            }

        end function,

        getSearchResult: function(login_Token, searchTex)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/search?query=" + searchTex
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "SeriesDataResponse",
            }
        end function,

        getRecentSearchList: function(login_Token)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/recent-search"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        postRecentSearchList: function(login_Token, selectedId, content_type)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            body = { content_type: content_type, content_id: selectedId }

            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/recent-search"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getLiveChannelContent: function(login_Token)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/live-channels"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "SeriesDataResponse",
            }
        end function,

        getRefreshToken: function(refresh_token)
            '?"refresh_tokenrefresh_tokenrefresh_token " refresh_token
            body = { refresh_token: refresh_token }

            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/refresh-token"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "UserModel",
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    '"content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
            }
        end function,

        getQrCode: function()
            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/activation-code/generate",
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "UserModel",
                'data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku"
                }
            }
        end function

        getQrCodeStatus: function()


            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/activation-code/status"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "UserModel",
                'data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku"
                }
            }
        end function,

        getLogout: function(login_Token)
            if login_Token = invalid
                login_Token = ""
            else
                login_Token = login_Token
            end if
            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/logout"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "UserModel",
                'data: body,

                headers: {
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku"
                }
            }
        end function,

        doLogin: function(loginData)

            body = { email: loginData.email, password: loginData.password }

            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/login"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                modelType: "UserModel",
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku"
                }
            }
        end function,

        getProfile: function(login_Token)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profile"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getAllProfile: function(login_Token)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profiles"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getSingleProfile: function(login_Token, id)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profiles" + id
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getCurrentProfile: function(login_Token)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/me"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getCreateProfile: function(login_Token, data)

            body = { name: data.name, avatar: data.avatar, language: "english", preferences: "" }

            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profiles/create"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getProfilePic: function(login_Token)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/avatars"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getSingleProfile: function(login_Token, id)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profiles/" + id
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getUpdateProfile: function(login_Token, data, id)
            body = { name: data.name, avatar: data.avatar, language: "english", preferences: "" }
            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profiles/" + id
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getSetProfile: function(login_Token, id)

            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profiles/set/" + id
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getDeleteProfile: function(login_Token, id, data)
            body = { name: data.name, avatar: data.avatar, language: "english", preferences: "" }
            return {
                method: "DELETE",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/profiles/" + id
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getPurchase: function(login_Token, data)
            '?"datadatadatadatadata " data, login_Token
            body = { receipt_id: data.receipt_id, platform: data.platform }
            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/purchase"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getSubscriptionDetail: function(login_Token)

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/subscription/"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                    "content-type": "application/json", "Authorization": "Bearer " + login_Token
                }
                modelType: "liveStreamModel",
            }
        end function,

        getRegister: function(data)
            body = { name: data.name, email: data.email, password: data.password }
            '?"bbbbbbbbbbbbbbb " body
            return {
                method: "POST",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "client/v1/subscribers/register"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                data: body,
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                }
                modelType: "UserModel",
            }
        end function,

        getConfiq: function()

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "v1/config"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                }
                modelType: "liveStreamModel",
            }
        end function,
       
        getAdApi: function()

            return {
                method: "GET",
                url: m.APIServiceProvider.REMOTE_CONFIG_BASE_URL + "v1/ads"
                httpResponse: CreateObject("roSGNode", "HttpResponseNode"),
                headers: {
                    "content-type": "application/json",
                    "X-Device-Id": getDeviceId(),
                    "X-Device-Type": "roku",
                }
                modelType: "liveStreamModel",
            }
        end function,

    }

end function