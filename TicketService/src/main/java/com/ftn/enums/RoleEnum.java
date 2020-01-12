package com.ftn.enums;

public enum RoleEnum {
	
	ADMIN(1), USER(2);

    private int value;

    private RoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
