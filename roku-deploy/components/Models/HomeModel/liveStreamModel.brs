function init()
    m.top.id = "liveStreamModel"
  end function

  function parseData(data)
    if (isValid(data))
        m.top.data = data
    end if
  end function