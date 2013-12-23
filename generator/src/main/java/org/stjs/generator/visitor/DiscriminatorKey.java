package org.stjs.generator.visitor;

public final class DiscriminatorKey {
	private final String id;
	private final Object value;

	private DiscriminatorKey(String id, Object value) {
		this.id = id;
		this.value = value;
	}

	public static DiscriminatorKey of(String id, Object value) {
		return new DiscriminatorKey(id, value);
	}

	public String getId() {
		return id;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DiscriminatorKey other = (DiscriminatorKey) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

}
