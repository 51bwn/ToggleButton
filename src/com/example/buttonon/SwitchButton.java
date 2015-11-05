package com.example.buttonon;
import com.example.buttonon.SwitchButton.OnChangeListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/**
 * 按钮滑动自动归位效果 增加了点击 就可以自动切换 ，滑动也可以功能
 * 我这个和老师做的又不相同。。。。。。。。。。
 * 
 * @author luozheng
 * 
 */
public class SwitchButton extends View{
	private static final String TAG="SwitchButton";
	private Bitmap bitmapButton;
	private Bitmap bitmapBackground;
	private int max;
	private int dX;
	private int downX;
	private int left;
	private long startTime;
	private long endTime;
	private int downY;
	private boolean isOpen;
	private boolean isClick;
	private OnChangeListener listener;
	public SwitchButton(Context context,AttributeSet attrs){
		super(context,attrs);
		init();
	}
	private void init(){
		bitmapButton=BitmapFactory.decodeResource(getResources(),R.drawable.slide_button_background);
		bitmapBackground=BitmapFactory.decodeResource(getResources(),R.drawable.switch_background);
		max=bitmapBackground.getWidth()- bitmapButton.getWidth();// 最大值 减去最小值
																	// 否则是负数
	}
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		// 如果没写，那么包裹内容有多大有多大，实际上是整个屏幕的高宽 但是我只想要按钮或者图片都只要图片背景那么宽高，
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		setMeasuredDimension(bitmapBackground.getWidth(),bitmapBackground.getHeight());// 这样整个红色背景
																						// 就只有按钮的那么宽高了
	}
	// 执行流程 先加载然后再ONMeasure测量 再布局 onLayout 再画图 onDraw
	@Override
	protected void onDraw(Canvas canvas){
		Log.i(TAG,"重绘图形方法被调用");
		super.onDraw(canvas);
		canvas.drawBitmap(bitmapBackground,0,0,null);
		// canvas.drawBitmap(bitmapButton,dX,0,null);//应该先画背景然后画按钮否则按钮就不见了
		canvas.drawBitmap(bitmapButton,left,0,null);// 应该先画背景然后画按钮否则按钮就不见了
		// dx第一次是0的，但是调用了invalid那么又会重新走一遍本方法。。
		// 第一个参数是左边，如果填写为10 发现距离原来的位置 有一段距离也就是等于按钮 往右边移动了一点点了。
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		// downX不设置为成员变量那么 他会认为没有初始化
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			downX=(int) event.getX();
			downY=(int) event.getY();// 用于点击事件的坐标判断
			startTime=SystemClock.uptimeMillis();
			Log.i(TAG,"鼠标按下了"+ downX);
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX=(int) event.getX();
			dX=moveX- downX;// 移动的距离这里可以设置成按钮的左边起始点。
			left=left+ dX;
			left=left< 0?0:(left> max?max:left);
			downX=moveX;
			Log.i(TAG,"鼠标移动了"+ dX+ "，"+ "");
			invalidate();// 重走 画图方法
			break;
		case MotionEvent.ACTION_UP:
			endTime=SystemClock.uptimeMillis();// 别用错了，刚开始用成了那个什么线程事件时间
			int upX=(int) event.getX();
			int upY=(int) event.getY();
			// Log.i(TAG,"没有绝对值:"+(upX-downX)+"有绝对值;"+(Math.abs(upX-downX)));
			// 点击松开事件
			long speendTime=endTime- startTime;
			if((upX- downX)< 5&& (upY- downY)< 5&& speendTime< 200){
				isClick=true;
				left=upX> bitmapButton.getWidth()&& upX< bitmapBackground.getWidth()?max:0;
				// left=left>bitmapButton.getWidth()&&
				// left<bitmapBackground.getWidth()?max:0;//点的时候left不会改变，所以不是取left
				// 如果在右边 但是没超过按钮的宽度快速让按钮跳转到右边，如果在左边， 就不判断了直接为0
				Log.i(TAG,"按钮的点击事件,耗时"+ speendTime+ ",left="+ left);
			}else{
				// 滑动松开事件
				Log.i(TAG,"鼠标的滑动事件");
				left=left> max/ 2?max:0;// 不应该取现在的getX因为这是我鼠标点哪就是哪的位置，这里肯定是不准确的，
										// 应该是左边
			}
			// 下面的方法是减少图形被调用的次数
			if(left== max&& isOpen== true)// 本来就是开
			{
				if(isClick){
					isClick=false;//恢复原来状态
					return true;
				}
			}else if(left== 0&& isOpen== false)// 本来就是关闭
			{
				if(isClick){
					isClick=false;//恢复原来状态
					return true;//如果对于非按钮也返回绘图就变得很丑了。
				}
			}else{
				isOpen=left== 0?(isOpen)=false:(isOpen=true);// 赋值病运算
				if(listener!= null){
					listener.onChanage(isOpen);
				}
			}
			invalidate();
			break;
		default:
			break;
		}
		return true;
		// return super.onTouchEvent(event);//如果交给父类那么父类有一些判断，那么事件返回false,那么不会触发
		// 移动事件一直是down事件
	}
	public interface OnChangeListener{
		public void onChanage(boolean value);
	}
	public void setOnChangeListener(OnChangeListener listener){
		this.listener=listener;
	}
}
