package cn.edu.cqut.base.domain;

import cn.edu.cqut.base.adapter.ItemAnnotation;
import cn.edu.cqut.base.adapter.ItemType;

public class Item
{
	private String image;
	private int item1;
	private String item2;
	private String item3;
	private String item4;

	public Item(String image, int item1, String item2, String item3,
			String item4)
	{
		this.image = image;
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
		this.item4 = item4;
	}

	@ItemAnnotation(types =
	{ ItemType.Image })
	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	@ItemAnnotation(types =
	{ ItemType.Item1 }, adapters =
	{ "123" })
	public int getItem1()
	{
		return item1;
	}

	public void setItem1(int item1)
	{
		this.item1 = item1;
	}

	@ItemAnnotation(types =
	{ ItemType.Item2 }, adapters =
	{ "123" })
	public String getItem2()
	{
		return item2;
	}

	public void setItem2(String item2)
	{
		this.item2 = item2;
	}

	@ItemAnnotation(types =
	{ ItemType.Item3 }, adapters =
	{ "123" })
	public String getItem3()
	{
		return item3;
	}

	public void setItem3(String item3)
	{
		this.item3 = item3;
	}

	@ItemAnnotation(types =
	{ ItemType.Item4 }, adapters =
	{ "123" })
	public String getItem4()
	{
		return item4;
	}

	public void setItem4(String item4)
	{
		this.item4 = item4;
	}
}
