package dev.shadowmeld.viewdaydream.ui.components

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import dev.shadowmeld.viewdaydream.R
import dev.shadowmeld.viewdaydream.util.dp

class RoundImageView (
    context: Context, attrs: AttributeSet?,
    @AttrRes defStyleAttr: Int
) : AppCompatImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context): this(context, null)

    private val paint = Paint()
    private var viewMatrix = Matrix()
    private var width = 0F
    private var height = 0F
    private var radius = 8F.dp
    private var rectF: RectF

    init {
        paint.isAntiAlias = true
        rectF = RectF()
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView)
        radius = typeArray.getDimension(R.styleable.RoundImageView_radius, 8F.dp)
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        if (drawable is BitmapDrawable) {
            paint.shader = initBitmapShader(drawable as BitmapDrawable)
            rectF.set(0F, 0F, width, height)
            canvas?.drawRoundRect(rectF, radius, radius, paint)
            return
        }

        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth.toFloat()
        height = measuredHeight.toFloat()
    }

    /**
     * 获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
     */
    private fun initBitmapShader(drawable: BitmapDrawable): BitmapShader {
        val bitmap = drawable.bitmap
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val scale = (width / bitmap.width).coerceAtLeast(width / bitmap.height)
        viewMatrix.setScale(scale, scale)
        bitmapShader.setLocalMatrix(viewMatrix)
        return bitmapShader
    }
}