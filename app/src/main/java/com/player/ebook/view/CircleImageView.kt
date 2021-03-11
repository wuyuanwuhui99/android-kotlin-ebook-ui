package com.player.ebook.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Xfermode
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageView

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mSize: Int = 0
    private var mPaint: Paint? = null
    private var mPorterDuffXfermode: Xfermode? = null

    init {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.isDither = true
        mPaint!!.isAntiAlias = true

        mPorterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        mSize = Math.min(width, height)  //取宽高的最小值
        setMeasuredDimension(mSize, mSize)    //设置CircleImageView为等宽高
    }

    override fun onDraw(canvas: Canvas) {
        //获取sourceBitmap，即通过xml或者java设置进来的图片
        val drawable = drawable ?: return

        val sourceBitmap = (getDrawable() as BitmapDrawable).bitmap
        if (sourceBitmap != null) {
            //对图片进行缩放，以适应控件的大小
            val bitmap = resizeBitmap(sourceBitmap, width, height)
            drawCircleBitmapByXfermode(canvas, bitmap)    //(1)利用PorterDuffXfermode实现
            //drawCircleBitmapByShader(canvas,bitmap);    //(2)利用BitmapShader实现
        }
    }

    private fun resizeBitmap(sourceBitmap: Bitmap, dstWidth: Int, dstHeight: Int): Bitmap {
        val width = sourceBitmap.width
        val height = sourceBitmap.height

        val widthScale = dstWidth.toFloat() / width
        val heightScale = dstHeight.toFloat() / height

        //取最大缩放比
        val scale = Math.max(widthScale, heightScale)
        val matrix = Matrix()
        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, matrix, true)
    }

    private fun drawCircleBitmapByXfermode(canvas: Canvas, bitmap: Bitmap) {
        val sc =
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        //绘制dst层
        canvas.drawCircle(
            (mSize / 2).toFloat(),
            (mSize / 2).toFloat(),
            (mSize / 2).toFloat(),
            mPaint!!
        )
        //设置图层混合模式为SRC_IN
        mPaint!!.xfermode = mPorterDuffXfermode
        //绘制src层
        canvas.drawBitmap(bitmap, 0f, 0f, mPaint)
        canvas.restoreToCount(sc)
    }
}
