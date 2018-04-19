package org.stjs.generator.writer.checks;

public class Point2D {
	protected Integer x;
	protected Integer y;

	public Point2D(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Point2D)) {
			return false;
		}

		Point2D other = (Point2D) obj;

		return other.x == x && other.y == y;
	}
}
