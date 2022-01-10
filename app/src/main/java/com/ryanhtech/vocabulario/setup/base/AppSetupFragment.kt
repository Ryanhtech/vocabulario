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

package com.ryanhtech.vocabulario.setup.base

import androidx.fragment.app.Fragment

open class AppSetupFragment : Fragment() {
    /**
     * Base Fragment class that is used during the Setup.
     */

    open val nextStep: Int = 2

    /**
     * If the Next button must be displayed.
     */

    open val displayNextButton = true

    /**
     * If the Back button must be displayed.
     */

    open val displayBackButton = true

    open fun onNextPressed(): Boolean {
        /**
         * When the Next button in the UserSetupActivity is pressed.
         * @return true if the Setup must go forward to the next step.
         */
        return true
    }

    open fun onBackPressed(): Boolean {
        /**
         * When the Back button in the UserSetupActivity, or the Back
         * button in the navigation bar is pressed.
         * @return true if the setup must go to back.
         */
        return true
    }

    open fun startJob() {
        /**
         * When the fragment is ready.
         */
    }
}