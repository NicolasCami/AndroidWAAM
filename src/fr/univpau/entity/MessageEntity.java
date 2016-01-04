package fr.univpau.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.univpau.util.Gender;

// {"time":"2015-12-04 11:45:26","msg":"regardez l'inscription sur l'arr\u00c3\u00aat de bus. Lol","gender":"1","meters":"0.0000","geo":["48.856614","2.352233"]}
public class MessageEntity {
	
	private Date 		_date;
	private String 		_content;
	private Gender 		_gender;
	private double 		_distance;
	private double[]	_location;
	
	public MessageEntity(String date, String content, int gender, double distance, double[] location) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			_date = format.parse(date);
		} catch (ParseException e) {
			System.out.println("Can't parse date !");
		}
		_content = content;
		_gender = Gender.getValue(gender);
		_distance = distance;
		_location = location;
	}

	public Date getDate() {
		return _date;
	}

	public void setDate(Date _date) {
		this._date = _date;
	}

	public String getContent() {
		return _content;
	}

	public void setContent(String _content) {
		this._content = _content;
	}

	public Gender getGender() {
		return _gender;
	}

	public void setGender(Gender _gender) {
		this._gender = _gender;
	}

	public double getDistance() {
		return _distance;
	}

	public void setDistance(double _distance) {
		this._distance = _distance;
	}

	public double[] getLocation() {
		return _location;
	}

	public void setLocation(double[] _location) {
		this._location = _location;
	}
	
	
}
