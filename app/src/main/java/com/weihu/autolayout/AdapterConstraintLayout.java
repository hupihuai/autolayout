package com.weihu.autolayout;

import android.content.Context;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by hupihuai on 2017/8/9.
 */

public class AdapterConstraintLayout extends ConstraintLayout {
    private static final int DESIGN_WIDTH = 1080;
    private static final int DESIGN_HEIGHT = 1920;
    private static final float DESIGN_SCALE = 3f;

    private float mScale;
    private float mFontScale;
    private float mScaleX = 0;
    private float mScaleY = 0;

    public AdapterConstraintLayout(Context context) {
        this(context, null);
    }

    public AdapterConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdapterConstraintLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Point point = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        //横屏
        if (point.x > point.y) {
            mScaleX = (point.x * 1.0f / DESIGN_HEIGHT);
            mScaleY = (point.y * 1.0f / DESIGN_WIDTH);
        } else {//竖屏
            mScaleX = (point.x * 1.0f / DESIGN_WIDTH);
            mScaleY = (point.y * 1.0f / DESIGN_HEIGHT);
        }
        float density = DESIGN_SCALE / getResources().getDisplayMetrics().density;
        float scaleDensity = DESIGN_SCALE / getResources().getDisplayMetrics().scaledDensity;
        float minScale = Math.min(mScaleX, mScaleY);
        mScale = minScale * density;
        mScaleX *= density;
        mScaleY *= density;
        mFontScale = minScale * scaleDensity;
        transformSize(this, getLayoutParams());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        transformSize(this, params);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        if (!isInEditMode()) {
            adapterChild(child, child.getLayoutParams());
        }
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        if (!isInEditMode()) {
            adapterChild(child, child.getLayoutParams());
        }
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        if (!isInEditMode()) {
            adapterChild(child, child.getLayoutParams());
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (!isInEditMode()) {
            adapterChild(child, params);
        }
        super.addView(child, params);
    }

    @Override
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        view.setTag(R.id.has_adapter_size, null);
    }

    private void adapterChild(View view, ViewGroup.LayoutParams params) {
        transformSize(view, params);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = viewGroup.getChildAt(i);
                adapterChild(childView, childView.getLayoutParams());
            }
        }
    }

    private void transformSize(View view, ViewGroup.LayoutParams params) {
        Object tag = view.getTag(R.id.has_adapter_size);
        if (tag != null || params == null) {//处理过就不处理了
            return;
        }
        view.setTag(R.id.has_adapter_size, true);
        adapterSize(params);
        adapterTextSize(view);
        adapterMargin(params);
        adapterPadding(view);
        adapterMinimumSize(view);
    }

    private void adapterMinimumSize(View view) {
        if (view.getMinimumWidth() > 0) {
            view.setMinimumWidth((int) (view.getMinimumWidth() * mScaleX));
        }
        if (view.getMinimumHeight() > 0) {
            view.setMinimumHeight((int) (view.getMinimumHeight() * mScaleY));
        }

    }

    private void adapterPadding(View view) {
        //padding
        int paddingLeft = (int) (view.getPaddingLeft() * mScaleX);
        int paddingTop = (int) (view.getPaddingTop() * mScaleY);
        int paddingRight = (int) (view.getPaddingRight() * mScaleX);
        int paddingBottom = (int) (view.getPaddingBottom() * mScaleY);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    private void adapterMargin(ViewGroup.LayoutParams params) {
        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) params;
            //margin
            marginLayoutParams.leftMargin *= mScaleX;
            marginLayoutParams.topMargin *= mScaleY;
            marginLayoutParams.rightMargin *= mScaleX;
            marginLayoutParams.bottomMargin *= mScaleY;
        }
    }

    private void adapterTextSize(View view) {
        //font size
        if (view instanceof TextView) {
            final float textSize = ((TextView) view).getTextSize();
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * mFontScale);
        } else if (view instanceof Button) {
            final float textSize = ((Button) view).getTextSize();
            ((Button) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * mFontScale);
        } else if (view instanceof EditText) {
            final float textSize = ((EditText) view).getTextSize();
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * mFontScale);
        }
    }

    private void adapterSize(ViewGroup.LayoutParams params) {
        if (params.width > 0 && params.height > 0) {//按比列
            params.width *= mScale;
            params.height *= mScale;
        } else {
            //width
            if (params.width > 0) {
                params.width *= mScaleX;
            }
            //height
            if (params.height > 0) {
                params.height *= mScaleY;
            }
        }
    }

}