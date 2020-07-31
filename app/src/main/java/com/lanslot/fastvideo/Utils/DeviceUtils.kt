package com.lanslot.fastvideo.Utils

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import java.util.*

object DeviceUtils {

    /**
     * 获取设备唯一 ID
     * @param context 上下文
     * @return 设备唯一 ID
     */
    public fun getUniqueId(context: Context): String {
        // 不选用需要权限的获取 ID 方式
        val data = getAndroidId(context) + getSerialNumber() + getUniquePsuedoId() + getUuid(context)
        return data
    }
    /**
     * 获取 UUID
     * @param context 上下文
     */
    private fun getUuid(context: Context): String? {
        // UUID 键
        val key = "key_uuid"
        // 获取 SharedPreferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        // 获取 UUID
        var uuid: String? = preferences.getString(key, "")
        // UUID 为空值
        if (uuid.isNullOrBlank()) {
            // 创建新的 UUID
            uuid = UUID.randomUUID().toString()
            // 保存
            preferences?.edit()?.putString(key, uuid)?.apply()
        }
        return uuid
    }

    public inline fun CharSequence?.isNullOrBlank(): Boolean = this == null || this.length == 0
    /**
     * 获取 Android ID
     * @param context 上下文
     * @return androidId
     */
    private fun getAndroidId(context: Context): String =
            Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)

    /**
     * 获取序列号
     * @return 序列号
     */
    private fun getSerialNumber(): String = Build.SERIAL

    /**
     * 伪 IMEI
     * @return 伪 IMEI
     */
    private fun getUniquePsuedoId(): String? =
            "35" +
                    Build.BOARD.length % 10 +
                    Build.BRAND.length % 10 +
                    Build.CPU_ABI.length % 10 +
                    Build.DEVICE.length % 10 +
                    Build.DISPLAY.length % 10 +
                    Build.HOST.length % 10 +
                    Build.ID.length % 10 +
                    Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10 +
                    Build.TAGS.length % 10 +
                    Build.TYPE.length % 10 +
                    Build.USER.length % 10 //13 digits

}