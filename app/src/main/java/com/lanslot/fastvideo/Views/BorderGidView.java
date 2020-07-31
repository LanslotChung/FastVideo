package com.lanslot.fastvideo.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.lanslot.fastvideo.R;

public class BorderGidView extends GridView {
    public BorderGidView(Context context) {
        super(context,null);
    }

    public BorderGidView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public BorderGidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BorderGidView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int column = getNumColumns(); //获取列数
        int row = (int) Math.ceil(getChildCount() / (float) column); //获取行数
        int childCount = getChildCount();
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(getResources().getColor(R.color.whitesmoke,null));
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if ((i + 1) > column * (row - 1)) {
                if ((i + 1) == column * row) {
                    continue;
                } else {
                    canvas.drawLine(child.getRight(), child.getTop(), child.getRight()
                            , child.getBottom(), linePaint);
                }
            } else if ((i + 1) % column == 0) {
                canvas.drawLine(child.getLeft(), child.getBottom(), child.getRight()
                        , child.getBottom(), linePaint);
            } else {
                canvas.drawLine(child.getLeft(), child.getBottom(), child.getRight()
                        , child.getBottom(), linePaint);
                canvas.drawLine(child.getRight(), child.getTop(), child.getRight()
                        , child.getBottom(), linePaint);
            }
        }
    }
}
