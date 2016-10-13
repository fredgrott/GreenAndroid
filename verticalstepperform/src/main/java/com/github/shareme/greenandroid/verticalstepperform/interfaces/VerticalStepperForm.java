/*
 Copyright 2016 Julio Ernesto RodrÃ­guez CabaÃ±as
 Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package com.github.shareme.greenandroid.verticalstepperform.interfaces;

import android.view.View;

public interface VerticalStepperForm {

    /**
     * The content of the layout of the corresponding step must be generated here. The system will
     * automatically call this method for every step
     * @param stepNumber the number of the step
     * @return The view that will be automatically added as the content of the step
     */
    View createStepContentView(int stepNumber);

    /**
     * This method will be called every time a certain step is open
     * @param stepNumber the number of the step
     */
    void onStepOpening(int stepNumber);

    /**
     * This method will be called when the user press the confirmation button
     */
    void sendData();

}
