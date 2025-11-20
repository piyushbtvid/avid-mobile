
' TODO: Once we login, the user profile object might also influenc the provider

' Returns a API Service provider object as needed by  API Service
' @param object constants
' @param object config
' @return object provider object
function APIProvider( constants = GetConstants() as Object )

  provider = {
    REMOTE_CONFIG_BASE_URL: constants.REMOTE_CONFIG.BASE_URL
    SERVER_ENV: "PROD_A"
  }

  return provider

end function
