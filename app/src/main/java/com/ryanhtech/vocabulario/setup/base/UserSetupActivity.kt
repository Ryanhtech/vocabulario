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

package com.ryanhtech.vocabulario.setup.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.ryanhtech.vocabulario.R
import com.ryanhtech.vocabulario.setup.config.UserSetupList
import com.ryanhtech.vocabulario.ui.activity.VocabularioActivity
import com.ryanhtech.vocabulario.utils.DataManager
import kotlinx.android.synthetic.main.activity_user_setup.*

class UserSetupActivity : VocabularioActivity() {
    private var currentFragment: AppSetupFragment? = null
    private var alreadySteppedIn = false
    override val isProtectedActivity: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setup)

        setCurrentFragment(
            UserSetupList.setupPages[intent.getIntExtra("step", UserSetupList.SETUP_RESET_APP)])

        setupClickListeners()
    }

    private fun setCurrentFragment(fragment: AppSetupFragment?) {
        currentFragment = fragment

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.setupContents, fragment!!)
            commit()
        }
    }

    private fun setupClickListeners() {
        /**
         * Sets up the onClickListeners.
         */

        setupNextButton.setOnClickListener {
            if (currentFragment!!.onNextPressed()) {

                setupNextButton.isEnabled = false
                setupProgressBar.isVisible = true

                Thread {
                    startActivity(
                        Intent(
                            this,
                            UserSetupActivity::class.java
                        ).putExtra("step", currentFragment!!.nextStep)
                    )

                    overridePendingTransition(0, 0)

                }.start()
            }
        }

        setupBackButton.setOnClickListener {
            if (currentFragment!!.onBackPressed()) {
                finish()
            }
        }

        setupBackButton.isEnabled = currentFragment!!.displayBackButton
        setupNextButton.isVisible = currentFragment!!.displayNextButton

        setupImage.setImageDrawable(AppCompatResources.getDrawable(
            this, currentFragment!!.fragmentIconResource))
        setupTitle.text = getString(currentFragment!!.fragmentTitleResource)

        // Check if the description is not null
        val lSetupDescription = currentFragment!!.fragmentDescriptionResource
        if (lSetupDescription == null) {
            setupDescription.isVisible = false
        } else {
            setupDescription.text = getString(lSetupDescription)
        }

        //currentFragment!!.startJob()
    }

    override fun onResume() {
        super.onResume()

        overridePendingTransition(0, 0)

        val isFragmentInNonFinishExceptions =
            UserSetupList.nonSetupPagesExceptions.contains(currentFragment!!::class.java)

        if (DataManager.checkIfAppConfigured(this)
            && !isFragmentInNonFinishExceptions) finish()

        setupContents.isVisible = true
        setupNextButton.isEnabled = true
        setupProgressBar.isVisible = false

        /**
         * Play the animation
         */
        if (!alreadySteppedIn) {
            /*val anim = AnimationUtils.loadAnimation(
                    this,
                    R.anim.zoom_in
            )

            anim.startOffset = 200

            fragmentContainerViewSetup.startAnimation(anim)*/
            val springAnimation = setupContents.let { view ->
                SpringAnimation(view, DynamicAnimation.TRANSLATION_X, 0f).apply {
                    spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    spring.stiffness = SpringForce.STIFFNESS_LOW
                }
            }

            setupContents.translationX = 400f

            springAnimation.start()
        } else {
            val springAnimation = setupContents.let { view ->
                SpringAnimation(view, DynamicAnimation.TRANSLATION_X, 0f).apply {
                    spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    spring.stiffness = SpringForce.STIFFNESS_LOW
                }
            }

            setupContents.translationX = -200f

            springAnimation.start()
        }

        alreadySteppedIn = true
    }

    override fun onBackPressed() {
        if (currentFragment!!.displayBackButton) {

            /**
             * Go back only if the back button is enabled.
             */

            if (currentFragment!!.onBackPressed()) {
                super.onBackPressed()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        overridePendingTransition(0, 0)
    }
}