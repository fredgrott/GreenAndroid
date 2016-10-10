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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for a multivalidator.
 * @see AndValidator
 * @see OrValidator
 * @author Andrea Baccega <me@andreabaccega.com>
 *
 */
public abstract class MultiValidator extends Validator {
  protected final List<Validator> validators;
  public MultiValidator(String message, Validator ...validators) {
    super(message);
    if (validators == null) throw new NullPointerException("validators is null");
    this.validators = new ArrayList<Validator>(Arrays.asList(validators));
  }
  public MultiValidator(String message) {
    super(message);
    this.validators = new ArrayList<Validator>();
  }
  
  public void enqueue(Validator newValidator) {
    validators.add(newValidator);
  }
  

}
