package com.itkc_carlife.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itkc_carlife.R;
import com.itkc_carlife.tool.WinTool;


public class MessageDialog {

	private Context context;
	private AlertDialog ad;
	private Window window;

	private TextView title;
	private TextView message;
	private TextView commit;
	private FrameLayout commitBox;
	private TextView cancel;
	private FrameLayout cancelBox;

	private boolean noExcep = true;

	public MessageDialog(Context context) {
		this.context = context;
		try {
			ad = new AlertDialog.Builder(context).create();
			ad.show();

			window = ad.getWindow();
			window.setContentView(R.layout.layout_message_dialog);

			title = (TextView) window.findViewById(R.id.messageDialog_title);
			message = (TextView) window
					.findViewById(R.id.messageDialog_message);
			commit = (TextView) window.findViewById(R.id.messageDialog_commit);
			commitBox = (FrameLayout) window
					.findViewById(R.id.messageDialog_commitBox);
			cancel = (TextView) window.findViewById(R.id.messageDialog_cancel);
			cancelBox = (FrameLayout) window
					.findViewById(R.id.messageDialog_cancelBox);

			setLayout();
			setCanceledOnTouchOutside(false);
		} catch (Exception e) {
			noExcep = false;
		}
	}

	public void setLayout() {
		setLayout(0.8, 0.3);
	}

	public void setLayout(double Xnum, double Ynum) {
		if (noExcep) {
			window.setLayout((int) (WinTool.getWinWidth(context) * Xnum),
					(int) (WinTool.getWinHeight(context) * Ynum));
		}
	}

	public void setTitel(String titleStr) {
		if (noExcep && titleStr != null && !titleStr.equals("")) {
			title.setVisibility(View.VISIBLE);
			title.setText(titleStr);
		}
	}

	public void setMessage(String messageStr) {
		if (noExcep && messageStr != null && !messageStr.equals("")) {
			message.setVisibility(View.VISIBLE);
			message.setText(messageStr);
			message.setMovementMethod(ScrollingMovementMethod.getInstance());
		}
	}

	public void setCanceledOnTouchOutside(boolean isOutside) {
		if (noExcep) {
			ad.setCanceledOnTouchOutside(isOutside);
		}
	}

	public void setCommitStyle(String message) {
		if (noExcep) {
			commitBox.setVisibility(View.VISIBLE);
			commit.setText(message);
		}
	}

	public void setCancelStyle(String message) {
		if (noExcep) {
			cancelBox.setVisibility(View.VISIBLE);
			cancel.setText(message);
		}
	}

	public void setCommitListener(final CallBackListener callback) {
		if (noExcep) {
			commitBox.setVisibility(View.VISIBLE);
			commit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (callback != null) {
						callback.callback();
					}
					dismiss();
				}
			});
		}
	}

	public void setCancelListener(final CallBackListener callback) {
		if (noExcep) {
			cancelBox.setVisibility(View.VISIBLE);
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (callback != null) {
						callback.callback();
					}
					dismiss();
				}
			});
		}
	}

	public void dismiss() {
		ad.dismiss();
	}

	public interface CallBackListener {
		public void callback();
	}

}
