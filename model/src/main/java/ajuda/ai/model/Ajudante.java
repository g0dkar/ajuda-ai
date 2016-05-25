package ajuda.ai.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ajuda.ai.model.extra.CreationInfo;

/**
 * Representa uma pessoa que está ajudando uma {@link Instituicao Instituição}
 * 
 * @author Rafael M. Lins - g0dkar
 *
 */
@Entity
public class Ajudante implements Serializable {
	/** Serial Version ID */
	private static final long serialVersionUID = 4721303739155862814L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	private CreationInfo creation;
}
