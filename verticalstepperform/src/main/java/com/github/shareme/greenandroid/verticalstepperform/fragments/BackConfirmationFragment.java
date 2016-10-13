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
package com.github.shareme.greenandroid.verticalstepperform.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.github.shareme.greenandroid.verticalstepperform.R;

/**
 * This fragment can be used to display a confirmation dialog when the user tries to go back
 */
@SuppressWarnings("unused")
public class BackConfirmationFragment extends DialogFragment {

    private DialogInterface.OnClickListener onConfirmBack;
    private DialogInterface.OnClickListener onNotConfirmBack;

    public void setOnConfirmBack(DialogInterface.OnClickListener onConfirmBack) {
        this.onConfirmBack = onConfirmBack;
    }

    public void setOnNotConfirmBack(DialogInterface.OnClickListener onNotConfirmBack) {
        this.onNotConfirmBack = onNotConfirmBack;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.vertical_form_stepper_form_discard_question)
                .setMessage(R.string.vertical_form_stepper_form_info_will_be_lost)
                .setNegativeButton(R.string.vertical_form_stepper_form_discard_cancel,
                        onConfirmBack)
                .setPositiveButton(R.string.vertical_form_stepper_form_discard,
                        onNotConfirmBack);
        return builder.create();
    }
}