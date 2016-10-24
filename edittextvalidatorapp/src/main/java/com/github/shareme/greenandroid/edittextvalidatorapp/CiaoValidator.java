package com.github.shareme.greenandroid.edittextvalidatorapp;

import android.text.TextUtils;
import android.widget.EditText;

import com.github.shareme.greenandroid.edittextvalidator.formedittextvalidator.Validator;


public class CiaoValidator
    extends Validator
{

	public CiaoValidator( String customErrorMessage )
	{
		super( customErrorMessage );

	}

	@Override
	public boolean isValid( EditText et )
	{
		return TextUtils.equals( et.getText(), "ciao" );
	}

}