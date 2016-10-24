package com.github.shareme.greenandroid.edittextvalidatorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.shareme.greenandroid.edittextvalidator.formedittextvalidator.CreditCardValidator;
import com.github.shareme.greenandroid.edittextvalidator.formedittextvalidator.EmailValidator;
import com.github.shareme.greenandroid.edittextvalidator.formedittextvalidator.OrValidator;
import com.github.shareme.greenandroid.edittextvalidator.widget.FormEditText;


public class EmailOrCreditCard extends AppCompatActivity {
  private FrameLayout flContainer;
  private TextView tvExplanation;
  private TextView tvTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_examplegeneric);

    flContainer     = (FrameLayout) findViewById(R.id.fl);
    tvExplanation   = (TextView)    findViewById(R.id.tv_explanation);
    tvTitle         = (TextView)    findViewById(R.id.tv_title);
    
    flContainer.addView(LayoutInflater.from(this).inflate(R.layout.example_email_or_creditcard, flContainer, false));
    tvExplanation.setText(R.string.explanation_emailorcredit);
    tvTitle.setText(R.string.emailorcredit_title);
    
    //Interesting stuff starts here
    
    FormEditText fdt = (FormEditText) findViewById(R.id.et);
    fdt.addValidator(
        new OrValidator(
            "This is neither a creditcard or an email", 
            new CreditCardValidator(null), // we specify null as the message string cause the Or validator will use his own message
            new EmailValidator(null) // same here for null
            )
        );
  }
  
  public void onClickValidate(View v) {
    FormEditText fdt = (FormEditText) findViewById(R.id.et);
    if (fdt.testValidity()) {
      Toast.makeText(this, ":)", Toast.LENGTH_LONG).show();
    } 
  }
}
