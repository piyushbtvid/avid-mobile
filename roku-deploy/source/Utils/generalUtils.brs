'-----------------------------------------------------
' Contains General Utility methods'
'-----------------------------------------------------

' Checks if object is valid or not'
' @param Dynamic entity'
' @return Boolean
function isValid(entity as dynamic) as boolean

    if ((entity = invalid) or (type(entity) = "roInvalid"))
        return false
    end if

    return true
end function

function isUnInitialized(value as dynamic) as boolean
    return (type(value) <> "<uninitialized>" and value <> invalid and (type(value) = "roInvalid"))
end function


' Checks if object is a string or not
' @param object string
' @return boolean
function isString(str as dynamic) as boolean
    strType = LCase(type(str))
    return (strType = "string") or (strType = "rostring")
end function


' Checks if value is an object or not
' @param object
' @return boolean
function isObject(obj as object) as boolean

    if (not isValid(obj)) then return false

    objType = LCase(type(obj))
    return objType = "roassociativearray"
end function


' Checks if value is a Boolean or not
' @param boolean
' @return boolean
function isBoolean(obj as boolean) as boolean
    objType = LCase(type(obj))
    return (objType = "boolean") or (objType = "roboolean")
end function


' Checks if value is an array or not
' @param array
' @return boolean
function isArray(arr as object) as boolean

    if (not isValid(arr)) then return false

    objType = LCase(type(arr))
    return (objType = "roarray") or (objType = "array")
end function


' Checks if value is an Integer or not
' @param integer
' @return boolean
' function isInteger( int as Integer ) as Boolean
'   objType = LCase( type(int) )
'   return ( objType = "integer" ) or ( objType = "roInteger" )
' end function

function IsInteger(movie_id as dynamic) as boolean
    return IsValid(movie_id) and GetInterface(movie_id, "ifInt") <> invalid and (Type(movie_id) = "roInt" or Type(movie_id) = "roInteger" or Type(movie_id) = "Integer")
end function


function isValidString(str as dynamic) as string
    returnValue = ""

    if ((str = invalid) or (type(str) = "roInvalid")) then return returnValue
    if str = "" then return returnValue
    return str

end function

function isValidArray(arr as dynamic) as object
    returnValue = []

    if ((arr = invalid) or (type(arr) = "roInvalid")) then return returnValue
    if arr.count() = 0 then return returnValue
    return arr

end function


' Returns a random UUID
' @return String
function generateUUID() as string
    return CreateObject("roDeviceInfo").GetRandomUUID()
end function


' Returns if the device is a low end device or not'
' @return Boolean'
function isLowEndDevice() as boolean

    lowEndDeviceList = GetConstants().LOW_END_DEVICE

    deviceInfo = CreateObject("roDeviceInfo")
    modelNo = deviceInfo.GetModel()

    return (deviceInfo.GetGraphicsPlatform() = "directfb") or (lowEndDeviceList.DoesExist(Lcase(modelNo)))
end function

' Capitalizes the first letter of the given text
' @param string text
' @return string text
function CapitalizeString(text = "" as string)

    returnValue = ""

    for each word in text.Tokenize(" ")
        returnValue += CapitalizeWord(word) + " "
    end for

    return Left(returnValue, Len(returnValue) - 1)
end function

' Capitalizes the first letter of the given text
' @param string text
' @return string text
function CapitalizeWord(text = "" as string)
    return UCase(Left(text, 1)) + LCase(Right(text, Len(Text) - 1))
end function


' Creates a shallow copy of a given node
' @param object item
' @return object new item
function shallowNodeCopy(item as object)

    copy = CreateObject("roSGNode", item.subtype())

    fields = item.getFields()

    ' NOTE: Had to do this. This stops warnings thrown by Roku
    fields.Delete("change")
    fields.Delete("focusedChild")

    copy.setFields(fields)
    return copy

end function


' Returns the query joining symbol ( & or ? )
' @param string url
' @return string symbol ( & or ? )
function getQueryStringSymbol(url as string)

    symbol = "&"
    if ((isValid(url)) and (url.Instr("?") = -1)) then symbol = "?"

    return symbol
end function


' Strips html <p> and &amp from strings
' @param string s as string
' @return string cleaned String
function stripHTMLFromString(s)

    if (not isValid(s))
        return ""
    end if

    tagsRegEx = CreateObject("roRegex", "<[a-zA-Z\/][^>]*>", "m")
    s = tagsRegEx.ReplaceAll(s, "")

    entitiesRegEx = CreateObject("roRegex", "(&[a-zA-Z]*;)", "m")
    s = entitiesRegEx.ReplaceAll(s, "")

    return s

end function

' Returns device id
' @return string
function getDeviceId()

    di = CreateObject("roDeviceInfo")
    return di.GetChannelClientId()

end function


' Returns device id
' @return string
function GetUIResolution()

    di = CreateObject("roDeviceInfo")
    return di.GetUIResolution()

end function


' Finds the first level children by matching ID names for content nodes
' @param Object parent content node
' @param String name - id of the child node
' @return object - child content node or invalid if it does not exist
function findChildById(parent as object, name as string)

    totalChildCount = parent.getChildCount()
    children = parent.getChildren(totalChildCount, 0)

    for each child in children
        if (child.id = name) return child
    end for

    return invalid

end function

' Finds the row position in RowList
' @param Object parent content node
' @param String name - id of the child node
' @return object - child content node position or invalid if it does not exist
function findChildPos(parent as object, name as string)

    totalChildCount = parent.getChildCount()
    children = parent.getChildren(totalChildCount, 0)

    for i = 0 to children.Count()
        child = children[i]
        if (isValid(child) and child.id = name) then return i
    end for

    return invalid

end function

function convertSecToMinute(secs)
    duration = ""
    mins = convertSecToMins(secs)
    if mins > 0 then
        duration = duration + mins.tostr() + " : "
    end if

    secs = findSecondsLeft(secs)
    if secs > 0 then
        duration = duration + secs.tostr()
    end if
    return duration
end function

function FormatDuration(seconds as integer) as string
    minutes = Int(seconds / 60)
    remainingSeconds = seconds mod 60

    result = ""
    if minutes > 0
        result = minutes.ToStr() + "m"
    end if

    if remainingSeconds > 0 or minutes = 0
        if result <> "" then
            result = result + " "
        end if
        result = result + remainingSeconds.ToStr() + "s"
    end if

    return result
end function

function FormatDurationwithHr(seconds as integer) as string
    hours = Int(seconds / 3600)
    remainingSeconds = seconds mod 3600
    minutes = Int(remainingSeconds / 60)
    remainingSeconds = remainingSeconds mod 60

    result = ""
    
    if hours > 0
        result = hours.ToStr() + "hr"
    end if

    if minutes > 0
        if result <> "" then result = result + " "
        result = result + minutes.ToStr() + "m"
    end if

    if (remainingSeconds > 0) or (hours = 0 and minutes = 0)
        if result <> "" then result = result + " "
        result = result + remainingSeconds.ToStr() + "s"
    end if

    return result
end function


function convertSecToMins(secs)
    if secs <> invalid
        mins = 0
        mins = Fix(secs / 60)
        return mins
    end if
    return ""

end function

function findSecondsLeft(secs)
    secs = Fix(secs mod 60)
    return secs
end function

function isValidEmail(email as string) as boolean
    ' Define the regular expression for a valid email address
    validEmailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"

    ' Create the roRegex object with 3 arguments
    regex = CreateObject("roRegex", validEmailPattern, "")

    ' Use the IsMatch() method to validate the email
    if regex.IsMatch(email)
        return true
    else
        return false
    end if
end function


function getRuntime(runtime as integer) as string
    runtimeSecs = runtime mod 60
    runtimeMins = (runtime \ 60) mod 60
    runtimeHours = (runtime \ 3600)

    if runtimeSecs < 10
        runtimeSecs = "0" + runtimeSecs.toStr()
    else
        runtimeSecs = runtimeSecs.toStr()
    end if

    if runtimeMins < 10
        runtimeMins = "0" + runtimeMins.toStr()
    else
        runtimeMins = runtimeMins.toStr()
    end if

    if runtimeHours > 9
        totalRuntime = runtimeHours.toStr() + ":" + runtimeMins + ":" + runtimeSecs
    else if runtimeHours > 0 and runtimeHours < 9
        totalRuntime = "0" + runtimeHours.toStr() + ":" + runtimeMins + ":" + runtimeSecs
    else
        totalRuntime = + runtimeMins + ":" + runtimeSecs
    end if

    return totalRuntime
end function

function ValueExistsInArray(arr as object, value as dynamic) as boolean
    ' Iterate through the array
    for each item in arr
        ' Check if the current item matches the value
        if item = value
            return true
        end if
    end for

    ' If no match is found, return false
    return false
end function


function GetFormattedDate(timestamp as integer) as string
    date = CreateObject("roDateTime")
    date.FromSeconds(timestamp)
    return date.AsDateString("long-date")  ' Returns something like: "Wednesday March 15, 2023"
end function


function unixToReadableTime(unixTime as Integer) as String
    date = CreateObject("roDateTime")
    date.FromSeconds(unixTime)

    formattedTime = date.AsDateString("full-date-long-weekday")
    formattedTime += " " + date.GetHours().ToStr() + ":" + date.GetMinutes().ToStr() + ":" + date.GetSeconds().ToStr()
    
    return formattedTime
end function

function getCurrentUnixTimestamp() as Integer
    date = CreateObject("roDateTime")
    currentTime = date.AsSeconds()  ' Returns current time in Unix timestamp (Integer)
    return currentTime
end function

function getDisplayMode() as string
	gaa = getGlobalAA()
	if gaa.displayMode = invalid then
		deviceinfo = CreateObject("roDeviceInfo")
		displaySize = deviceinfo.getDisplaySize()
		if displaySize.h = 1080
			gaa.displayMode = "FHD"
		else if displaySize.h = 720
			gaa.displayMode = "HD"
		else if displaySize.h = 480
			gaa.displayMode = "SD"
		end if
		return gaa.displayMode
	else
		return gaa.displayMode
	end if
end function