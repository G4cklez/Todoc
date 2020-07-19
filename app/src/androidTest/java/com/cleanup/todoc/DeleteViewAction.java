package com.cleanup.todoc;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

public class DeleteViewAction implements ViewAction {

    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Handle click";
    }

    @Override
    public void perform(UiController uiController, View view) {
        if(view!=null) {
            View button = view.findViewById(R.id.img_delete);
            if (button != null) {
                button.performClick();
            }
        }
    }
}
