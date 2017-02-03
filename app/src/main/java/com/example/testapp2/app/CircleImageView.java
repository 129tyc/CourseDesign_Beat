package com.example.testapp2.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Code on 2016/12/11 0011.
 * <p>
 * 圆形ImageView
 */
public class CircleImageView extends MaskedImage {
    /**
     * 构造函数
     * @param paramContext 上下文参数
     */
    public CircleImageView(Context paramContext) {
        super(paramContext);
    }

    /**
     * 构造函数
     * @param paramContext 上下文参数
     * @param paramAttributeSet 属性集参数
     */
    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    /**
     * 构造函数
     * @param paramContext 上下文参数
     * @param paramAttributeSet 属性集参数
     * @param paramInt Int参数
     */
    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    /**
     * 裁切加载的图片
     * @return 裁切后的Bitmap
     */
    public Bitmap createMask() {
//        获取ImageView长宽
        int i = getWidth();
        int j = getHeight();
//        设置Bitmap属性
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
//        实例化Canvas对象
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
//        获得长宽
        float f1 = getWidth();
        float f2 = getHeight();
        RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
//        根据大小圆形裁切
        localCanvas.drawOval(localRectF, localPaint);
        return localBitmap;
    }
}
