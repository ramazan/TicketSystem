package com.j32bit.ticket.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "jaxbString")
public class StringKeyValuePer {

	private String value;

	public StringKeyValuePer() {
	}

	public StringKeyValuePer(String v) {
		this.value = v;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}