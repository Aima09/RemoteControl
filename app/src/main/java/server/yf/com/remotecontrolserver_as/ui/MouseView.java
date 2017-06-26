package server.yf.com.remotecontrolserver_as.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import server.yf.com.remotecontrolserver_as.R;

public class MouseView extends FrameLayout {
		private int viewWidth;
		private int viewHeight;
	    public MouseView(Context context) {
	        super(context);
	        init(context);
	    }

	    public MouseView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init(context);
	    }

	    public MouseView(Context context, AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr);
	        init(context);
	    }

	    private void init(Context context){
	            LayoutInflater.from(context).inflate(R.layout.activity_main, this);
	            View view = findViewById(R.id.iv);
	            android.view.ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
	            viewHeight=layoutParams.height;
	            viewWidth=layoutParams.width;
	    }

	    public int getViewWidth() {
	        return viewWidth;
	    }

	    public int getViewHeight() {
	        return viewHeight;
	    }
}
