package com.nohari.campus_hub

import android.app.Application
import com.cloudinary.android.MediaManager

class CampusHubApp : Application() {

    override fun onCreate() {
        super.onCreate()

        try {

            val config = HashMap<String, String>()

            config["cloud_name"] = "dkgilo0wp"
            config["api_key"] = "979484549793933"
            config["api_secret"] = "oquj3Q-K6IUXUdLh_0csyzjCrmQ"

            MediaManager.init(this, config)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}