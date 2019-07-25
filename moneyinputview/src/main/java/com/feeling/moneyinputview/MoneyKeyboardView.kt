package com.feeling.moneyinputview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class MoneyKeyboardView : View {
    var screenWidth = 0
    var screenHeight = 0
    val keys = arrayOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        com.feeling.moneyinputview.R.drawable.ic_confirm,
        "0",
        com.feeling.moneyinputview.R.drawable.ic_delete
    )

    private var pressedIndex = -1
    private var mContext: Context? = null
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rectList = mutableListOf<KeyArea>()
    private var onClickListener: OnClickListener? = null

    var dividerColor = Color.parseColor("#BFBFBF")
    var keyBackground: Drawable? = null //Color.parseColor("#FFFFFF")
    var textColor = Color.parseColor("#000000")
    var textSize = 28

    @SuppressLint("ResourceType")
    var deleteRes = com.feeling.moneyinputview.R.drawable.ic_delete

    @SuppressLint("ResourceType")
    var confirmRes = com.feeling.moneyinputview.R.drawable.ic_confirm

    var deleteDrawble: Drawable ? =null
    var confirmDrawable: Drawable ?= null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    @SuppressLint("NewApi")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr, 0){
        this.mContext = context!!
        screenWidth = getScreenW(context)
        screenHeight = getScreenH(context)
        var typedArray = context.obtainStyledAttributes(attrs, com.feeling.moneyinputview.R.styleable.MoneyKeyboardView)
        textSize = typedArray.getDimensionPixelOffset(com.feeling.moneyinputview.R.styleable.MoneyKeyboardView_textSize, 28)
        textColor = typedArray.getColor(
            com.feeling.moneyinputview.R.styleable.MoneyKeyboardView_textColor,
            Color.parseColor("#000000")
        )
        dividerColor = typedArray.getColor(
            com.feeling.moneyinputview.R.styleable.MoneyKeyboardView_dividerColor,
            Color.parseColor("#BFBFBF")
        )
        keyBackground = typedArray.getDrawable(
            com.feeling.moneyinputview.R.styleable.MoneyKeyboardView_keyBackground
        )

        if (keyBackground == null) {
            keyBackground = ColorDrawable(Color.parseColor("#FFFFFF"))
        }

        deleteRes = typedArray.getResourceId(
            com.feeling.moneyinputview.R.styleable.MoneyKeyboardView_deleteDrawable,
            com.feeling.moneyinputview.R.drawable.ic_delete
        )
        confirmRes = typedArray.getResourceId(
            com.feeling.moneyinputview.R.styleable.MoneyKeyboardView_confirmDrawable,
            com.feeling.moneyinputview.R.drawable.ic_confirm
        )
        typedArray.recycle()

        confirmDrawable = ContextCompat.getDrawable(context,confirmRes)!!
        deleteDrawble = ContextCompat.getDrawable(context,deleteRes)!!
    }


    override fun onDraw(canvas: Canvas?) {
        Log.e("feeling","onDraw")
        mTextPaint.color = textColor
        mTextPaint.textSize = textSize.toFloat()

        var rectW = width / 3f
        var rectH = height / 4f

        keyBackground?.bounds?.set(0, 0, rectW.toInt(), rectH.toInt())

        rectList.clear()
        for (index in keys.indices) {
            var row = index / 3
            var clu = index % 3
            var rect = RectF(rectW * clu, rectH * row, rectW * (clu + 1), rectH * (row + 1))
            var keyArea = KeyArea(rect, index)
            rectList.add(keyArea)

            canvas?.save()
            canvas?.translate(rectW * clu, rectH * row)
            if (index == pressedIndex) {
                keyBackground?.setState(intArrayOf(android.R.attr.state_pressed))
            }else{
                keyBackground?.setState(intArrayOf())
            }
            if (canvas != null) {
                keyBackground?.draw(canvas)
            }
            canvas?.restore()

            if (index == 9) {
                canvas?.saveLayer(rect, mPaint,Canvas.ALL_SAVE_FLAG)
                canvas?.translate(0f, rectH * row)

                setDrawableBounds(this!!.confirmDrawable!!, rectW, rectH)
                if (canvas != null) {
                    confirmDrawable?.draw(canvas)
                }

                canvas?.restore()
                continue
            } else if (index == 11) {
                canvas?.saveLayer(rect, mPaint,Canvas.ALL_SAVE_FLAG)
                canvas?.translate(rectW * clu, rectH * row)

                setDrawableBounds(this!!.deleteDrawble!!, rectW, rectH)
                if (canvas != null) {
                    deleteDrawble?.draw(canvas)
                }

                canvas?.restore()
                continue
            }

            canvas?.drawText(
                keys[index].toString(),
                rect.centerX().toFloat() - textSize / 3,
                rect.centerY().toFloat() + textSize / 2,
                mTextPaint
            )
        }

        mPaint.color = dividerColor

        canvas?.drawLine(0f, 0f, width.toFloat(), 0f, mPaint)
        canvas?.drawLine(0f, rectH.toFloat(), width.toFloat(), rectH.toFloat(), mPaint)
        canvas?.drawLine(0f, rectH.toFloat() * 2, width.toFloat(), rectH.toFloat() * 2, mPaint)
        canvas?.drawLine(0f, rectH.toFloat() * 3, width.toFloat(), rectH.toFloat() * 3, mPaint)

        canvas?.drawLine(rectW.toFloat(), 0f, rectW.toFloat(), height.toFloat(), mPaint)
        canvas?.drawLine(rectW.toFloat() * 2, 0f, rectW.toFloat() * 2, height.toFloat(), mPaint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e("feeling","onMeasure")
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        var width = 0
        var height = 0

        when (widthMode) {
            MeasureSpec.EXACTLY -> width = widthSize
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST ->
                width = screenWidth
        }

        when (heightMode) {
            MeasureSpec.EXACTLY -> height = heightSize
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {

                height = this.screenHeight * 3 / 7
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //获取事件坐标(x,y)
        var x = event?.x
        var y = event?.y
        pressedIndex = -1
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                pressedIndex = calClickArea(x!!, y!!)

                invalidate()

                when (pressedIndex) {
                    -1 -> return super.onTouchEvent(event)
                    9 -> {
                        onClickListener?.onConfirm()
                    }
                    11 -> {
                        onClickListener?.onDelete()
                    }
                    else -> {
                        onClickListener?.onClickKey(keys[pressedIndex])
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                pressedIndex = -1
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                pressedIndex = -1
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    fun setOnclickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    fun getOnClickListener(): OnClickListener? {
        return this.onClickListener
    }

    //设置drawable的bounds
    fun setDrawableBounds(drawable: Drawable, rectW: Float, rectH: Float) {

        drawable.setLevel(0)
        var drawableW = drawable.intrinsicWidth
        var drawableH = drawable.intrinsicWidth

        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f

        left = (rectW - drawableW) / 2
        top = (rectH - drawableH) / 2
        right = (rectW + drawableW) / 2
        bottom = (rectH + drawableH) / 2
        drawable.bounds.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    fun calClickArea(x: Float, y: Float): Int {
        for (keyArea in rectList) {
            if (keyArea.rectF.contains(x, y)) {
                return keyArea.index
            }
        }
        return -1
    }

    inner class KeyArea {
        var index: Int
        var rectF: RectF

        constructor(rectF: RectF, index: Int) {
            this.index = index
            this.rectF = rectF
        }

    }

    interface OnClickListener {
        fun onClickKey(keyValue: Any)
        fun onConfirm()
        fun onDelete()
    }

    companion object{
        /**
         * 获取屏幕宽度
         *
         * @param context the c
         * @return the screen w
         */
        fun getScreenW(context: Context): Int {
            return context.resources.displayMetrics.widthPixels
        }

        /**
         * 获取屏幕高度
         *
         * @param context the c
         * @return the screen h
         */
        fun getScreenH(context: Context): Int {
            return context.resources.displayMetrics.heightPixels
        }
    }
}