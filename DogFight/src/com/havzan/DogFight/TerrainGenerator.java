package com.havzan.dogfight;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;

public class TerrainGenerator {
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

	final int mTriangleCount;
	final int mVertexCount;
	final int mIndiceCount;

	short[] mIndices;
	private final int mVertexSize;
	private Vector3[] mVertexPositions;
	Plane[] mTriangles;
	ArrayList<ArrayList<Plane>> vertexToFace = new ArrayList<ArrayList<Plane>>();

	private boolean mIsGeoCreated = false;
	private boolean mIsCollisionDataCreated = false;
	private final VertexAttributes vertexAttributes;
	private final float mHeightDelta;

	public TerrainGenerator(float terrainSize, float heightDelta, int gridWidth) {
		mWidth = gridWidth;
		mHeight = gridWidth;
		mNumVertX = mWidth + 1;
		mNumVertY = mWidth + 1;
		mHeightMap = new float[mNumVertX][mNumVertY];
		mScaleX = terrainSize / gridWidth;
		mScaleY = mScaleX;
		mScaleZ = 1;
		mStartX = -mWidth / 2;
		mStartY = -mWidth / 2;
		mVertexCount = mNumVertX * mNumVertY;
		mTriangleCount = mWidth * mHeight * 2;
		mIndiceCount = mTriangleCount * 3;
		
		mHeightDelta = heightDelta;

		vertexAttributes = new VertexAttributes(new VertexAttribute(
				Usage.Position, 3, "a_pos"), new VertexAttribute(
				Usage.TextureCoordinates, 2, "a_tex"), new VertexAttribute(
				Usage.Normal, 3, "a_nor"));
		// devide by float size(?)
		mVertexSize = vertexAttributes.vertexSize / 4;
	}

	public void createGeometry() {
		createGridData();
		createIndices();

		mIsGeoCreated = true;
	}

	public void createCollisionData() {
		if (!mIsGeoCreated)
			createGeometry();
		
		mTriangles = new Plane[mTriangleCount];

		vertexToFace = new ArrayList<ArrayList<Plane>>(mVertexCount);
		for (int i = 0; i < mVertexCount; i++) {
			vertexToFace.add(new ArrayList<Plane>(6));
		}

		for (int i = 0; i < mTriangleCount; i++) {
			short v0 = mIndices[i * 3];
			short v1 = mIndices[i * 3 + 1];
			short v2 = mIndices[i * 3 + 2];

			Plane p = new Plane(mVertexPositions[v0], mVertexPositions[v1],
					mVertexPositions[v2]);

			mTriangles[i] = p;

			vertexToFace.get(v0).add(p);
			vertexToFace.get(v1).add(p);
			vertexToFace.get(v2).add(p);
		}
	}

	private void createIndices() {
		mIndices = new short[mIndiceCount];

		int triangleIndex = 0;
		for (short i = 0; i < mHeight; i++) {
			for (short j = 0; j < mWidth; j++) {
				// j.th col
				// |
				// V
				// v2---v3
				// | / |
				// v0---v1 <- i.th row
				short v0 = (short) (j + (i * mNumVertX));
				short v1 = (short) (j + 1 + (i * mNumVertX));
				short v2 = (short) (j + ((i + 1) * mNumVertX));
				short v3 = (short) (j + 1 + ((i + 1) * mNumVertX));

				mIndices[triangleIndex * 3] = v0; // set v0
				mIndices[triangleIndex * 3 + 1] = v1; // set v2
				mIndices[triangleIndex * 3 + 2] = v3; // set v3

				triangleIndex++;

				mIndices[triangleIndex * 3] = v0; // set v0
				mIndices[triangleIndex * 3 + 1] = v3; // set v3
				mIndices[triangleIndex * 3 + 2] = v2; // set v1

				triangleIndex++;
			}
		}
	}

	private void createGridData() {
		fillHeights(0, mHeightDelta/2);

		mVertexPositions = new Vector3[mNumVertX * mNumVertY];

		int vertexCount = 0;
		for (int i = 0; i < mNumVertY; i++) {
			for (int j = 0; j < mNumVertX; j++) {
				mVertexPositions[vertexCount++] = new Vector3(mScaleX
						* (mStartX + j), mScaleY * (mStartY + i), mScaleZ
						* (mHeightMap[i][j]));
			}
		}
	}

	private void createTriangleIndiceBuffer() {
		int numOfTrianles = mWidth * mHeight * 2;
		mIndices = new short[numOfTrianles * 3];

		int triangleCount = 0;
		for (short i = 0; i < mHeight; i++) {
			for (short j = 0; j < mWidth; j++) {
				// jth col
				// |
				// V
				// v2---v3
				// | / |
				// v0---v1 <- i.th row
				short v0 = (short) (j + (i * mNumVertX));
				short v1 = (short) (j + 1 + (i * mNumVertX));
				short v2 = (short) (j + ((i + 1) * mNumVertX));
				short v3 = (short) (j + 1 + ((i + 1) * mNumVertX));

				mIndices[triangleCount * 3] = v0; // set v0
				mIndices[triangleCount * 3 + 1] = v1; // set v2
				mIndices[triangleCount * 3 + 2] = v3; // set v3

				Plane p = new Plane(mVertexPositions[v0], mVertexPositions[v1],
						mVertexPositions[v3]);

				mTriangles[triangleCount] = p;
				vertexToFace.get(v0).add(p);
				vertexToFace.get(v1).add(p);
				vertexToFace.get(v3).add(p);

				triangleCount++;

				mIndices[triangleCount * 3] = v0; // set v0
				mIndices[triangleCount * 3 + 1] = v3; // set v3
				mIndices[triangleCount * 3 + 2] = v2; // set v1

				p = new Plane(mVertexPositions[v0], mVertexPositions[v3],
						mVertexPositions[v2]);

				mTriangles[triangleCount] = p;
				vertexToFace.get(v0).add(p);
				vertexToFace.get(v3).add(p);
				vertexToFace.get(v2).add(p);

				triangleCount++;
			}
		}
	}

	public Model createMesh() {
		Mesh mesh = new Mesh(false, mVertexCount, mIndiceCount,
				vertexAttributes);

		if (!mIsCollisionDataCreated)
			createCollisionData();

		float[] verticeBuffer = new float[mNumVertX * mNumVertY * mVertexSize];

		float step = 1f / mNumVertX; // Map whole texture evenly.

		for (int i = 0, vertexIndex = 0; i < mNumVertY; i++) {
			for (int j = 0; j < mNumVertX; j++) {
				int bufferIndex = vertexIndex * mVertexSize;
				verticeBuffer[bufferIndex] = mVertexPositions[vertexIndex].x;
				verticeBuffer[bufferIndex + 1] = mVertexPositions[vertexIndex].y;
				verticeBuffer[bufferIndex + 2] = mVertexPositions[vertexIndex].z;
				verticeBuffer[bufferIndex + 3] = j % 2 == 0 ? 0 : 1;// * step; // u
				verticeBuffer[bufferIndex + 4] = i % 2 == 1 ? 0 : 1;//* step;// v
				verticeBuffer[bufferIndex + 5] = mTriangles[vertexIndex].normal.x;
				verticeBuffer[bufferIndex + 6] = mTriangles[vertexIndex].normal.y;
				verticeBuffer[bufferIndex + 7] = mTriangles[vertexIndex].normal.z;
				vertexIndex++;
			}
		}

		createTriangleIndiceBuffer();

		mesh.setVertices(verticeBuffer);
		mesh.setIndices(mIndices);
		
		ModelBuilder mb =  new ModelBuilder();
		mb.begin();
		MeshPart part = mb.part("terrain", mesh, GL10.GL_TRIANGLES, new Material());
		
		return mb.end();
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
