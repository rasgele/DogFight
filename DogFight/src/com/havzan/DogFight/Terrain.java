package com.havzan.DogFight;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.Vector3;

public class Terrain {
	Mesh mesh;
	final int mWidth;
	final int mHeight;
	final int mNumVertX;
	final int mNumVertY;
	final float[][] mHeightMap;
	final float mStartX;
	final float mStartY;

	final float mScaleX;
	final float mScaleY;
	final float mScaleZ;

	float[] mVertices;
	short[] mIndices;
	private int mVertexSize;

	ImmediateModeRenderer10 renderer = new ImmediateModeRenderer10();
	ArrayList<Vector3> mVertexNormals = new ArrayList<Vector3>();

	Vector3 getVertex(int index) {
		return new Vector3(mVertices[index * mVertexSize], mVertices[index
				* mVertexSize + 1], mVertices[index * mVertexSize + 2]);
	}

	public Terrain(int width, int height, float scaleX, float scaleY,
			float scaleZ) {
		mWidth = width;
		mHeight = height;
		mNumVertX = width + 1;
		mNumVertY = width + 1;
		mHeightMap = new float[mNumVertX][mNumVertY];
		mScaleX = scaleX;
		mScaleY = scaleY;
		mScaleZ = scaleZ;
		mStartX = -width / 2;
		mStartY = -height / 2;
	}

	public void create() {
		mesh = new Mesh(false, mNumVertX * mNumVertY, mWidth * mHeight * 2 * 3,
				new VertexAttribute(Usage.Position, 3, "a_pos")
				/* , new VertexAttribute(Usage.ColorPacked, 4, "a_col") */
				, new VertexAttribute(Usage.TextureCoordinates, 2, "a_tex"),
				new VertexAttribute(Usage.Normal, 3, "a_nor"));

		mVertexSize = mesh.getVertexSize() / 4;

		mVertices = new float[mNumVertX * mNumVertY * mVertexSize];
		mIndices = new short[mWidth * mHeight * 2 * 3];

		fillHeights(1.0, 1);

		float step = 1f / mNumVertX;

		ArrayList<ArrayList<Vector3>> vertexToFaceNorm = new ArrayList<ArrayList<Vector3>>();

		int bufferIndex = 0;
		for (int i = 0; i < mNumVertY; i++) {
			for (int j = 0; j < mNumVertX; j++) {

				mVertices[bufferIndex] = mScaleX * (mStartX + j); // set x
				mVertices[bufferIndex + 1] = mScaleY * (mStartY + i); // set x
				mVertices[bufferIndex + 2] = mScaleZ * (mHeightMap[i][j]); // set
																			// z

				mVertices[bufferIndex + 3] = j * step; // u
				mVertices[bufferIndex + 4] = i * step;// v
				// mVertices[vertexNumber + 3] =
				// Color.toFloatBits(r.nextFloat(), r.nextFloat(),
				// r.nextFloat(), 1f);
				vertexToFaceNorm.add(new ArrayList<Vector3>());
				bufferIndex += mVertexSize;
			}
		}

		int triangleCount = 0;
		for (short i = 0; i < mHeight; i++) {
			for (short j = 0; j < mWidth; j++) {
				// v0---v1
				// | \ |
				// v2---v3
				short v0 = (short) (j + (i * mNumVertX));
				short v1 = (short) (j + 1 + (i * mNumVertX));
				short v2 = (short) (j + ((i + 1) * mNumVertX));
				short v3 = (short) (j + 1 + ((i + 1) * mNumVertX));

				mIndices[triangleCount * 3] = v0; // set v0
				mIndices[triangleCount * 3 + 1] = v1; // set v2
				mIndices[triangleCount * 3 + 2] = v3; // set v3

				Vector3 normal = calculateNormal(v0, v1, v3);
				ArrayList<Vector3> vec = vertexToFaceNorm.get(v0);
				vec.add(normal);
				vec = vertexToFaceNorm.get(v1);
				vec.add(normal);
				vec = vertexToFaceNorm.get(v3);
				vec.add(normal);

				triangleCount++;

				mIndices[triangleCount * 3] = v0; // set v0
				mIndices[triangleCount * 3 + 1] = v3; // set v3
				mIndices[triangleCount * 3 + 2] = v2; // set v1

				normal = calculateNormal(v0, v3, v2);

				vertexToFaceNorm.get(v0).add(normal);
				vertexToFaceNorm.get(v3).add(normal);
				vertexToFaceNorm.get(v2).add(normal);

				triangleCount++;
			}
		}

		for (int i = 0; i < vertexToFaceNorm.size(); i++) {

			ArrayList<Vector3> faceNorms = vertexToFaceNorm.get(i);

			Vector3 sum = new Vector3();
			for (Vector3 norm : faceNorms) {
				sum.add(norm);
			}
			sum.nor();

			mVertexNormals.add(sum);

			mVertices[i * mVertexSize + 5] = sum.x;
			mVertices[i * mVertexSize + 6] = sum.y;
			mVertices[i * mVertexSize + 7] = sum.z;
		}

		mesh.setVertices(mVertices);
		mesh.setIndices(mIndices);

	}

	private Vector3 calculateNormal(short v0, short v1, short v2) {
		Vector3 pnt0 = new Vector3();
		Vector3 pnt1 = new Vector3();
		Vector3 pnt2 = new Vector3();
		pnt0.x = mVertices[v0 * mVertexSize];
		pnt0.y = mVertices[v0 * mVertexSize + 1];
		pnt0.z = mVertices[v0 * mVertexSize + 2];

		pnt1.x = mVertices[v1 * mVertexSize];
		pnt1.y = mVertices[v1 * mVertexSize + 1];
		pnt1.z = mVertices[v1 * mVertexSize + 2];

		pnt2.x = mVertices[v2 * mVertexSize];
		pnt2.y = mVertices[v2 * mVertexSize + 1];
		pnt2.z = mVertices[v2 * mVertexSize + 2];

		Vector3 vec01 = pnt1.sub(pnt0).nor();
		Vector3 vec02 = pnt2.sub(pnt0).nor();

		vec01.crs(vec02);

		return vec01;
	}

	void fillHeights(double seed, double h) {
		// size of grid to generate, note this must be a
		// value 2^n+1
		final int DATA_SIZE = mNumVertX;
		// an initial seed value for the corners of the data
		final float SEED = (float) seed;
		float[][] data = mHeightMap;
		// seed the data
		data[0][0] = data[0][DATA_SIZE - 1] = data[DATA_SIZE - 1][0] = data[DATA_SIZE - 1][DATA_SIZE - 1] = SEED;

		Random r = new Random();// for the new value in range of h
		// side length is distance of a single square side
		// or distance of diagonal in diamond
		for (int sideLength = DATA_SIZE - 1;
		// side length must be >= 2 so we always have
		// a new value (if its 1 we overwrite existing values
		// on the last iteration)
		sideLength >= 2;
		// each iteration we are looking at smaller squares
		// diamonds, and we decrease the variation of the offset
		sideLength /= 2, h /= 2.0) {
			// half the length of the side of a square
			// or distance from diamond center to one corner
			// (just to make calcs below a little clearer)
			int halfSide = sideLength / 2;

			// generate the new square values
			for (int x = 0; x < DATA_SIZE - 1; x += sideLength) {
				for (int y = 0; y < DATA_SIZE - 1; y += sideLength) {
					// x, y is upper left corner of square
					// calculate average of existing corners
					double avg = data[x][y] + // top left
							data[x + sideLength][y] + // top right
							data[x][y + sideLength] + // lower left
							data[x + sideLength][y + sideLength];// lower right
					avg /= 4.0;

					// center is average plus random offset
					data[x + halfSide][y + halfSide] = (float) (// We calculate
																// random value
																// in range of
																// 2h
					// and then subtract h so the end value is
					// in the range (-h, +h)
					avg + (r.nextDouble() * 2 * h) - h);
				}
			}

			// generate the diamond values
			// since the diamonds are staggered we only move x
			// by half side
			// NOTE: if the data shouldn't wrap then x < DATA_SIZE
			// to generate the far edge values
			for (int x = 0; x < DATA_SIZE - 1; x += halfSide) {
				// and y is x offset by half a side, but moved by
				// the full side length
				// NOTE: if the data shouldn't wrap then y < DATA_SIZE
				// to generate the far edge values
				for (int y = (x + halfSide) % sideLength; y < DATA_SIZE - 1; y += sideLength) {
					// x, y is center of diamond
					// note we must use mod and add DATA_SIZE for subtraction
					// so that we can wrap around the array to find the corners
					float avg = data[(x - halfSide + DATA_SIZE) % DATA_SIZE][y]
							+ // left of center
							data[(x + halfSide) % DATA_SIZE][y] + // right of
																	// center
							data[x][(y + halfSide) % DATA_SIZE] + // below
																	// center
							data[x][(y - halfSide + DATA_SIZE) % DATA_SIZE]; // above
																				// center
					avg /= 4.0;

					// new value = average plus random offset
					// We calculate random value in range of 2h
					// and then subtract h so the end value is
					// in the range (-h, +h)
					avg = (float) (avg + (r.nextDouble() * 2 * h) - h);
					// update value for center of diamond
					data[x][y] = avg;

					// wrap values on the edges, remove
					// this and adjust loop condition above
					// for non-wrapping values.
					if (x == 0)
						data[DATA_SIZE - 1][y] = avg;
					if (y == 0)
						data[x][DATA_SIZE - 1] = avg;
				}
			}
		}

		// print out the data
		for (float[] row : data) {
			for (float d : row) {
				System.out.printf("%8.3f ", d);
			}
			System.out.println();
		}
	}
}
