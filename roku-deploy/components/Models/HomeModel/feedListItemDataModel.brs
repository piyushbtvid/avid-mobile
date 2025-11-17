function init()
  m.top.id = "feedListItemDataModel"
end function

function parseData(data)

  if (isValid(data))

    m.top.type = data.type            

    m.top.content_type = data.content_type
    m.top.id = data.id
    m.top.title = data.name
    m.top.slug = data.slug
    m.top.series_slug = data.series_slug
    m.top.access = data.access
    m.top.description = data.description
    m.top.thumbnail_image = data.portrait
    m.top.poster_image = data.landscape
    m.top.rating = data.rating
    m.top.order_index = data.order_index
    m.top.release_date = data.release_date
    m.top.duration = data.duration
    m.top.video_type = data.video_type
    m.top.video_link = data.video_link
    m.top.seasonid = data.season_number
    m.top.download_enable = data.download_enable
    m.top.genre = data.genres
    m.top.subtitle_on_off = data.subtitle_on_off
    m.top.subtitle_language1 = data.subtitle_language1
    m.top.subtitle_url1 = data.subtitle_url1
    m.top.subtitle_language2 = data.subtitle_language2
    m.top.subtitle_url2 = data.subtitle_url2
    m.top.views = data.views
    m.top.progress_seconds = data.progress_seconds
    m.top.creator = data.creator
    m.top.dateUploaded = data.dateUploaded
    m.top.uploadedYear = data.uploadedYear
    m.top.channel_name = data.channel_name
    m.top.profileImg = data.profile_img
    m.top.channel_subscribers = data.channel_subscribers
    if isValid(data.episode_number) then m.top.episode_no = data.episode_number.toStr()
 
  end if

end function



