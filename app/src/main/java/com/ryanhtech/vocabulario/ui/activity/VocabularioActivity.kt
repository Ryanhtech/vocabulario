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

package com.ryanhtech.vocabulario.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ryanhtech.vocabulario.admin.internal.AdminPasswordManager
import com.ryanhtech.vocabulario.admin.internal.AdminPermissions
import com.ryanhtech.vocabulario.admin.ui.AdminPassActivity
import com.ryanhtech.vocabulario.internal.framework.VbUtils
import com.ryanhtech.vocabulario.internal.legal.licensemgr.RootCheck
import com.ryanhtech.vocabulario.internal.reset.LocalConfigurationRequest
import com.ryanhtech.vocabulario.ui.popup.PopupFragment
import com.ryanhtech.vocabulario.ui.popup.PopupFragmentExecutor
import com.ryanhtech.vocabulario.ui.startup.SplashScreenActivity
import com.ryanhtech.vocabulario.utils.DataManager

/**
 * This provides the base Activity class for all Vocabulario
 * activities. It helps defining custom properties for Activities
 * in Vocabulario.
 *
 * @since initial version
 * @author Ryanhtech Labs
 */
open class VocabularioActivity : AppCompatActivity() {
    /**
     * If you should allow this Activity starting even if
     * the app is in Forgot password mode, set this to `false`.
     */
    open val applyEmergencyBlock: Boolean = true

    /**
     * If it's better to ask an administrator password to start
     * this app, set this to `true`.
     */
    open val isProtectedActivity: Boolean = false

    /**
     * If this Activity must start even if the app's maintenance is
     * discontinued, set this to `false`.
     */
    open val applyEndOfSupport: Boolean = true

    /**
     * If you should not prevent this Activity from starting if the user
     * hasn't accepted the software license, set this to `false`.
     */
    open val applyLicenseApprovalProtection = true

    /**
     * If set to `true`, this tells to the Android system that the
     * Activity must be secured (prevents screenshots and overlays).
     */
    open val isSecuredActivity: Boolean = false

    /**
     * If set to `true`, the Activity will close if the app isn't configured
     * properly after a local reset.
     */
    open val applyLocalResetConfigurationRequest = true

    /**
     * The [VbUtils] instance for this Activity.
     */
    private lateinit var mVbUtilsInst: VbUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        val registerResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                // Finish the Activity
                finish()
            }
        }

        super.onCreate(savedInstanceState)

        // Check the root status only if we should apply license approval protection.
        if (applyLicenseApprovalProtection && checkRoot()) {
            // Finish and return.
            finish()
            return
        }

        if (applyEmergencyBlock) {
            AdminPasswordManager.checkIfEmergencyModeEnabledAndLock(this)
        }

        if (isProtectedActivity && !AdminPermissions.adminUnlocked
            && DataManager().isManagedByOrganization(this)
            && DataManager.checkIfAppConfigured(this)) {

            registerResult.launch(
                Intent(
                    this,
                    AdminPassActivity::class.java
                )
            )
        }

        if (isSecuredActivity) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        initializeVbUtils()
    }

    override fun onResume() {
        super.onResume()

        if (applyLocalResetConfigurationRequest
            && LocalConfigurationRequest.isReConfigRequested(this)) {
            // Start the splash screen to re-configure the app
            startActivity(Intent(this, SplashScreenActivity::class.java))
            finish()
        }
    }

    open fun displayPopupFragment(fragmentInst: PopupFragment, activityContext: Activity) {
        // Call the PopupFragmentExecutor API
        PopupFragmentExecutor.displayPopupFragment(fragmentInst, activityContext)
    }

    /**
     * This returns the current [VbUtils] instance in this Activity.
     */
    fun getVbUtils(): VbUtils = mVbUtilsInst

    private fun initializeVbUtils() {
        // Instantiate a new instance of VbUtils
        val lVbUtilsInst = VbUtils(this)

        // Set this as the default utils instance
        mVbUtilsInst = lVbUtilsInst
    }

    /**
     * This method checks if the device is rooted. If it is, it shows a warning Toast and returns
     * `true`.
     * @return `true` if the device is rooted, `false` if not.
     */
    private fun checkRoot(): Boolean {
        // Get a RootCheck instance (that acts like a bridge between us and RootBeer)
        val lVRootCheck = RootCheck(this)

        // Scan for root
        val lIsRooted = lVRootCheck.isDeviceRooted()

        // If we are rooted, show the toast.
        if (lIsRooted) {
            RootCheck.showStandaloneRootedToast(this)
        }

        // Return the result
        return lIsRooted
    }
}