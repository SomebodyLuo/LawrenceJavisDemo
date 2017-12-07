package com.x.components.widget;


public class Button extends TextView
{
	private OnClickAnimal mOnClickAnimal=null;
	
	public Button()
	{
		super();
		setClickable(true);
	}
	
	public void setOnClickAnimal(OnClickAnimal onClickAnimal)
	{
		this.mOnClickAnimal=onClickAnimal;
	}
	
	
	@Override
	public void onClick()
	{
		if(mOnClickAnimal!=null)
		{
			mOnClickAnimal.onClickAnimal(Button.this);
		}
	}
}
