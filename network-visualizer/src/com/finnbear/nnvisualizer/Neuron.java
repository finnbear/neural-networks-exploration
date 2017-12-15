package com.finnbear.nnvisualizer;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.geom.Point2D;

public class Neuron {
	private String _name;
	private Float _value;
	private Float _bias;
	private Point2D _position;
	private double _radius;
	
	public Neuron(String name, Float value) {
		_name = name;
		_value = value;
	}

	public Neuron(String name, Float value, Float bias) {
		_name = name;
		_value = value;
		_bias = bias;
	}
	
	public String getNameString() {
		return "\"" + _name + "\"";
	}
	
	public String getValueString() {
		if (_value == null) {
			return "";
		} else {
			return _value.toString();
		}
	}
	
	public Point2D getPosition() {
		return _position;
	}
	
	public void setPosition(Point2D position) {
		_position = position;
	}

	public Float getBias() {
		return _bias;
	}
	
	public double getRadius() {
		return _radius;
	}
	
	public void setRadius(double radius) {
		_radius = radius;
	}
}
