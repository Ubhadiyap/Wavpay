package com.hbyundu.shop.vendor.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by longe on 2015/11/20.
 */
public class DeviceUuidUtils {

    protected static final String PREFS_FILE = "device_uuid.xml";
    protected static final String PREFS_DEVICE_UUID = "device_uuid";

    protected volatile static UUID uuid;

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static UUID getUUID(Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidUtils.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_UUID, null);
                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = UUID.fromString(id);

                    } else {
                        final UUID randomUUID;
                        final String androidID, deviceID, simSerialID;
                        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        randomUUID = UUID.randomUUID();

                        androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            deviceID = null;
                            simSerialID = null;
                        } else {
                            deviceID = tm.getDeviceId();
                            simSerialID = tm.getSimSerialNumber();
                        }

                        //two cases android 2.2 bug and emulator
                        if ("9774d56d682e549c".equals(androidID) || androidID == null) {
                            uuid = randomUUID;
                            prefs.edit().putString(PREFS_DEVICE_UUID, uuid.toString()).commit();
                        } else {
                            if (deviceID == null) {
                                uuid = randomUUID;
                                prefs.edit().putString(PREFS_DEVICE_UUID, uuid.toString()).commit();
                            } else {
                                // non phone device and if debugging with actual phone with no simcard
                                if (simSerialID == null) {
                                    uuid = randomUUID;
                                    prefs.edit().putString(PREFS_DEVICE_UUID, uuid.toString()).commit();
                                } else {
                                    uuid = new UUID(androidID.hashCode(), ((long) deviceID.hashCode() << 32) | simSerialID.hashCode());
                                    prefs.edit().putString(PREFS_DEVICE_UUID, uuid.toString()).commit();
                                }
                            }
                        }
                    }
                }
            }
        }

        return uuid;
    }
}
