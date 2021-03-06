package com.itkc_carlife.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class InsideGridView extends GridView {

	public InsideGridView(Context context) {
		super(context);
	}

	public InsideGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InsideGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
