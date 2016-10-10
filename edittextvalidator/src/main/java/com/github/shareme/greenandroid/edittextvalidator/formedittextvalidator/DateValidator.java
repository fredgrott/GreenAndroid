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

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class DateValidator extends Validator {
	private String[] formats;
	public DateValidator(String _customErrorMessage, String _format) {
		super(_customErrorMessage);
		formats = TextUtils.isEmpty(_format) ? new String[]{"DefaultDate","DefaultTime","DefaultDateTime"} : _format.split(";") ;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public boolean isValid(EditText et) {
		if(TextUtils.isEmpty(et.getText()))
			return true;
		String value = et.getText().toString();
		for(String _format : formats){
			DateFormat format;
			if("DefaultDate".equalsIgnoreCase(_format)){
				format = SimpleDateFormat.getDateInstance();
			} else if("DefaultTime".equalsIgnoreCase(_format)){
				format = SimpleDateFormat.getTimeInstance();
			} else if("DefaultDateTime".equalsIgnoreCase(_format)){
				format = SimpleDateFormat.getDateTimeInstance();
			} else {
				format = new SimpleDateFormat(_format);
			}
			Date date = null;
			try {
				date = format.parse(value);
			} catch (ParseException e) {
				return false;
			}
			if(date != null){
				return true;
			}
		}
		return false;
	}

}
