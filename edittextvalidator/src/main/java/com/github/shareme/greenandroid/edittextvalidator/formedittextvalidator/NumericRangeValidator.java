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

import android.widget.EditText;

/**
 * A validator that returns true only if the input field contains only numbers
 * and the number is within the given range.
 * 
 * @author Said Tahsin Dane <tasomaniac@gmail.com>
 *
 */
@SuppressWarnings("unused")
public class NumericRangeValidator extends Validator{
	
	private int min, max;
	
	public NumericRangeValidator(String _customErrorMessage, int min, int max) {
		super(_customErrorMessage);
		this.min = min;
		this.max = max;
	}

	public boolean isValid(EditText et) {
		try {
			int value = Integer.parseInt(et.getText().toString());
			return value >= min && value <= max;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}
