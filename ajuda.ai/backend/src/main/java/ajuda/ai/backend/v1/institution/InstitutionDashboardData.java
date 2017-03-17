package ajuda.ai.backend.v1.institution;

import java.util.List;

import ajuda.ai.model.institution.Institution;

/**
 * POJO para facilitar o envio dos dados do Dashboard do Ajuda.Ai
 * 
 * @author Rafael Lins
 *
 */
public class InstitutionDashboardData {
	private int donations;
	private int value;
	private int helpers;
	private int maxValue;
	private int meanValue;
	private int institutionCount;
	private List<Institution> institutions;
	
	public int getDonations() {
		return donations;
	}

	public void setDonations(final int donations) {
		this.donations = donations;
	}
	
	public void setDonations(final Object value) {
		if (value == null) {
			this.donations = 0;
		}
		else {
			this.donations = ((Number) value).intValue();
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
	
	public void setValue(final Object value) {
		if (value == null) {
			this.value = 0;
		}
		else {
			this.value = ((Number) value).intValue();
		}
	}

	public int getHelpers() {
		return helpers;
	}

	public void setHelpers(final int helpers) {
		this.helpers = helpers;
	}
	
	public void setHelpers(final Object value) {
		if (value == null) {
			this.helpers = 0;
		}
		else {
			this.helpers = ((Number) value).intValue();
		}
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(final int maxValue) {
		this.maxValue = maxValue;
	}
	
	public void setMaxValue(final Object value) {
		if (value == null) {
			this.maxValue = 0;
		}
		else {
			this.maxValue = ((Number) value).intValue();
		}
	}

	public int getMeanValue() {
		return meanValue;
	}

	public void setMeanValue(final int meanValue) {
		this.meanValue = meanValue;
	}
	
	public void setMeanValue(final Object value) {
		if (value == null) {
			this.meanValue = 0;
		}
		else {
			this.meanValue = ((Number) value).intValue();
		}
	}

	public List<Institution> getInstitutions() {
		return institutions;
	}

	public void setInstitutions(final List<Institution> institutions) {
		this.institutions = institutions;
	}

	public int getInstitutionCount() {
		return institutionCount;
	}

	public void setInstitutionCount(final int institutionCount) {
		this.institutionCount = institutionCount;
	}
	
	public void setInstitutionCount(final Object value) {
		if (value == null) {
			this.institutionCount = 0;
		}
		else {
			this.institutionCount = ((Number) value).intValue();
		}
	}
}
