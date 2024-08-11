package com.goods.client.data

class Constants {

    interface URL_CONSTANTS{
        companion object{
            const val API_URL = "https://be-ksp.analitiq.id/"
        }
    }

    interface GRANT_TYPE_PATTERN{
        companion object{
            const val PASSWORD = "password"
        }
    }

    interface PREFERENCES{
        companion object{
            const val APP_PREFERENCES = "APP_PREFERENCES"
            const val TOKEN_KEY = "TOKEN_KEY"
            const val USERNAME_KEY = ""
            const val EMAIL_KEY = "EMAIL_KEY"
            const val PASSWORD_KEY = "PASSWORD_KEY"
        }
    }
}