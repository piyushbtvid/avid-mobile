
' Contains a list of constants and enums for the application'

function GetConstants()

  if (not isValid(m.constants))
    m.constants = {

      ' App navigation types
      NAVIGATION_TYPES: {
        NAVIGATE_TO: "NAVIGATE_TO",
        NAVIGATE_AWAY: "NAVIGATE_AWAY"
      },

      PLATFORMS: {
        ROKU: "roku"
        ANDROID: "android"
        ios: "ios"
      }

      ' App Controllers
      CONTROLLERS: {
        HOME: "HomeController",
        CREATOR: "CreatorController",
        LOGIN: "LoginController",
        VIDEO: "VideoController",
        ALBUM: "AlbumController",
        SEARCH: "SearchController",
        GRID: "GridController",
        SETTINGS: "SettingsController",
        DETAIL: "DetailController",
        SIGNUP: "SignUpController",
        SPLASH: "SplashController",
        ACCOUNT: "AccountController",
        GENRELIST: "GenreListController",
        CREATORDETAIL: "CreatorDetailController",
        SERIES: "SeriesController",
        MOVIES: "MoviesController",
        LIVETV: "LiveTvController",
        GUIDE: "GuideController",
        MYLIST: "MyListController",
        QRCODE: "QrCodeScreenController"
        BROWSE: "BrowserController",
        SUBSCRIPTIONPLAN: "SubscriptionPlanController",

        ALLPROFILESHOW: "AllProfileController",
        CREATEPROFILE: "CreateProfileController"
        STREAM: "StreamController",
        LIVETVSTREAM: "LiveTvStreamController"
      },

      ' HTTP Types'
      HTTP_TYPES: {
        GET: "GET",
        POST: "POST",
        DELETE: "DELETE",
        PUT: "PUT"
      },

      REGISTRY_ACTION_TYPES: {
        READ: "READ",
        WRITE: "WRITE",
        DELETE: "DELETE"
      },

      NAV_ITEMS: [
        "LIVE",
        "VOD ",
        "SIGN-IN"
      ],
      NAV_ITEMS_LOGIN: [
        "LIVE",
        "VOD ",
        "PROFILE"
      ],

      LOW_END_DEVICE: {
        "2400x": true,
        "3000x": true,
        "3050x": true,
        "3100x": true,
        "2450x": true,
        "2500x": true,
        "3400x": true,
        "3420x": true,
        "2700x": true,
        "2710x": true,
        "2720x": true,
        "3500x": true,
        "3700x": true,
        "3710x": true,
        "5000x": true
      },

      UI_BUTTON_STATES: {
        FOCUSED: "focused",
        UNFOCUSED: "unfocused",
        PRESSED_FOCUSED: "pressed_focused",
        PRESSED_UNFOCUSED: "pressed_unfocused",
        DISABLED: "disabled"
      },

      REMOTE_KEYS: {
        BACK: "back",
        UP: "up",
        DOWN: "down",
        LEFT: "left",
        RIGHT: "right",
        OK: "OK",
        REPLAY: "replay",
        PLAY: "play"
        REWIND: "rewind",
        FASTFORWARD: "fastforward",
        OPTIONS: "options"
      },

      DIALOGS_TITLES: {
        INVALD_INPUT: "Sorry! Invalid Input",
        LOGIN_FAILED: "Authentication Failed",
        CONFIRM_LOGOUT: "Confirm Logout"
      },

      DIALOGS_BUTTONS: {
        OK_TRY_AGAIN: "OK_TRY_AGAIN",
        FORGOT_PASSWORD: "Forgot Password?",
        GO_BACK: "Go Back",
        LOG_OUT: "Yes, Log Out"
      },

      LOGIN_ERRORS: {
        EMPTY_EMAIL: "Email field cannot be empty. Please go back and enter your email address.",
        EMPTY_PASSWORD: "Password field cannot be empty. Please go back and enter your password.",
        INVALID_EMAIL: "Please enter a valid email address. For example: random123@example.com",
        UNSUCCESFULL_LOGIN: "The email or password you entered is incorrect. Please check your credentials and try again.",
        LOGOUT_CONFIRM_MESSAGE: "Are you sure you want to log out?"
      },

      SCHEDULER: {
        TIMER_DURATION: { LOW_END_DEVICE: 0.1, HIGH_END_DEVICE: 0.01 }
      },
      REMOTE_CONFIG: {
        'dev
        'BASE_URL: "http://107.180.208.127:8001/api/"
        'prod
        'BASE_URL: "http://107.180.208.127:8000/api/"
        BASE_URL: "https://dev.api.ffmedia.tvidservices.com/api/"

      },
      IMAGE_BUTTON_ICONS: {
        PLAY_BUTTON: {
          id: "play"
          imgSrc: { "unFocusImg": "pkg:/images/play_btn_off.png", "focusImg": "pkg:/images/play_btn_on.png" },
          selectedImgSrc: { "unFocusImg": "pkg:/images/play_btn_on.png", "focusImg": "pkg:/images/play_btn_on.png" },
          imgSize: [60, 60]
        },
        PAUSE_BUTTON: {
          id: "pause"
          imgSrc: { "unFocusImg": "pkg:/images/pause_btn_off.png", "focusImg": "pkg:/images/pause_btn_on.png" },
          selectedImgSrc: { "unFocusImg": "pkg:/images/pause_btn_on.png", "focusImg": "pkg:/images/pause_btn_on.png" },
          imgSize: [60, 60]
        },
        SEARCH: {
          imgSrc: { "unFocusURI": "pkg:/images/ic_action_search.png", "focusURI": "pkg:/images/ic_action_search_selected.png" },
          selectedImgSrc: { "unFocusURI": "pkg:/images/ic_action_search.png", "focusURI": "pkg:/images/ic_action_search_selected.png" },
          imgSize: [60, 60]
        },
      },

      TOP_IMAGE_BUTTON_ICONS: [
        {
          id: "Home"
          imgSrc: "",
          text: "Home",
          icon: "pkg://images/home-top-nav.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
        {
          id: "Movies"
          imgSrc: "",
          text: "Movies",
          icon: "pkg://images/ic_stat_local_movies.png",
          imgSize: [42, 42],
          buttonSize: [148, 52]
        },
        {
          id: "Series"
          imgSrc: "",
          text: "Series",
          icon: "pkg://images/ic_stat_collections.png",
          imgSize: [42, 42],
          buttonSize: [126, 52]
        },
        {
          id: "Live"
          imgSrc: "",
          text: "Live",
          icon: "pkg://images/stream (3).png",
          imgSize: [42, 42],
          buttonSize: [180, 52]
        },
        {
          id: "Search"
          imgSrc: "",
          text: "Search",
          icon: "pkg://images/search-top-nav.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Favorites"
          imgSrc: "",
          text: "Favorites",
          icon: "pkg://images/ic_stat_favorite.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Settings"
          imgSrc: "",
          text: "Settings",
          icon: "pkg://images/set_w.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },

      ],
      ROKU_PREMIUM_SETTINGS_MENU_BUTTON_ICONS: [
        {
          id: "About us"
          imgSrc: "",
          text: "About us",
          icon: "pkg://images/about.png",
          imgSize: [42, 42],
          buttonSize: [148, 52]
        },
        {
          id: "Terms of use"
          imgSrc: "",
          text: "Terms of use",
          icon: "pkg://images/Terms.png",
          imgSize: [42, 42],
          buttonSize: [126, 52]
        },
        {
          id: "Privacy Policy"
          imgSrc: "",
          text: "Privacy Policy",
          icon: "pkg://images/privacy.png",
          imgSize: [42, 42],
          buttonSize: [180, 52]
        },
        {
          id: "Contact us"
          imgSrc: "",
          text: "Contact us",
          icon: "pkg://images/contact.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Log In"
          imgSrc: "",
          text: "Log In",
          icon: "pkg://images/profile-image.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
        {
          id: "Faq"
          imgSrc: "",
          text: "Faq's",
          icon: "pkg://images/Faq_icon.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
      ],
      SETTINGS_MENU_BUTTON_ICONS: [

        {
          id: "About us"
          imgSrc: "",
          text: "About us",
          icon: "pkg://images/about.png",
          imgSize: [42, 42],
          buttonSize: [148, 52]
        },

        {
          id: "Terms of use"
          imgSrc: "",
          text: "Terms of use",
          icon: "pkg://images/Terms.png",
          imgSize: [42, 42],
          buttonSize: [126, 52]
        },
        {
          id: "Privacy Policy"
          imgSrc: "",
          text: "Privacy Policy",
          icon: "pkg://images/privacy.png",
          imgSize: [42, 42],
          buttonSize: [180, 52]
        },
        {
          id: "Contact us"
          imgSrc: "",
          text: "Contact us",
          icon: "pkg://images/contact.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Log In"
          imgSrc: "",
          text: "Log In",
          icon: "pkg://images/profile-image.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
        {
          id: "Premium"
          imgSrc: "",
          text: "Go Premium",
          icon: "pkg://images/ic_stat_monetization_on.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
        {
          id: "Faq"
          imgSrc: "",
          text: "Faq's",
          icon: "pkg://images/Faq_icon.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
      ],
      SETTINGS_MENU_BUTTON_ICONS_LOGGEDIN_PREMIUM: [
        {
          id: "About us"
          imgSrc: "",
          text: "About us",
          icon: "pkg://images/about.png",
          imgSize: [42, 42],
          buttonSize: [148, 52]
        },
        {
          id: "Profile"
          imgSrc: "",
          text: "Profile",
          icon: "pkg://images/profile-image.png",
          imgSize: [42, 42],
          buttonSize: [148, 52]
        },
        {
          id: "Terms of use"
          imgSrc: "",
          text: "Terms of use",
          icon: "pkg://images/Terms.png",
          imgSize: [42, 42],
          buttonSize: [126, 52]
        },
        {
          id: "Privacy Policy"
          imgSrc: "",
          text: "Privacy Policy",
          icon: "pkg://images/privacy.png",
          imgSize: [42, 42],
          buttonSize: [180, 52]
        },
        {
          id: "Contact us"
          imgSrc: "",
          text: "Contact us",
          icon: "pkg://images/contact.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Log out"
          imgSrc: "",
          text: "Log out",
          icon: "pkg://images/ic_stat_logout.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Faq"
          imgSrc: "",
          text: "Faq's",
          icon: "pkg://images/Faq_icon.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
      ],
      SETTINGS_MENU_BUTTON_ICONS_LOGGEDIN_NOTPREMIUM: [
        {
          id: "About us"
          imgSrc: "",
          text: "About us",
          icon: "pkg://images/about.png",
          imgSize: [42, 42],
          buttonSize: [148, 52]
        },
        {
          id: "Profile"
          imgSrc: "",
          text: "Profile",
          icon: "pkg://images/profile-image.png",
          imgSize: [42, 42],
          buttonSize: [148, 52]
        },
        {
          id: "Terms of use"
          imgSrc: "",
          text: "Terms of use",
          icon: "pkg://images/Terms.png",
          imgSize: [42, 42],
          buttonSize: [126, 52]
        },
        {
          id: "Privacy Policy"
          imgSrc: "",
          text: "Privacy Policy",
          icon: "pkg://images/privacy.png",
          imgSize: [42, 42],
          buttonSize: [180, 52]
        },
        {
          id: "Contact us"
          imgSrc: "",
          text: "Contact us",
          icon: "pkg://images/contact.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Log out"
          imgSrc: "",
          text: "Log out",
          icon: "pkg://images/ic_stat_logout.png",
          imgSize: [42, 42],
          buttonSize: [141, 52]
        },
        {
          id: "Premium"
          imgSrc: "",
          text: "Go Premium",
          icon: "pkg://images/ic_stat_monetization_on.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
        {
          id: "Faq"
          imgSrc: "",
          text: "Faq's",
          icon: "pkg://images/Faq_icon.png",
          imgSize: [42, 42],
          buttonSize: [142, 52]
        },
      ],

      REGISTRY_SECTIONS: {
        USER_INFO: "user_info"
      },

      REGISTRY_KEYS: {
        USER_INFO: "user_info",
      },

      SCREEN: {
        HOME: "Home",
        LOGIN: "SignUp",
        VIDEO: "Video",
        SEARCH: "Search",
        SETTINGS: "Settings",
        DETAIL: "Detail",
        VIDEO: "Video",
        Channel: "Channel",
        MOVIES: "Movies",
        SERIES: "Series",
        LIVETV: "LiveTv",
        SERIESDETAIL: "SeriesDetail",
        SIGNUP: "SignUp",
        FAVOURITES: "Favourites",
        PREMIUM: "Premium",
        RESETPASSWORD: "ResetPassword",
        PROFILE: "Profile",
        ADDPROFILE: "AddProfile"
        LOGIN_IN: "Login",
        FAQ: "Faq",
      },

    }
  end if

  return m.constants
end function
