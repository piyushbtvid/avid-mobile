function init()

  m.top.id = "HeroContent"

  m.heroBackgroundCover = m.top.findNode("heroBackgroundImage")
  m.bannerDetails = m.top.findNode("bannerDetails")
  m.heroBackgroundCover.visible = false


end function

function onContentChange(event as object)

  content = m.top.content
  if content <> invalid

    m.heroBackgroundCover.visible = true

    if content.media <> invalid
      if content.media.thumbnailImage <> invalid and content.media.thumbnailImage.len() > 0
        m.heroBackgroundCover.uri = content.media.thumbnailImage
      else if content.media.posterImage <> invalid and content.media.posterImage.len() > 0
        m.heroBackgroundCover.uri = content.media.posterImage
      end if
    end if

    if content.thumbnail_image <> invalid and content.thumbnail_image.len() > 0
      m.heroBackgroundCover.uri = content.thumbnail_image
    else if content.poster_image <> invalid and content.poster_image.len() > 0
      m.heroBackgroundCover.uri = content.poster_image
    end if

    if content.video <> invalid and content.video.thumbnail <> invalid
      m.heroBackgroundCover.uri = content.video.thumbnail
    end if

    m.bannerDetails.bannerContent = content
  end if

end function

