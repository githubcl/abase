package cn.edu.cqut.base.domain;

/**
 * 联系人
 *
 */
public class Contract
{
	private Long id;
	private String name;
	private String phone;

	public Contract(String name, String phone)
	{
		this.name = name;
		this.phone = phone;
	}
	
	public Contract()
	{
		
	}

	
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	@Override
	public String toString()
	{
		return "Contract [name=" + name + ", phone=" + phone + "]";
	}
}
