package com.andbase.demo.model;

import java.util.List;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;
@Table(name = "local_user")
public class LocalUser {

	// ID @Id����,int����,���ݿ⽨��ʱ���ֶλ���Ϊ������
	@Id
	@Column(name = "_id")
	private int _id;
	
	@Column(name = "u_id")
	private String uId;

	// ��¼�û��� length=20�����ֶεĳ�����20
	@Column(name = "name", length = 20)
	private String name;

	// �û�����
	@Column(name = "password")
	private String password;

	// ����һ������ֵ,��type = "INTEGER"�淶һ��.
	@Column(name = "age", type = "INTEGER")
	private int age;

	// ����ʱ��
	@Column(name = "create_time")
	private String createTime;
	
	// ����ʵ��Ĵ洢��ָ�����
	@Relations(name="stock",type="one2one",foreignKey = "u_id",action="query_insert")
	private Stock stock;

	// ����List�Ĵ洢��ָ�����
	@Relations(name="stocks",type="one2many",foreignKey = "u_id",action="query_insert")
	private List<Stock> stocks;
	
	// ��Щ�ֶ������ܲ�ϣ�����浽���ݿ���,����@Columnע�;Ͳ���ӳ�䵽���ݿ�.
	private String remark;


	public int get_id() {
		return _id;
	}


	public void set_id(int _id) {
		this._id = _id;
	}


	public String getuId() {
		return uId;
	}


	public void setuId(String uId) {
		this.uId = uId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public List<Stock> getStocks() {
		return stocks;
	}


	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Stock getStock() {
		return stock;
	}


	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
