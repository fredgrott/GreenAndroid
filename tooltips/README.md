ToolTips
========

Soft-forking of Tomet Goldstein's ToolTips project.

# Usage

Create a ToolTipsManager object
```java
ToolTipsManager mToolTipsManager;
mToolTipsManager = new ToolTipsManager();
```
  
Use the ToolTip Builder to construct your tip
```java
ToolTip.Builder builder = new ToolTip.Builder(this, mTextView, mRootLayout, "Tip message", ToolTip.POSITION_ABOVE);
```
'mTextView' here is the view which near it the tip will be shown and 'mRootLayout' is the layout where the tip view will be added to.
The root layout must be of RelativeLayout, FrameLayout or similar. LinearLayout won't work but you can always wrap your LinearLayout
with another layout. Prefer to pass in a layout which is higher in the xml tree as this will give the
tip view more visible space.
 
OPTIONAL: Customize your tip with background color, text color, alignment, text gravity and more. 
```java
builder.setAlign(ToolTip.ALIGN_LEFT);
builder.setBackgroundColor(getResources().getColor(R.color.colorOrange));
builder.setTextColor(getResources().getColor(R.color.colorBlack));
builder.setGravity(ToolTip.GRAVITY_RIGHT);
```

Use ToolTipManger to show the tip

IMPORTANT: This must be called after the layout has been drawn
You can override the 'onWindowFocusChanged()' of an Activity and show there, Start a delayed runnable from onStart() , React to user action or any other method that works for you
```java
mToolTipsManager.show(builder.build());
```

Each tip is dismissable by clicking on it, if you want to dismiss a tip from code there are a few options, The most simple way is to do the following
```java
mToolTipsManager.findAndDismiss(mTextView);
```
Where 'mTextView' is the same view we asked to position a tip near it

If you want to react when tip has been dismissed, Implement ToolTipsManager.TipListener interface and use appropriate ToolTipsManager constructor
```java
public class MainActivity extends AppCompatActivity implements ToolTipsManager.TipListener
.
.
@Override
protected void onCreate(Bundle savedInstanceState) {
    mToolTipsManager = new ToolTipsManager(this);
}
.
.
@Override
public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
    Log.d(TAG, "tip near anchor view " + anchorViewId + " dismissed");

    if (anchorViewId == R.id.text_view) {
        // Do something when a tip near view with id "R.id.text_view" has been dismissed
    }
}
```

# Credits

Tooltips was originally created by Tomet Goldstein:

[Tooltips]()

