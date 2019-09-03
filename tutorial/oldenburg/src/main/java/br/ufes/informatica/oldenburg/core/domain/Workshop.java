package br.ufes.informatica.oldenburg.core.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.ufes.inf.nemo.jbutler.ejb.persistence.PersistentObjectSupport;

@Entity
public class Workshop extends PersistentObjectSupport implements Comparable<Workshop> {
	private static final long serialVersionUID = 1L;

	@Size(max = 100)
	private String name;

	@Size(max = 10)
	private String acronym;

	@NotNull
	private int year;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date submissionDeadline;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date reviewDeadline;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Date getSubmissionDeadline() {
		return submissionDeadline;
	}

	public void setSubmissionDeadline(Date submissionDeadline) {
		this.submissionDeadline = submissionDeadline;
	}

	public Date getReviewDeadline() {
		return reviewDeadline;
	}

	public void setReviewDeadline(Date reviewDeadline) {
		this.reviewDeadline = reviewDeadline;
	}

	@Override
	public int compareTo(Workshop o) {
		return year - o.year;
	}
}