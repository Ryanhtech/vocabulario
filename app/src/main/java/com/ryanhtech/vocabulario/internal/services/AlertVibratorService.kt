/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ryanhtech.vocabulario.internal.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.internal.notifications.Notifications

class AlertVibratorService : Service() {
    companion object {
        var vibTimes = 0
        const val vibTarget = 15
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the vibrator and stop the service once it's done
        Thread {
            Notifications.notifyVibratorAlert(this)
            AdminPasswordManager.startAlertVibration(applicationContext)

            with(NotificationManagerCompat.from(this)) {
                cancel(Notifications.VIBRATOR_ALERT_NOTIF)
            }

            stopSelf()
        }.start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}