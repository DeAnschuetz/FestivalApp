package com.ffb.model.objects.credit;

import java.util.UUID;

public class Credit {

	private UUID id;
	private double ammount;

	public Credit(UUID id, double ammount) {
		super();
		this.id = id;
		this.ammount = ammount;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public double getAmmount() {
		return ammount;
	}

	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}

	@Override
	public String toString() {
		return "Credit [id=" + id + ", ammount=" + ammount + "]";
	}

}
