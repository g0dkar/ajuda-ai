package ajuda.ai.backend.v1.user;

import java.util.List;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.institution.InstitutionPost;

public class UserDashboardData {
	private int donations;
	private int institutions;
	private int value;
	private int meanValue;
	private List<InstitutionPost> posts;
	private List<Payment> payments;

	public int getDonations() {
		return donations;
	}

	public void setDonations(final int donations) {
		this.donations = donations;
	}

	public int getInstitutions() {
		return institutions;
	}

	public void setInstitutions(final int institutions) {
		this.institutions = institutions;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public int getMeanValue() {
		return meanValue;
	}

	public void setMeanValue(final int meanValue) {
		this.meanValue = meanValue;
	}

	public List<InstitutionPost> getPosts() {
		return posts;
	}

	public void setPosts(final List<InstitutionPost> posts) {
		this.posts = posts;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(final List<Payment> payments) {
		this.payments = payments;
	}
}
