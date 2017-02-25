package ajuda.ai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Guarda configurações no Banco de Dados
 * 
 * @author Rafael Lins
 *
 */
@Entity
public class Configuration {
	@Id @Column(length = 64)
	private String name;
	
	@Column(length = 1024)
	private String value;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
}
