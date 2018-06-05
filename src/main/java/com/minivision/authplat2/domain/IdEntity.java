package com.minivision.authplat2.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Setter
@Getter
@ToString
public abstract class IdEntity implements Serializable {
	
	private static final long serialVersionUID = -3934786521557080589L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;

}
