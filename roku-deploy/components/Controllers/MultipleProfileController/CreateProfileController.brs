sub init()

    m.top.id = "CreateProfileController"
    m.scene = getScene()
    m.constants = GetConstants()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)
    m.localStorage = LocalStorage()


    m.nameInput = m.top.findNode("nameInput")
    m.nameInput.width = 400
    m.nameInput.translation = [(1920 - m.nameInput.width) / 2, 370]
    m.nameInput.observeField("text", "onNameInputChanged")

    m.submitBtn = m.top.findNode("submitBtn")
    m.submitBtn.observeField("buttonSelected", "onSave")
    m.deleteBtn = m.top.findNode("deleteBtn")
    m.deleteBtn.observeField("buttonSelected", "deleteFun")

    m.headingTitle = m.top.findNode("headingTitle")


    m.profileGrid = m.top.findNode("profileGrid")
    m.profileGrid.observeField("rowItemSelected", "onProfileImageSelected")
    m.profileGrid.observeField("itemFocused", "onProfileImageFocused")

    m.keyboardDialog = m.top.findNode("nameKeyboard")
    m.keyboardDialog.visible = true
    m.keyboardDialog.observeFieldScoped("buttonSelected", "dismissDialog")
    m.keyboardDialog.observeFieldScoped("text", "handleTextEdit")
    m.keyboardDialog.textEditBox.observeField("text", "onKeyboardTextChanged")


    screenBounds = m.top.boundingRect()
    keyboard = m.top.findNode("nameKeyboard")

    kbBounds = keyboard.boundingRect()
    kbWidth = kbBounds["width"]
    kbHeight = kbBounds["height"]
    centerX = (screenBounds["width"] - kbWidth) / 2
    centerY = 500
    keyboard.translation = [centerX, centerY]

    m.errorText = m.top.findNode("errorText")
    m.errorText = m.top.findNode("errorText")
    m.errorTextTimer = m.top.findNode("errorTextTimer")
    m.errorTextTimer.ObserveField("fire", "hideError")
    m.lastText = ""
    m.updatingText = false
end sub


sub onNavigateTo(params as object)

    m.editProfileFlag = params.editProfile
    if isValid(params) and isValid(params.data) and isValid(params.editProfile)
        m.profileData = params.data
        m.profileId = params.profile_id
        m.nameInput.active = true
        m.headingTitle.text = "Update Profile"
        m.nameInput.text = m.profileData.name
        m.nameInput.cursorPosition = len(m.profileData.name)
        if m.profileData.is_default <> true 
            m.deleteBtn.imgSrc = { id: "CreateProfielBtn", label: "Delete", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
        end if
        m.submitBtn.imgSrc = { id: "CreateProfielBtn", label: "Update", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
    else
        m.nameInput.active = true
        m.headingTitle.text = "Choose Your Profile"
        m.submitBtn.imgSrc = { id: "CreateProfielBtn", label: "Submit", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
    end if
    m.submitBtn.translation = [(1920 - m.submitBtn.imgSrc.width) / 2, 900]
    m.deleteBtn.translation = [1550, 90]

    if m.top.visible = true then
        profileImgApi()
    end if
end sub


sub profileImgApi()
    login_Token = LocalStorage().getValueForKey("login_Token")
    creatorPageContent = TVService(m.apiProvider).getProfilePic(login_Token)
    creatorPageContent.httpResponse.observeField("response", "profileImgResponse")
    m.http.sendRequest(creatorPageContent)
end sub

sub profileImgResponse(event)
    response = event.getData()
    if not isValid(response) then return

    if isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        profilePicData = response.data.data.data

        contentNode = createObject("roSGNode", "ContentNode")
        rowNode = createObject("roSGNode", "ContentNode")


        for each imageUri in profilePicData

            if isValid(imageUri) and isValid(imageUri.image_url)
                item = rowNode.CreateChild("ContentNode")
                item.addFields({
                    url: imageUri.image_url
                    itemText: "Profile " + (rowNode.getChildCount()).toStr()
                    picId: imageUri.id
                    isSelected: false
                    isCreateProfile: true
                })

                if m.editProfileFlag = "true" and isValid(m.profileData) and isValid(m.profileData.avatar_id) and imageUri.id = m.profileData.avatar_id
                    item.isSelected = true
                    m.imageId = imageUri.id
                    selectedIndex = rowNode.getChildCount() - 1
                end if
                rowNode.appendChild(item)
            end if
        end for

        contentNode.appendChild(rowNode)



        m.profileGrid.content = contentNode
        m.profileGrid.jumpToRowItem = [0, 2]
        applyFocus(m.keyboardDialog, true, "profileImgResponse()")



        numItems = profilePicData.count()
        itemWidth = 150
        itemSpacing = 20
        rowWidth = (itemWidth * numItems) + (itemSpacing * (numItems - 1))
        centerX = (1920 - rowWidth) / 2

        m.profileGrid.translation = [centerX, 200]


    end if
end sub


sub onProfileImageFocused(event)
    focusedIndex = event.getData()
    ' Handle focus changes if needed
end sub

sub onProfileImageSelected(event)
    selectedIndex = event.getData()
    rowIndex = selectedIndex[0]
    rowItemIndex = selectedIndex[1]

    ' Clear all selections in the RowList
    for i = 0 to m.profileGrid.content.getChildCount() - 1
        row = m.profileGrid.content.getChild(i)
        for j = 0 to row.getChildCount() - 1
            row.getChild(j).isSelected = false
        end for
    end for

    ' Set the selected item
    row = m.profileGrid.content.getChild(rowIndex)
    rowItem = row.getChild(rowItemIndex)
    rowItem.isSelected = true

    m.imageId = rowItem.picId
    'print "Selected image: "; m.imageId
end sub


sub handleTextEdit()
    if m.keyboardDialog = invalid then return
    m.nameInput.text = m.keyboardDialog.text
    m.nameInput.cursorPosition = len(m.keyboardDialog.text)
end sub

sub onKeyboardTextChanged(event)
    if m.updatingText then return

    newText = event.getData()
    if newText <> m.lastText
        m.updatingText = true
        m.nameInput.text = newText
        m.lastText = newText
        m.nameInput.cursorPosition = len(newText)
        m.updatingText = false
    end if
end sub

sub onNameInputChanged(event)
    if m.updatingText then return

    newText = event.getData()
    if newText <> m.lastText
        m.updatingText = true
        m.keyboardDialog.text = newText
        m.lastText = newText
        m.keyboardDialog.textEditBox.cursorPosition = len(newText)
        m.updatingText = false
    end if
end sub


sub onSave()
    validateForm()
    if m.editProfileFlag = "true"
        updateProfile()
    else
        Data = {
            name: m.nameInput.text,
            avatar: m.imageId,
            language: "english",
            preferences: ""
        }
        login_Token = LocalStorage().getValueForKey("login_Token")
        createPageContent = TVService(m.apiProvider).getCreateProfile(login_Token, Data)
        createPageContent.httpResponse.observeField("response", "addprofileUI")
        m.http.sendRequest(createPageContent)
    end if
end sub

sub addprofileUI(event)
    response = event.getData()

    if isValid(response) and isValid(response.data)
        pageInfo = createPageInfo(m.constants.CONTROLLERS.ALLPROFILESHOW, {})
        m.scene.loadController = pageInfo
    end if
end sub

function validateForm() as boolean
    if m.nameInput.text = ""
        showError("The name field is required.")
        return false
    end if

    return true
end function

sub updateProfile()
    Data = {
        name: m.nameInput.text,
        avatar: m.imageId,
        language: "english",
        preferences: ""
    }

    login_Token = LocalStorage().getValueForKey("login_Token")
    createPageContent = TVService(m.apiProvider).getUpdateProfile(login_Token, Data, m.profileId)
    createPageContent.httpResponse.observeField("response", "updateProfileUi")
    m.http.sendRequest(createPageContent)
end sub

sub updateProfileUi(event)
    response = event.getData()
    if isValid(response) and isValid(response.data)
        '?"eventevent " response.data.data
        pageInfo = createPageInfo(m.constants.CONTROLLERS.ALLPROFILESHOW, {})
        m.scene.loadController = pageInfo
    end if
end sub

sub deleteFun()
    if m.profileData.is_default <> true
        Data = {
            name: m.nameInput.text,
            avatar: m.imageId,
            language: "english",
            preferences: ""
        }

        login_Token = LocalStorage().getValueForKey("login_Token")
        deletePageContent = TVService(m.apiProvider).getDeleteProfile(login_Token, m.profileId, Data)
        deletePageContent.httpResponse.observeField("response", "deleteProfileUi")
        m.http.sendRequest(deletePageContent)
    end if
end sub

sub deleteProfileUi(event)
    response = event.getData()
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and response.data.data.status = "success"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.ALLPROFILESHOW, {})
        m.scene.loadController = pageInfo
    end if

end sub

function onKeyEvent(key as string, press as boolean) as boolean
    handled = false
    if press then

        if key = m.constants.remote_keys.BACK then
            goBackInHistory(m.scene)
            handled = true

        else if key = m.constants.remote_keys.UP
            if m.submitBtn.hasFocus()
                applyFocus(m.keyboardDialog, true, "profileImgResponse()")
                handled = true

            else if m.keyboardDialog.isInFocusChain()
                applyFocus(m.profileGrid, true, "profileImgResponse()")
                handled = true
            else if m.profileGrid.isInFocusChain() and m.editProfileFlag = "true" and m.profileData.is_default <> true 
                applyFocus(m.deleteBtn, true, "profileImgResponse()")
                handled = true
            end if
            handled = true

        else if key = m.constants.remote_keys.LEFT
            handled = true

        else if key = m.constants.remote_keys.DOWN

            if m.deleteBtn.hasFocus()
                applyFocus(m.profileGrid, true, "profileImgResponse()")
                handled = true
            else if m.profileGrid.hasFocus()
                applyFocus(m.keyboardDialog, true, "profileImgResponse()")
                handled = true
            else if m.keyboardDialog.isInFocusChain()
                applyFocus(m.submitBtn, true, "profileImgResponse()")
                handled = true
            end if

        end if
    end if
    return handled
end function


function showError(text as string)
    m.errorText.text = text
    m.errorText.visible = true
    m.errorTextTimer.control = "start"
end function

function hideError()
    m.errorText.text = ""
    m.errorText.visible = false
    m.errorTextTimer.control = "stop"
end function