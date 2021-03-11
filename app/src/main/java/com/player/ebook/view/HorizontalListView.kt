package com.player.ebook.view

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.Scroller
import java.util.*

//com.xiaojian.frame.widgets.HorizontalListView
class HorizontalListView(
    context: Context?,
    attrs: AttributeSet?
) : AdapterView<ListAdapter?>(context, attrs) {
    var mAlwaysOverrideTouch = true
    protected var mAdapter: ListAdapter? = null
    private var mLeftViewIndex = -1
    private var mRightViewIndex = 0
    protected var mCurrentX = 0
    protected var mNextX = 0
    private var mMaxX = Int.MAX_VALUE
    private var mDisplayOffset = 0
    protected var mScroller: Scroller? = null
    private var mGesture: GestureDetector? = null
    private val mRemovedViewQueue: Queue<View> =
        LinkedList()
    private var mOnItemSelected: OnItemSelectedListener? = null
    private var mOnItemClicked: OnItemClickListener? = null
    private var mOnItemLongClicked: OnItemLongClickListener? = null
    private var mDataChanged = false

    @Synchronized
    private fun initView() {
        mLeftViewIndex = -1
        mRightViewIndex = 0
        mDisplayOffset = 0
        mCurrentX = 0
        mNextX = 0
        mMaxX = Int.MAX_VALUE
        mScroller = Scroller(context)
        mGesture = GestureDetector(context, mOnGesture)
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        mOnItemSelected = listener
    }

    override fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClicked = listener
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mOnItemLongClicked = listener
    }

    private val mDataObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            synchronized(this@HorizontalListView) { mDataChanged = true }
            invalidate()
            requestLayout()
        }

        override fun onInvalidated() {
            reset()
            invalidate()
            requestLayout()
        }
    }

    override fun getAdapter(): ListAdapter? {
        return mAdapter
    }

    override fun getSelectedView(): View? {
        //TODO: implement
        return null
    }

    override fun setAdapter(adapter: ListAdapter?) {
        if (mAdapter != null) {
            mAdapter!!.unregisterDataSetObserver(mDataObserver)
        }
        mAdapter = adapter
        mAdapter!!.registerDataSetObserver(mDataObserver)
        reset()
    }

    @Synchronized
    private fun reset() {
        initView()
        removeAllViewsInLayout()
        requestLayout()
    }

    override fun setSelection(position: Int) {
        //TODO: implement
    }

    private fun addAndMeasureChild(child: View, viewPos: Int) {
        var params = child.layoutParams
        if (params == null) {
            params = LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT
            )
        }
        addViewInLayout(child, viewPos, params, true)
        child.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST)
        )
    }

    @Synchronized
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        if (mAdapter == null) {
            return
        }
        if (mDataChanged) {
            val oldCurrentX = mCurrentX
            initView()
            removeAllViewsInLayout()
            mNextX = oldCurrentX
            mDataChanged = false
        }
        if (mScroller!!.computeScrollOffset()) {
            val scrollx = mScroller!!.currX
            mNextX = scrollx
        }
        if (mNextX <= 0) {
            mNextX = 0
            mScroller!!.forceFinished(true)
        }
        if (mNextX >= mMaxX) {
            mNextX = mMaxX
            mScroller!!.forceFinished(true)
        }
        val dx = mCurrentX - mNextX
        removeNonVisibleItems(dx)
        fillList(dx)
        positionItems(dx)
        mCurrentX = mNextX
        if (!mScroller!!.isFinished) {
            post { requestLayout() }
        }
    }

    private fun fillList(dx: Int) {
        var edge = 0
        var child = getChildAt(childCount - 1)
        if (child != null) {
            edge = child.right
        }
        fillListRight(edge, dx)
        edge = 0
        child = getChildAt(0)
        if (child != null) {
            edge = child.left
        }
        fillListLeft(edge, dx)
    }

    private fun fillListRight(rightEdge: Int, dx: Int) {
        var rightEdge = rightEdge
        while (rightEdge + dx < width && mRightViewIndex < mAdapter!!.count) {
            val child =
                mAdapter!!.getView(mRightViewIndex, mRemovedViewQueue.poll(), this)
            addAndMeasureChild(child, -1)
            rightEdge += child.measuredWidth
            if (mRightViewIndex == mAdapter!!.count - 1) {
                mMaxX = mCurrentX + rightEdge - width
            }
            if (mMaxX < 0) {
                mMaxX = 0
            }
            mRightViewIndex++
        }
    }

    private fun fillListLeft(leftEdge: Int, dx: Int) {
        var leftEdge = leftEdge
        while (leftEdge + dx > 0 && mLeftViewIndex >= 0) {
            val child =
                mAdapter!!.getView(mLeftViewIndex, mRemovedViewQueue.poll(), this)
            addAndMeasureChild(child, 0)
            leftEdge -= child.measuredWidth
            mLeftViewIndex--
            mDisplayOffset -= child.measuredWidth
        }
    }

    private fun removeNonVisibleItems(dx: Int) {
        var child = getChildAt(0)
        while (child != null && child.right + dx <= 0) {
            mDisplayOffset += child.measuredWidth
            mRemovedViewQueue.offer(child)
            removeViewInLayout(child)
            mLeftViewIndex++
            child = getChildAt(0)
        }
        child = getChildAt(childCount - 1)
        while (child != null && child.left + dx >= width) {
            mRemovedViewQueue.offer(child)
            removeViewInLayout(child)
            mRightViewIndex--
            child = getChildAt(childCount - 1)
        }
    }

    private fun positionItems(dx: Int) {
        if (childCount > 0) {
            mDisplayOffset += dx
            var left = mDisplayOffset
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val childWidth = child.measuredWidth
                child.layout(left, 0, left + childWidth, child.measuredHeight)
                left += childWidth + child.paddingRight
            }
        }
    }

    @Synchronized
    fun scrollTo(x: Int) {
        mScroller!!.startScroll(mNextX, 0, x - mNextX, 0)
        requestLayout()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        var handled = super.dispatchTouchEvent(ev)
        handled = handled or mGesture!!.onTouchEvent(ev)
        return handled
    }

    protected fun onFling(
        e1: MotionEvent?, e2: MotionEvent?, velocityX: Float,
        velocityY: Float
    ): Boolean {
        synchronized(
            this@HorizontalListView
        ) { mScroller!!.fling(mNextX, 0, (-velocityX).toInt(), 0, 0, mMaxX, 0, 0) }
        requestLayout()
        return true
    }

    protected fun onDown(e: MotionEvent?): Boolean {
        mScroller!!.forceFinished(true)
        return true
    }

    private val mOnGesture: GestureDetector.OnGestureListener = object : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return this@HorizontalListView.onDown(e)
        }

        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float,
            velocityY: Float
        ): Boolean {
            return this@HorizontalListView.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent,
            distanceX: Float, distanceY: Float
        ): Boolean {
            synchronized(this@HorizontalListView) { mNextX += distanceX.toInt() }
            requestLayout()
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (isEventWithinView(e, child)) {
                    if (mOnItemClicked != null) {
                        mOnItemClicked!!.onItemClick(
                            this@HorizontalListView,
                            child,
                            mLeftViewIndex + 1 + i,
                            mAdapter!!.getItemId(mLeftViewIndex + 1 + i)
                        )
                    }
                    if (mOnItemSelected != null) {
                        mOnItemSelected!!.onItemSelected(
                            this@HorizontalListView,
                            child,
                            mLeftViewIndex + 1 + i,
                            mAdapter!!.getItemId(mLeftViewIndex + 1 + i)
                        )
                    }
                    break
                }
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val childCount = childCount
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (isEventWithinView(e, child)) {
                    if (mOnItemLongClicked != null) {
                        mOnItemLongClicked!!.onItemLongClick(
                            this@HorizontalListView,
                            child,
                            mLeftViewIndex + 1 + i,
                            mAdapter!!.getItemId(mLeftViewIndex + 1 + i)
                        )
                    }
                    break
                }
            }
        }

        private fun isEventWithinView(e: MotionEvent, child: View): Boolean {
            val viewRect = Rect()
            val childPosition = IntArray(2)
            child.getLocationOnScreen(childPosition)
            val left = childPosition[0]
            val right = left + child.width
            val top = childPosition[1]
            val bottom = top + child.height
            viewRect[left, top, right] = bottom
            return viewRect.contains(e.rawX.toInt(), e.rawY.toInt())
        }
    }

    init {
        initView()
    }
}