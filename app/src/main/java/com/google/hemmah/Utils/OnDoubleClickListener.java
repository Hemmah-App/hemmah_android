package com.google.hemmah.Utils;

import android.view.MotionEvent;
import android.view.View;

public class OnDoubleClickListener implements View.OnTouchListener  {
        private static final long DOUBLE_CLICK_TIME_DELTA = 300;
        long lastClickTime = 0;
        private final Runnable mOnSingleClick;
        private final Runnable mOnDoubleClick;

        public OnDoubleClickListener(Runnable onSingleClick, Runnable onDoubleClick) {
            mOnSingleClick = onSingleClick;
            mOnDoubleClick = onDoubleClick;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    long clickTime = System.currentTimeMillis();
                    if ((clickTime - lastClickTime) < DOUBLE_CLICK_TIME_DELTA) {
                        if (mOnDoubleClick != null) {
                            mOnDoubleClick.run();
                        }
                    } else {
                        if (mOnSingleClick != null) {
                            mOnSingleClick.run();
                        }
                    }
                    lastClickTime = clickTime;
                    break;
            }
            return true;
        }
}
