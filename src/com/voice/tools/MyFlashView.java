package com.voice.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyFlashView extends View {
	private Paint paint;
	public int a;
	private final Context context;

	public MyFlashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.paint = new Paint();
		this.paint.setAntiAlias(true);// �������
		this.paint.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int center = getWidth() / 2;
		int innerCircle = dip2px(context, 83); // ������Բ�뾶
		int ringWidth = dip2px(context, 5); // ����Բ�����

		// ������Բ
		this.paint.setColor(Color.parseColor("#7ecef4"));
		this.paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle, this.paint);

		// ����Բ��
		this.paint.setColor(Color.parseColor("#7ecef4"));
		this.paint.setStrokeWidth(ringWidth);
		canvas.drawCircle(center, center, innerCircle + 1 + ringWidth / 2,
				this.paint);

		// ������Բ
		this.paint.setColor(Color.parseColor("#7ecef4"));
		this.paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle + ringWidth, this.paint);

	}

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
