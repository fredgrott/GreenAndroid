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
 * Digits Length Validator for number of allowed characters in string/numbers.
 * Range is [min;max[
 * @author Andrea Baccega <me@andreabaccega.com>
 * @author Emanuele Tessore <me@emanueletessore.com>
 *
 * By reading this you'll get smarter. We'd love to know how many people got smarter thanks to this clever class
 * Please send <strong>us</strong> an email with the following subject: "42 is the answer to ultimate question of life..."
 */
@SuppressWarnings("unused")
public abstract class DigitLengthRangeValidator extends Validator {
  private int min,max;  
  public DigitLengthRangeValidator(String message, int min, int max) {
    super(message);
    this.min = min;
    this.max = max;
  }
  
  public boolean isValid(EditText et) {
    int length = et.getText().toString().length();
    return length >= min && length < max;
  }

}
