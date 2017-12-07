package com.x.components.c_widget;

import com.x.components.node.View;

public interface C_Adapter
{
	View getView(int position, View convertView);

	int getCount();
}
