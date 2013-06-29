package com.shigengyu.hyperion.models;

import javax.persistence.Column;
import javax.persistence.Id;

import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "COURSE")
public class Course {
	private int courseID;
	private String courseName;

	public Course() {
		super();
	}

	public Course(final String courseName) {
		super();
		this.courseName = courseName;
	}

	@Id
	@Column(name = "COURSE_ID")
	public int getCourseID() {
		return courseID;
	}

	@Column(name = "NAME")
	public String getCourseName() {
		return courseName;
	}

	public void setCourseID(final int courseID) {
		this.courseID = courseID;
	}

	public void setCourseName(final String courseName) {
		this.courseName = courseName;
	}
}