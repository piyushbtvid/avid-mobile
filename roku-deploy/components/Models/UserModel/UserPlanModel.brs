function init()
    m.top.id = "UserPlanModel"
  end function

  function parseData(data)

    if ( isValid(data) )
      m.top.response_data = data.response_data
    end if

  end function