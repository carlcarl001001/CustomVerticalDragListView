package com.example.carl.customverticaldraglistview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class VerticalDragListView extends FrameLayout {
    //可以认为这是系统提供给我们写好的一个工具类
    private ViewDragHelper mDragHelper;
    private View mDragListView;
    //后面一个菜单的高度
    private int mMenuHeight=0;
    private boolean mMenuIsOpen=false;
    public VerticalDragListView(@NonNull Context context) {
        //super(context);
        this(context,null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context,attrs,0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper =ViewDragHelper.create(this, mDragHelperCallback);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount!=2){
            throw new RuntimeException("VerticalDragListView 只能包含两个布局。");
        }
        mDragListView = getChildAt(1);

        //View menuView = getChildAt(0);
        //mMenuHeight=menuView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed){
            View menuView =getChildAt(0);
            mMenuHeight=menuView.getMeasuredHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    //1.拖动我们的子View
    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            //指定该子View是否可以拖动，即child
            return mDragListView==child;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            //垂直移动的位置
            //垂直拖动范围只能是后面菜单的 view的高度
            if(top<=0){
                top=0;
            }
            if(top>=mMenuHeight){
                top = mMenuHeight;
            }
            return top;
        }
        //手指松开的时候二选一
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if(mDragListView.getTop()>mMenuHeight/2){
                mDragHelper.settleCapturedViewAt(0,mMenuHeight);
                mMenuIsOpen=true;
            }else {
                mDragHelper.settleCapturedViewAt(0,0);
                mMenuIsOpen = false;
            }
            invalidate();
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    //现象是ListView可以滑动，但是菜单没有效果

    private float mDownY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //菜单打开要全部拦截

        if (mMenuIsOpen){
            return true;
        }
        //向下滑动拦截，不要给ListView做处理
        //父View拦截子View
        //但子View可以调用requestDisallowInterceptTouchEvent请求父View不要拦截
        //改变的其实就是mGroupFlags的值
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                //让DragHelper拿一个完整的事件
                mDragHelper.processTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY=event.getY();
                if ((moveY-mDownY)>0){
                    //向下滑动，拦截不让lisView处理
                    return true;
                }
                break;
        }
        return super.onInterceptHoverEvent(event);
    }

}
