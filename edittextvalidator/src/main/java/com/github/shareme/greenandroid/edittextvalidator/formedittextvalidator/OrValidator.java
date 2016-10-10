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
 * The or validator checks if one of passed validators is returning true.<br/>
 * Note: the message that will be shown is the one passed to the Constructor
 * @author Andrea B.
 *
 */
public class OrValidator extends MultiValidator {
  
  public OrValidator( String message, Validator ... validators) {
    super(message, validators);
  }
  
  public boolean isValid(EditText et) {
    //TODO: What if we've no validators ?
    for (Validator v: validators) {
      if (v.isValid(et)) {
        return true; // Remember :) We're acting like an || operator.
      }
    }
    return false;
  }
  
}

