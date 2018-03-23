package com.orientdata.lookforcustomers.network.okhttp.bean;

/**
 * 可根据实际情况进行调整
 */
public class OkError {
	private int status;
	private String msg;

	public OkError() {
	}
	public OkError(String msg){
		this.msg = msg;
	}
	public OkError(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
