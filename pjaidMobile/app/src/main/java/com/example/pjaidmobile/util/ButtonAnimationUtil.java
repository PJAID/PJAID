package com.example.pjaidmobile.util;

import android.view.MotionEvent;
import android.view.View;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class ButtonAnimationUtil {

    public static void applySpringAnimation(View view) {
        SpringAnimation scaleXAnim = new SpringAnimation(view, SpringAnimation.SCALE_X, 1f);
        SpringAnimation scaleYAnim = new SpringAnimation(view, SpringAnimation.SCALE_Y, 1f);

        SpringForce spring = new SpringForce(1f);
        spring.setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        spring.setStiffness(SpringForce.STIFFNESS_LOW);

        scaleXAnim.setSpring(spring);
        scaleYAnim.setSpring(spring);

        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setScaleX(0.9f);
                v.setScaleY(0.9f);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                scaleXAnim.start();
                scaleYAnim.start();
            }
            return false;
        });
    }
}
