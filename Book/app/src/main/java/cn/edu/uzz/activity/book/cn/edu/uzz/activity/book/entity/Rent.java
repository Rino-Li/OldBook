package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.entity;

import java.io.Serializable;
import java.sql.Date;

public class Rent implements Serializable{
	private String account;
	private String bookanme;
	private Date nowdate;
	private Date endtime;
	private String picture;
	private int bookid;
	private int booktype;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBookanme() {
		return bookanme;
	}

	public void setBookanme(String bookanme) {
		this.bookanme = bookanme;
	}

	public Date getNowdate() {
		return nowdate;
	}

	public void setNowdate(Date nowdate) {
		this.nowdate = nowdate;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getBookid() {
		return bookid;
	}

	public void setBookid(int bookid) {
		this.bookid = bookid;
	}

	public int getBooktype() {
		return booktype;
	}

	public void setBooktype(int booktype) {
		this.booktype = booktype;
	}
}
