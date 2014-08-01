package io.yunba.yo.bean;

public class ItemBean {
	public String value;
	public int show = 0;
	public int num = 0;
	
	public ItemBean(String value, int show, int num) {

		this.value = value;
		this.show = show;
		this.num = num;
	}

	public ItemBean(String value) {
		this.value = value;
	}

}
