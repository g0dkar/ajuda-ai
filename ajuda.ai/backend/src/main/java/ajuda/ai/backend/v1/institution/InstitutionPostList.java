package ajuda.ai.backend.v1.institution;

import java.util.List;

import ajuda.ai.model.institution.InstitutionPost;

public class InstitutionPostList {
	private int total;
	private int offset;
	private int pageSize;
	private List<InstitutionPost> posts;

	public int getTotal() {
		return total;
	}

	public void setTotal(final int total) {
		this.total = total;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(final int offset) {
		this.offset = offset;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	public List<InstitutionPost> getPosts() {
		return posts;
	}

	public void setPosts(final List<InstitutionPost> posts) {
		this.posts = posts;
	}
}
