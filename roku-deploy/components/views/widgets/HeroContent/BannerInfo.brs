function init()

  m.top.id = "BannerInfo"
  m.description = m.top.findNode("description")
  m.title = m.top.findNode("title")
  m.timimg = m.top.findNode("timimg")
  ' m.showcase_title = m.top.findNode("showcase_title")

end function

sub onContentChanged(nodeEvent as object)
    content = nodeEvent.getData()

    if content.media <> invalid and content.media.title <> invalid
      m.title.text = content.media.title
    end if

    if content.media <> invalid and content.media.description <> invalid
      m.description.text = content.media.description
    end if

    ' m.title.text = content.title
    ' m.description.text = content.description
    ' m.timimg.text = content.duration

    if content.title <> invalid AND content.title.len() > 0
      m.title.text = content.title
    else if content.seoTitle <> invalid
      m.title.text = content.seoTitle
    end if 

    if content.description <> invalid AND content.description.len() > 0
      m.description.text = content.description
    else if content.seoDescription <> invalid
      m.description.text = content.seoDescription
    end if 
    
    ' m.timimg.text = content.duration
end sub

