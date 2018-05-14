package com.projects.leophilo.eltools.view.anim;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.app.Elements;

public class AnimHelper {

    private AnimHelper() {
    }

    private static class InstanceHolder {
        private static final AnimHelper INSTANCE = new AnimHelper();
    }

    public static AnimHelper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void animMainItemStateChange(
            @NonNull final Context context, @NonNull final View target
            , final boolean expectToDelete, final int itemType) {
        final ImageView logoImg = target.findViewById(R.id.logo_img);
        final ImageButton deleteBtn = target.findViewById(R.id.button_delete);
        final TextView logoTxt = target.findViewById(R.id.logo_txt);
        final Animator animator = AnimatorInflater.loadAnimator(context, R.animator.flip_out);
        animator.setTarget(target);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (expectToDelete) {
                    logoImg.setBackgroundResource(R.drawable.shape_oval);
                    logoTxt.setVisibility(View.GONE);
                } else {
                    switch (itemType) {
                        case Elements.Type.Air:
                            logoImg.setBackgroundResource(R.drawable.bg_air);
                            break;
                        case Elements.Type.NobleGas:
                            logoImg.setBackgroundResource(R.drawable.bg_noble_gas);
                            break;
                        case Elements.Type.CombustibleGas:
                            logoImg.setBackgroundResource(R.drawable.bg_combustible_gas);
                            break;
                    }
                    deleteBtn.setVisibility(View.GONE);
                }
                Animator animator1 = AnimatorInflater.loadAnimator(context, R.animator.flip_in);
                animator1.setTarget(target);
                animator1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animator animator2 = AnimatorInflater.loadAnimator(context, R.animator.scale_in);
                        if (expectToDelete) {
                            deleteBtn.setScaleX(0);
                            deleteBtn.setScaleY(0);
                            deleteBtn.setVisibility(View.VISIBLE);
                            animator2.setTarget(deleteBtn);
                        } else {
                            logoTxt.setScaleX(0);
                            logoTxt.setScaleY(0);
                            logoTxt.setVisibility(View.VISIBLE);
                            animator2.setTarget(logoTxt);
                        }
                        animator2.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator1.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
