' ******************************************************
' RegistryService / LocalStorage to save and retrieve from local storage
' Usage:
'
' ******************************************************
function LocalStorage()
  this = {}

  ' ******************************************************
  ' Initialization
  ' ******************************************************
  this.initialize = function()
    m.localStorage = CreateObject("roRegistrySection", "com.app.ReDiscoverTv")
    jsonConfig = invalid
    config = invalid
  end function

  ' ******************************************************
  ' Checks if a key is present in localstorage
  ' ******************************************************
  this.hasKey = function(key) as boolean
    return m.localStorage.exists(key)
  end function

  ' ******************************************************
  ' Get all keys
  ' ******************************************************
  this.allKeys = function()
    return m.localStorage.GetKeyList().toArray()
  end function

  ' ******************************************************
  ' Gets the value for the key
  ' ******************************************************
  this.getValueForKey = function(key)
    if m.localStorage.exists(key)
      return m.localStorage.read(key)
    end if
    return invalid
  end function

  this.getValuesForKeys = function(keysArray)
    result = m.localStorage.readMulti(keysArray)
    if result.Count() = 0 then return invalid
    return result
  end function

  ' ******************************************************
  ' Sets the value for the key
  ' ******************************************************
  this.setValueForKey = function(value, key)
    m.localStorage.write(key, value)
    m.localStorage.flush()
  end function

  ' ******************************************************
  ' Sets values for the key
  ' ******************************************************
  this.setValuesForKeys = function(obj)
    for each key in obj
      m.setValueForKey(obj[key], key)
    end for
  end function

  ' ******************************************************
  ' Removed a key-value entry from the localStorage
  ' ******************************************************
  this.removeKey = function(key)
    m.localStorage.delete(key)
    m.localStorage.flush()
  end function

  ' ******************************************************
  ' Removed an array of keys entries from the localStorage
  ' ******************************************************
  this.removeKeys = function(keys)
    for each key in keys
      m.localStorage.delete(key)
    end for
    m.localStorage.flush()
  end function

  ' ******************************************************
  ' Clear LocalStorage
  ' ******************************************************
  this.clearLocalStorage = function()
    keys = m.allKeys()
    for each key in keys
      m.removeKey(key)
    end for
  end function

  this.initialize()
  return this
end function
