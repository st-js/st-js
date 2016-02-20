package org.stjs.generator.writer.typedarrays;

public class MultiF32AInit1 {
	public void testInit() {
		float[][][] arr = 
			{ 
				{ 
					{ 1.2f, 2f, 3, .4f, a() }, 
					{}, 
					new float[1], 
					myarray() 
				}, 
				{} 
			};
	}

	private float[] myarray() {
		return new float[3];
	}

	public int a() {
		return 42;
	}

}
