package com.x.components.listener;

import com.x.components.node.View;

public interface OnAnimationListener {

	void onAnimationStart(View view);
	void onAnimationEnd(View view);
	void onAnimationRepeat(View view);
}
