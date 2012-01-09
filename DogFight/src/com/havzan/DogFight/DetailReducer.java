package com.havzan.DogFight;

public class DetailReducer {
	public DetailReducer(int reduceRate) {

	}

	public static float[][] reduceBy(int rate, float[][] data) {
		final int size = data.length;
		final int jumpStep = 1 << rate;
		final int reducedSize = (size - 1) / jumpStep + 1;
		float[][] reducedData = new float[reducedSize][reducedSize];

		for (int i = 0; i < reducedSize; i++) {
			for (int j = 0; j < reducedSize; j++) {
				reducedData[i][j] = data[i * jumpStep][j * jumpStep];
			}
		}

		return null;
	}
}
