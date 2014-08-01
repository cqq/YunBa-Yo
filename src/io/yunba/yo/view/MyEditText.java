package io.yunba.yo.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MyEditText extends EditText
{
  public MyEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  
  private static final String TAG = "BigGrid";

  public MyEditText(Context context) {
      super(context);
      setFocusableInTouchMode(true); // allows the keyboard to pop up on
                                     // touch down

      setOnKeyListener(new OnKeyListener() {
    	  @Override
          public boolean onKey(View v, int keyCode, KeyEvent event) {
              Log.d(TAG, "onKeyListener");
              if (event.getAction() == KeyEvent.ACTION_DOWN) {
                  // Perform action on key press
                  Log.d(TAG, "ACTION_DOWN");
                  return true;
              }
              return false;
          }
      });
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
      super.onTouchEvent(event);
      Log.d(TAG, "onTOUCH");
      if (event.getAction() == MotionEvent.ACTION_UP) {

          // show the keyboard so we can enter text
          InputMethodManager imm = (InputMethodManager) getContext()
                  .getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
      }
      return true;
  }

  @Override
  public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
      Log.d(TAG, "onCreateInputConnection");

      BaseInputConnection fic = new BaseInputConnection(this, false);
      outAttrs.actionLabel = null;
      outAttrs.inputType = InputType.TYPE_NULL;
      outAttrs.imeOptions = EditorInfo.IME_ACTION_NEXT;
      return fic;
  }

  @Override
  public boolean onCheckIsTextEditor() {
      Log.d(TAG, "onCheckIsTextEditor");
      return true;
  }

}