package com.onlinepowers.demo.domain;

public class Domain {
	private boolean a;
	private boolean[] b;
	
	public boolean isA() {
		return a;
	}
	public void setA(boolean a) {
		this.a = a;
	}
	public boolean[] getB() {
		if (b == null) {
			return null;
		} else {
			int length = b.length;
			boolean[] array = new boolean[length];
			for(int i = 0 ; i < length ; i++) {
				array[i] = b[i];
			}
			return array;
		}
	}
	public void setB(boolean[] b) {
		if (b == null) {
			this.b = null;
		} else {
			int length = b.length;
			this.b = new boolean[length];
			for(int i = 0 ; i < length ; i++) {
				this.b[i] = b[i];
			}
		}
	}
	
	
	
}
