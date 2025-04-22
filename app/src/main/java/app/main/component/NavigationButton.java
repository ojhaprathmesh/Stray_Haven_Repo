package app.main.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import app.main.R;

public class NavigationButton extends FrameLayout {

    private OnNavClickListener listener;
    public static boolean isClosed = true;

    public NavigationButton(Context context) {
        super(context);
        init(context);
    }

    public NavigationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavigationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnNavClickListener(OnNavClickListener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.comp_nav_button, this, true);

        Interpolator easeInBack = input -> {
            float c1 = 1.70158f;
            return input * input * ((c1 + 1) * input - c1);
        };

        this.setOnClickListener(v -> {
            if (isClosed) {
                this.animate()
                        .rotationBy(90f)
                        .setDuration(750)
                        .setInterpolator(easeInBack)
                        .withEndAction(() -> {
                            if (listener != null) {
                                listener.onNavClick();
                            }
                        })
                        .start();
            } else {
                this.animate()
                        .rotationBy(-90f)
                        .setDuration(750)
                        .setInterpolator(easeInBack)
                        .withEndAction(() -> {
                            if (listener != null) {
                                listener.onNavClick();
                            }
                        })
                        .start();
            }
            isClosed = !isClosed;
        });
    }

    public interface OnNavClickListener {
        void onNavClick();
    }
}
