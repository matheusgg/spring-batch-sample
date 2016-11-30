package br.com.sample.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	private String firstName;
	private String lastName;

}
