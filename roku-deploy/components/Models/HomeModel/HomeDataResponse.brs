function init()
  m.top.id = "HomeDataResponse"
end function

function parseData(data)

  if ( isValid(data) )


    m.top.status = data.status
    m.top.data = data.data
    m.top.msg = data.msg
    m.top.response_data = data.response_data
    m.top.status_code = data.status_code

  end if

end function



