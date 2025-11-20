function init()
  m.top.id = "CategoryContentResponse"
end function

function parseData(data)
  ?"data " data
  if ( isValid(data) )
    m.top.status = data.status
    m.top.data = data
  end if
end function



