package com.erakk.contas;

public class Transaction {
	private String id;
	private String name;
    private int month;
	private int type;
	private double value;
	
	public Transaction(String id, String name, int type, double value, int month) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.type = type;
		this.value = value;
        this.month = month;
	}
	public String getId(){
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName () {
		return this.name;
	}
	
	public void setType (int type) {
		this.type = type;
	}
	public int getType () {
		return this.type;
	}
	
	public void setValue (double value) {
		this.value = value;
	}
	public double getValue () {
		return this.value;
	}

    public void setMonth (int month) {
        this.month = month;
    }
    public double getMonth () {
        return this.month;
    }
	
}