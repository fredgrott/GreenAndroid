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
package com.github.shareme.greenandroid.edittextvalidator.widget;

import android.content.Context;
import android.text.TextWatcher;

import com.github.shareme.greenandroid.edittextvalidator.formedittextvalidator.Validator;


/**
 * Interface for encapsulating validation of an EditText control
 */
@SuppressWarnings("unused")
public interface EditTextValidator {
	/**
	 * Add a validator to this FormEditText. The validator will be added in the
	 * queue of the current validators.
	 * 
	 * @param theValidator
	 * @throws IllegalArgumentException
	 *             if the validator is null
	 */
	void addValidator(Validator theValidator)
			throws IllegalArgumentException;

	/**
	 * This should be used with { #addTextChangedListener(TextWatcher)}. It
	 * fixes the non-hiding error popup behaviour.
	 */
	TextWatcher getTextWatcher();

	boolean isEmptyAllowed();

	/**
	 * Resets the {@link Validator}s
	 */
	void resetValidators(Context context);

	/**
	 * Calling *testValidity()* will cause the EditText to go through
	 * customValidators and call { #Validator.isValid(EditText)}
	 * Same as {@link #testValidity(boolean)} with first parameter true
	 * @return true if the validity passes false otherwise.
	 */
	boolean testValidity();

    /**
     * Calling *testValidity()* will cause the EditText to go through
     * customValidators and call { #Validator.isValid(EditText)}
     * @param showUIError determines if this call should show the UI error.
     * @return true if the validity passes false otherwise.
     */
    boolean testValidity(boolean showUIError);

    void showUIError();

	int TEST_REGEXP = 0;

	int TEST_NUMERIC = 1;

	int TEST_ALPHA = 2;

	int TEST_ALPHANUMERIC = 3;

	int TEST_EMAIL = 4;

	int TEST_CREDITCARD = 5;

	int TEST_PHONE = 6;

	int TEST_DOMAINNAME = 7;

	int TEST_IPADDRESS = 8;

	int TEST_WEBURL = 9;

	int TEST_NOCHECK = 10;

	int TEST_CUSTOM = 11;

	int TEST_PERSONNAME = 12;

	int TEST_PERSONFULLNAME = 13;

	int TEST_DATE = 14;
	
	int TEST_NUMERIC_RANGE = 15;

}
