package com.x.components.widget;

public class ImageButton extends ImageView
{
	private OnClickAnimal mOnClickAnimal = null;

	public ImageButton()
	{
		super();
		setClickable(true);
	}
	@Override
	public void onClick()
	{
		if (mOnClickAnimal != null)
		{
			mOnClickAnimal.onClickAnimal(ImageButton.this);
		}
	}
}
