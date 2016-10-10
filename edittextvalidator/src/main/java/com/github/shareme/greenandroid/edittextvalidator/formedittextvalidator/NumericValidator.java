/*
 The MIT License (MIT)
Copyright (c) 2012 Andrea Baccega <me@andreabaccega.com>
Modifications Copyright(C) 2016 Fred Grott(GrottWorkShop)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
package com.github.shareme.greenandroid.edittextvalidator.formedittextvalidator;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * A validator that returns true only if the input field contains only numbers.
 * @author Andrea Baccega <me@andreabaccega.com>
 *
 */
@SuppressWarnings("unused")
public class NumericValidator extends Validator{
	public NumericValidator(String _customErrorMessage) {
		super(_customErrorMessage);
	}

	public boolean isValid(EditText et) {
		return TextUtils.isDigitsOnly(et.getText());
	}
}
