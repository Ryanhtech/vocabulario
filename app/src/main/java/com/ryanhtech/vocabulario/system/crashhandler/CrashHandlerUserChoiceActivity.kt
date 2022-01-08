/*
 * Copyright 2021-2022 Ryanhtech Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ryanhtech.vocabulario.system.crashhandler

import android.content.Intent
import android.os.Bundle
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.ui.settings.FatalErrorActivity
import kotlinx.android.synthetic.main.activity_crash_handler_user_choice2.*

class CrashHandlerUserChoiceActivity : VocabularioActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_handler_user_choice2)

        closeCrashHandlerButton.setOnClickListener {
            finish()
        }

        repairAppLaunchButton.setOnClickListener {
            startActivity(Intent(this, FatalErrorActivity::class.java))
            finish()
        }
    }
}