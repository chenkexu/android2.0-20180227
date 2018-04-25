package com.orientdata.lookforcustomers.widget.graph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.orhanobut.logger.Logger;
import com.orientdata.lookforcustomers.R;
import com.orientdata.lookforcustomers.util.CommonUtils;
import com.orientdata.lookforcustomers.widget.DensityUtils;

/**
 * 报表 折线
 */

@SuppressLint("DrawAllocation")
public class MyChartView extends View {
	// 默认边距
	private int Margin = 20;
	// 原点坐标
	private int Xpoint;
	private int Ypoint;
	// X,Y轴的单位长度
	private int Xscale = 2;
	private int xScale = 2;//X轴每块区域的长度

	private int Yscale = 5;

	// X,Y轴上面的显示文字
	private String[] Xlabel = {"", "", "", "", "", ""}; //X轴显示的文字
	private String[] Ylabel = {"0", "100", "200", "300", "400", "500"};

	private int[] y = {0};

	private Context context;
	// 标题文本
	private String Title;
	private int lineColor;
	private Path linePath;
	private Paint linePaint;
	private Paint linePaint1;
	private float length;
//	private Paint p1;
	private float phase = 0.0f;
	public int mTouchIndex = -1;
	private Context mContext;
	private int verticalLineColor = R.color.c_09B6F2;
	private int scaleTextColor = R.color.c_414141;
	private float eventX = 0;//手指按下的横坐标
	private Bitmap mBitmap;
	private Rect mBitmapRect = null;
	private RectF mRect = new RectF(0, 0, 0,dpTopx(50));
	private boolean isVisble = false;//纵向的指示线是否显示


    //求数组的最大值和最小值
    private void maxY(int[] A){
        int i,min,max;
        min=max=A[0];
        System.out.print("数组A的元素包括：");
        for(i=0;i<A.length;i++) {
            System.out.print(A[i]+" ");
            if(A[i]>max)   // 判断最大值
                max=A[i];
            if(A[i]<min)   // 判断最小值
                min=A[i];
        }
        Logger.e("Y轴最大值为："+max);

		if (max!=0) {
			int length = (max / 5);
			Ylabel[5] = max + "";
			Ylabel[4] = length * 4+"";
			Ylabel[3] = length * 3+"";
			Ylabel[2] = length * 2+"";
			Ylabel[1] = length + "";
			Logger.e("maxY:"+Ylabel[0]+Ylabel[1]+Ylabel[2]+"-"+Ylabel[3]+"--"+Ylabel[4]+"-"+Ylabel[5]);
		}
    }


    //设置Y轴显示的数据
    private void setYlabel(int maxFloat){
		if (maxFloat<1000) {
			maxFloat=(maxFloat/100+1)*100;
		}else if (1000<maxFloat&&maxFloat<5000){
			maxFloat=(maxFloat/100+5)*100;
		}
		else if (5000<maxFloat&&maxFloat<10000){
			maxFloat=(maxFloat/1000+1)*1000;
		}
		else if (10000<maxFloat&&maxFloat<50000){
			maxFloat=(maxFloat/1000+5)*1000;
		}
		else if (50000<maxFloat&&maxFloat<100000){
			maxFloat=(maxFloat/10000+1)*10000;
		}
		else if (maxFloat>100000){
			maxFloat=(maxFloat/10000+2)*10000;
		}

		if (maxFloat!=0) {
			int length = (maxFloat / 5);
			Ylabel[5] = maxFloat + "";
			Ylabel[4] = length * 4+"";
			Ylabel[3] = length * 3+"";
			Ylabel[2] = length * 2+"";
			Ylabel[1] = length + "";
			Ylabel[0] = "0";
//			Logger.e("maxY:"+Ylabel[1]+Ylabel[2]+"-"+Ylabel[3]+"--"+Ylabel[4]+"-"+Ylabel[5]);
		}
	}


	// 曲线数据
	public MyChartView(Context context, String[] xlabel, String title, int[] y, int lineColor,int max){
		super(context);

		setYlabel(max);
		this.mContext = context;
		Margin = CommonUtils.dipToPx(context,10);
		if (null != xlabel) {
			this.Xlabel = xlabel;
		}
		this.Title = title;


		if (null != y) {
			//求出最大值
//			maxY(y);
			this.y = y;
        }

		this.context = context;
		this.lineColor = lineColor;
		mBitmap = ((BitmapDrawable) getContext().getResources().getDrawable(R.mipmap.dot)).getBitmap();
		mBitmapRect = new Rect(0, 0, mBitmap.getWidth()*2, mBitmap.getHeight()*2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

//		maxY(y);

		init(canvas);
//		this.drawXLine(canvas, p1);
//		this.drawYLine(canvas, p1);
		this.drawTable(canvas);
//		PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);
//		linePaint.setPathEffect(effects);

		Path dst = new Path();
//		postInvalidateasure.getSegment(0, currentValue * measure.getLength(), dst, true);
		//画图
		canvas.drawPath(linePath,linePaint);

		if(isVisble){
			mRect.left = eventX-mBitmap.getWidth()/2;
			mRect.right = eventX+mBitmap.getWidth()/2;
			mRect.top = Ypoint-mBitmap.getWidth()/2;
			mRect.bottom = Ypoint+mBitmap.getWidth()/2;
			canvas.drawBitmap(mBitmap,null,mRect,null);
			linePaint1 = new Paint();
			linePaint1.setAntiAlias(true);
			linePaint1.setStyle(Paint.Style.STROKE);
			linePaint1.setStrokeWidth(5);//笔宽5像素
			linePaint1.setColor(context.getResources().getColor(R.color.c_D9D9D9));

			//画线
			canvas.drawLine(eventX,Ypoint-this.getHeight(),eventX,Ypoint-mBitmap.getWidth()/2,linePaint1);
		}

	}

	// 初始化数据值
	public void init(Canvas canvas) {
		Xpoint = 3*this.Margin;
		//初始化Y轴的原点
		Ypoint = this.getHeight() - 2*this.Margin;
		if(this.Xlabel.length - 1 == 0){ //如果只有一个值
			Xscale = (this.getWidth() - 4 * this.Margin) / 2;//x轴每块的长度显示出来的长度
		}else{
			Xscale = (this.getWidth() - 4 * this.Margin) / (this.Xlabel.length - 1);//x轴每块的长度显示出来的长度
		}

		if(this.y.length-1 == 0){  //Y轴的每一个数据
			xScale = (this.getWidth() - 4 * this.Margin) / 2;//x轴每个刻度代表的长度 可能有没显示出来的横坐标 按照纵坐标个数计算
		}else{
			xScale = (this.getWidth() - 4 * this.Margin) / (this.y.length-1);//x轴每个刻度代表的长度 可能有没显示出来的横坐标 按照纵坐标个数计算
		}

		//Y轴每块区域的长度 =（chartView的总高度 - 50)/5
		Yscale = (this.getHeight() - 2 * this.Margin) / (this.Ylabel.length - 1);//y轴每块的长度

		linePath = new Path();
		int xCatch = 0;//最后一个不是0的x坐标
		int yCatch = 0;//最后一个不是0的y坐标
		int count = y.length;



		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setStrokeWidth(5);//笔宽5像素
		linePaint.setColor(context.getResources().getColor(lineColor));


		if(count > 1){
			//2个点以上
			for (int i = 0; i < y.length; ) {
				if(yCatch!=0){
					//如果记录上一个最后一个不为0的坐标
//						canvas.drawLine(xCatch, yCatch, (Xpoint + i * xScale), calY1(y[i]), p);
					yCatch = calY1(y[i]);
					xCatch = (Xpoint + i * xScale);
					linePath.lineTo(xCatch,yCatch);
				}else{
					yCatch = calY1(y[i]);
					xCatch = Xpoint+(i)*xScale;
					linePath.moveTo(xCatch,yCatch);
				}
				i++;
			}
			PathMeasure measure = new PathMeasure(linePath, false);
			length = measure.getLength();
			CornerPathEffect cornerPathEffect = new CornerPathEffect(200);
			linePaint.setPathEffect(createPathEffect(length, phase, 0.0f));
			linePaint.setPathEffect(cornerPathEffect);
		}else{
			if(y!=null && y.length>0){
				canvas.drawPoint((Xpoint +  xScale), calY1(y[0]),linePaint);
			}
		}
	}



	/**
	 * 获取value值所占的view高度
	 *
	 * @param value
	 * @return
	 */
//	private int getValueHeight(int value) {
//		float valuePercent = Math.abs(value - minValue) * 100f / (Math.abs(maxValue - minValue) * 100f);//计算value所占百分比
//		return (int) (getViewDrawHeight() * valuePercent + bottomSpace + 0.5f);//底部加上间隔
//	}


	private float heightPercent = 0.618f;

	/**
	 * 获取绘制区域高度
	 *
	 * @return
	 */
	private float getViewDrawHeight() {
		return getMeasuredHeight() * heightPercent;
	}



	/**
	 * 开始绘制动画
	 */
	public void startDrawLine(int durationTime){
		phase = 0.0f;
		ObjectAnimator animator = ObjectAnimator.ofFloat(MyChartView.this, "phase", 0.0f, 1.0f);
		animator.setDuration(5);
		animator.start();
	}

	public void setPhase(float phase) {
		this.phase = phase;
		invalidate();//will calll onDraw
	}

	private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
		return new DashPathEffect(new float[] { pathLength, pathLength },
				pathLength - phase * pathLength);
//		PathEffect effects = new DashPathEffect(new float[] { 25, 25, 25, 25 }, 4);
//		return effects;
	}


	/**
	 * 计算纵坐标的坐标点
	 * @param y
	 * @return
	 */
	private int calY1(int y){
		
		int baseNum = (Integer.parseInt(Ylabel[1])) / 2;  //300
		// TODO: 2018/4/24 这里需要改一下 
		int str = baseNum / 10;

		if(y>-1 && y<baseNum+1){	           //0-50
			return calY(y,Ylabel[0],Ylabel[1]);
		}else if(y>baseNum && y<baseNum*3){    //51-150
			return (calY(y,Ylabel[1],Ylabel[2])-(2*Yscale))+str;
		}else if(y>baseNum*3 && y<baseNum*6+1){  //151-301
			return calY(y,Ylabel[2],Ylabel[3])-3*Yscale+str;
		}else if(y>baseNum*6 && y<baseNum*10+1){  //1800-3001

			return calY(y,Ylabel[3],Ylabel[4])-4*Yscale+str;

		}else if(y>baseNum*10){ //   5000-
			y = baseNum*10;
			return calY(y,Ylabel[4],Ylabel[5])-4*Yscale;
		}
		return 0;
	}
		
	/**
	 *
	 * @param y
	 * @return
	 */
	private int calY(int y, String label1, String label2){
		int y0 = 0 ;
		int y1 = 0 ;
		try{
			//Y是传过来的值
			y0 = Integer.parseInt(label1); //纵轴的一个值
			y1 = Integer.parseInt(label2); //纵轴的一个值
		}catch(Exception e){
			return 0;
		}
		//Y是传过来的值。Yscale是间距
		int i = Ypoint - ((y - y0) * Yscale / (y1 - y0));
		Logger.d("Ypoint-((y-y0)*Yscale/(y1-y0))值:"+ i);
		return (Ypoint - ((y - y0) * Yscale / (y1 - y0)));
	}





	public int getMargin() {
		return Margin;
	}
	public void setMargin(int margin) {
		Margin = margin;
	}






	// 画表格
	private void drawTable(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setColor(mContext.getResources().getColor(R.color.c_D9D9D9));
		Path path = new Path();
		PathEffect effects = new DashPathEffect(new float[] { 10, 10, 10, 10 }, 2);
		paint.setPathEffect(effects);

		Paint mPaint = new Paint();
		mPaint.setStrokeWidth(1);
		mPaint.setColor(ContextCompat.getColor(context,R.color.c_414141));


//		// 纵向线
//		for (int i = 1; i * Xscale <= (this.getWidth() - (3*this.Margin)); i++) {
//			int startX = Xpoint + i * Xscale;
//			int startY = Ypoint;
//			int stopY = Ypoint - (this.Ylabel.length - 1) * Yscale;
//			path.moveTo(startX, startY);//移动画笔
//			path.lineTo(startX, stopY);//画线
//			canvas.drawPath(path, paint);
//		}
		// 横向线 // TODO: 2018/4/20 这个地方改成了 -2
		for (int i = 0; (Ypoint - i * Yscale) >= (-2*this.Margin); i++) {
			int startX = Xpoint;
			int startY = Ypoint - i * Yscale;
			int stopX = 0;
			if(Xlabel.length - 1 == 0){
				stopX = Xpoint + 2 * Xscale;
			}else{
				stopX = Xpoint + (this.Xlabel.length - 1) * Xscale;
			}
			path.moveTo(startX, startY);
			path.lineTo(stopX, startY);
			paint.setColor(ContextCompat.getColor(context, R.color.c_D9D9D9));
			canvas.drawPath(path, paint);
			mPaint.setTextSize(this.Margin);
			
			canvas.drawText(this.Ylabel[i], (3*this.Margin) / 8, startY + (3*this.Margin) / 4, mPaint);//纵坐标的数字
		}


		mPaint.setTextSize(this.Margin-3);
		mPaint.setColor(getResources().getColor(scaleTextColor));
		if(Xlabel.length == 1){
			int width = getTextWidth(mPaint,this.Xlabel[0]);
			int startX = Xpoint + Xscale - (width/2);
			//横坐标的字
//			mPaint.setColor(mTouchIndex == 0 ? getResources().getColor(verticalLineColor) : getResources().getColor(scaleTextColor));
			canvas.drawText(this.Xlabel[0], startX,this.getHeight() - (3*this.Margin) / 4, mPaint);
		}else{
			if(Xlabel!=null){
				for (int i = 0; i * Xscale <= (this.getWidth() - (4*this.Margin)); i++) {
					int startX = Xpoint + i * Xscale;
					//横坐标的字
					if(Xlabel.length>i){
						int width = getTextWidth(mPaint,this.Xlabel[i]);
//				mPaint.setColor(mTouchIndex == i ? getResources().getColor(verticalLineColor) : getResources().getColor(scaleTextColor));
						canvas.drawText(this.Xlabel[i], startX - width/2-1,this.getHeight() - (3*this.Margin) / 4, mPaint);
					}

				}
			}
		}
	}
	public  int getTextWidth(Paint paint, String str) {
		int iRet = 0;
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];
			paint.getTextWidths(str, widths);
			for (int j = 0; j < len; j++) {
				iRet += (int) Math.ceil(widths[j]);
			}
		}
		return iRet;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean touchEvent = super.onTouchEvent(event);
		if ((event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN)) {
			float dur = Float.MAX_VALUE;
			float endX = this.getWidth()-Margin;
			if(event.getX()>Xpoint && event.getX()<endX){
				eventX = event.getX();
				isVisble = true;
				postInvalidate();
			}
			// 点多了会慢，如果想优化，请用二叉树将X作为节点
			for (int i = 0; i < y.length; i++) {
				if (Math.abs((Xpoint+i*xScale) - event.getX()) < dur) {
					dur = Math.abs((Xpoint+i*xScale) - event.getX());
					mTouchIndex = i;
				}
			}
			clickBtn.onClickLine(mTouchIndex);
			touchEvent = true;
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			mTouchIndex = -1;
			clickBtn.onClickLine(mTouchIndex);
			float endX = this.getWidth()-Margin;
			isVisble = false;
			postInvalidate();
			touchEvent = true;
		}
		return touchEvent;
	}

	public static ClickListener clickBtn = null;
	public void setClickListener(ClickListener clickBtn){
		this.clickBtn = clickBtn;
	}

	public interface  ClickListener{
		void onClickLine(int touchIndex);
	}


	// 画横轴
	private void drawXLine(Canvas canvas, Paint p) {
		canvas.drawLine(Xpoint, Ypoint, this.getWidth() - this.Margin, Ypoint, p);
	}
	// 画纵轴
	private void drawYLine(Canvas canvas, Paint p) {
		canvas.drawLine(Xpoint, Ypoint, 3*this.Margin, 2*this.Margin, p);
	}


	private int dpTopx(float dp){
		return DensityUtils.dp2px(getContext(), dp);
	}
}
