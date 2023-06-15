package com.mindlease.fa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_electric_error")
public class ElectricError implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ELEE_ID")
	private Long id;
	
	@Column(name = "ELEE_TEXT")
	private String name;
	
	@Column(name = "ELEE_TEXT_DE")
	private String nameDe;
	
	@Column(name = "EXAM")
	private String exam;
}
