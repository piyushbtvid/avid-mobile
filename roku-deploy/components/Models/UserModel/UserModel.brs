function init()
    m.top.id = "UserModel"
  end function

  function parseData(data)

    if ( isValid(data) )

      m.top.status = data.status
      m.top.data = data.data
      m.top.msg = data.message

    end if

  end function