package com.example.buttonon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/**
 * 按钮滑动自动归位效果
 * 
 * @author luozheng
 *
 */
public class SwitchButton2 extends View{

	private static final String TAG="SwitchButton";
	private Bitmap bitmapButton;
	private Bitmap bitmapBackground;
	private int max;
	private int dX;
	private int downX;
	private int left;
	public SwitchButton2(Context context,AttributeSet attrs){
		super(context,attrs);
		init();
	}
	private void init(){
		bitmapButton=BitmapFactory.decodeResource(getResources(),R.drawable.slide_button_background);
		bitmapBackground=BitmapFactory.decodeResource(getResources(),R.drawable.switch_background);
		max=bitmapBackground.getWidth()-bitmapButton.getWidth();//最大值 减去最小值 否则是负数
	}
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		//如果没写，那么包裹内容有多大有多大，实际上是整个屏幕的高宽 但是我只想要按钮或者图片都只要图片背景那么宽高，
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		setMeasuredDimension(bitmapBackground.getWidth(),bitmapBackground.getHeight());// 这样整个红色背景 就只有按钮的那么宽高了
		
	}
	//执行流程  先加载然后再ONMeasure测量 再布局 onLayout 再画图 onDraw
	@Override
	protected void onDraw(Canvas canvas){
		
		super.onDraw(canvas);
		canvas.drawBitmap(bitmapBackground,0,0,null);
//		canvas.drawBitmap(bitmapButton,dX,0,null);//应该先画背景然后画按钮否则按钮就不见了  
		canvas.drawBitmap(bitmapButton,left,0,null);//应该先画背景然后画按钮否则按钮就不见了  
		//dx第一次是0的，但是调用了invalid那么又会重新走一遍本方法。。
		//第一个参数是左边，如果填写为10 发现距离原来的位置 有一段距离也就是等于按钮 往右边移动了一点点了。
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		//downX不设置为成员变量那么 他会认为没有初始化
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			downX=(int) event.getX();
			Log.i(TAG,"鼠标按下了"+downX);
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX=(int) event.getX();
			dX=moveX-downX;//移动的距离这里可以设置成按钮的左边起始点。
//			dX=dX<0?0:(dX>max?max:dX);//如果小于0设置为0如果大于max那么再设置为max
		
			//第二种写法 就是  下面的需要downX修改， 二如果是上面的那么直需在绘图的哪里 填写dX,但是这样做会出现某个时刻卡闪按钮的情况
			left=left+dX;
			left=left<0?0:(left>max?max:left);
			downX=moveX;
			Log.i(TAG,"鼠标移动了"+dX+"，"+"");
			invalidate();//重走 画图方法
			break;
		case MotionEvent.ACTION_UP:
			left=left>max/2?max:0;//不应该取现在的getX因为这是我鼠标点哪就是哪的位置，这里肯定是不准确的， 应该是左边
			invalidate();
			break;
		default:
			break;
		}
		return true;
//	return super.onTouchEvent(event);//如果交给父类那么父类有一些判断，那么事件返回false,那么不会触发 移动事件一直是down事件
	}
}
