function init()
  m.top.id = "SeriesDataResponse"
end function

function parseData(data)

  if ( isValid(data) )


    m.top.status = data.status
    m.top.data = data.data
    m.top.msg = data.msg

  end if

end function



