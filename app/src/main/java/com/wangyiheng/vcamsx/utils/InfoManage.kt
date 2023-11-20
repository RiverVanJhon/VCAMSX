package com.wangyiheng.vcamsx.utils

import android.content.Context
import com.crossbowffs.remotepreferences.RemotePreferences
import com.wangyiheng.vcamsx.data.models.VideoStatues
import com.google.gson.Gson
class InfoManager(private val context: Context) {
    val prefs = RemotePreferences(context, "com.wangyiheng.vcamsx.preferences", "main_prefs")
    private val gson = Gson()
    fun saveVideoStatus(videoStatus: VideoStatues) {
        val jsonString = gson.toJson(videoStatus)
        prefs.edit().putString("videoStatus", jsonString).apply()
    }

    fun getVideoStatus(): VideoStatues? {
        val jsonString = prefs.getString("videoStatus", null)
        return if (jsonString != null) {
            gson.fromJson(jsonString, VideoStatues::class.java)
        } else {
            null
        }
    }

    fun removeVideoStatus() {
        prefs.edit().remove("videoStatus").apply()
    }
}