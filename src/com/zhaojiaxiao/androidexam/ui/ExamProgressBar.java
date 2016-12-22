package com.jiaogui.androidexam.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ExamProgressBar extends ProgressBar{
	String text;
	Paint mPaint;

	public ExamProgressBar (Context context) {
		super(context);
		initText();
	}

	public ExamProgressBar (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public ExamProgressBar (Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		setText(progress);
		super.setProgress(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();

		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mPaint);
	}

	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.WHITE);
	}

	private void setText(int progress) {
		//int i = (progress * 1) / this.getMax();
		this.text = progress+"/"+this.getMax();
	}

}
