package com.player.ebook.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.player.ebook.R


class RoundImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet) :
    MyImageView(context, attrs) {
    private val paint: Paint
    private val paintBorder: Paint
    private var mSrcBitmap: Bitmap? = null
    private val mSize: Int = 0
    /**
     * 圆角的弧度
     */
    private val mRadius: Float
    private val mIsCircle: Boolean

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
        mRadius = ta.getDimension(R.styleable.RoundImageView_radius, 0f)
        mIsCircle = ta.getBoolean(R.styleable.RoundImageView_circle, false)
        val srcResource = attrs!!.getAttributeResourceValue(
            "http://schemas.android.com/apk/res/android", "src", 0
        )
        if (srcResource != 0)
            mSrcBitmap = BitmapFactory.decodeResource(
                resources,
                srcResource
            )
        ta.recycle()
        paint = Paint()
        paint.isAntiAlias = true
        paintBorder = Paint()
        paintBorder.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }


    public override fun onDraw(canvas: Canvas) {
        val width = canvas.width - paddingLeft - paddingRight
        val height = canvas.height - paddingTop - paddingBottom
        val image = drawableToBitmap(drawable)
        if (image != null) {
            if (mIsCircle) {
                val reSizeImage = reSizeImageC(image, width, height)
                canvas.drawBitmap(
                    createCircleImage(reSizeImage, width, height),
                    paddingLeft.toFloat(), paddingTop.toFloat(), null
                )

            } else {

                val reSizeImage = reSizeImage(image, width, height)
                canvas.drawBitmap(
                    createRoundImage(reSizeImage, width, height),
                    paddingLeft.toFloat(), paddingTop.toFloat(), null
                )
            }
        }

    }

    /**
     * 画圆角
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private fun createRoundImage(source: Bitmap, width: Int, height: Int): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        val target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, mRadius, mRadius, paint)
        // 核心代码取两个图片的交集部分
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(source, 0f, 0f, paint)
        return target
    }

    /**
     * 画圆
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private fun createCircleImage(source: Bitmap, width: Int, height: Int): Bitmap {

        val paint = Paint()
        paint.isAntiAlias = true
        val target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        canvas.drawCircle(
            (width / 2).toFloat(), (height / 2).toFloat(), (Math.min(width, height) / 2).toFloat(),
            paint
        )
        // 核心代码取两个图片的交集部分
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(
            source, ((width - source.width) / 2).toFloat(),
            ((height - source.height) / 2).toFloat(), paint
        )
        return target

    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return if (mSrcBitmap != null) {
                mSrcBitmap
            } else {
                null
            }
        } else if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private fun reSizeImage(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        // 计算出缩放比
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 矩阵缩放bitmap
        val matrix = Matrix()

        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private fun reSizeImageC(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val x = (newWidth - width) / 2
        val y = (newHeight - height) / 2
        if (x > 0 && y > 0) {
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, null, true)
        }

        var scale = 1f

        if (width > height) {
            // 按照宽度进行等比缩放
            scale = newWidth.toFloat() / width

        } else {
            // 按照高度进行等比缩放
            // 计算出缩放比
            scale = newHeight.toFloat() / height
        }
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }
}
