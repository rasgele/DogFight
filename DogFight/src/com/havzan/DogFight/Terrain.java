package com.havzan.DogFight;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class Terrain {
	Mesh mesh;
	final int mWidth;
	final int mHeight;
	final int mNumVertX;
	final int mNumVertY;
	final float[][] mHeightMap;
	final float mScale;
	final float mStartX;
	final float mStartY;

	float[] mVertices;
	short[] mIndices;

	public Terrain(int width, int height, float scale) {
		mWidth = width;
		mHeight = height;
		mNumVertX = width + 1;
		mNumVertY = width + 1;
		mHeightMap = new float[mNumVertX][mNumVertY];
		mScale = scale;
		mStartX = -width / 2;
		mStartY = -height / 2;
	}

	public void create() {
		mesh = new Mesh(false, mNumVertX * mNumVertY, mWidth * mHeight * 2 * 3,
				new VertexAttribute(Usage.Position, 3, "a_pos"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_tex"));

		int vertexSize = mesh.getVertexSize() / 4;

		mVertices = new float[mNumVertX * mNumVertY * vertexSize];
		mIndices = new short[mWidth * mHeight * 2 * 3];

		int vertexNumber = 0;
		for (int i = 0; i < mNumVertY; i++) {
			for (int j = 0; j < mNumVertX; j++) {

				mVertices[vertexNumber] = mStartX + j; // set x
				mVertices[vertexNumber + 1] = mStartY + i; // set x
				mVertices[vertexNumber + 2] = 0; // set z flat
				mVertices[vertexNumber + 3] = 0; // u
				mVertices[vertexNumber + 4] = 1;// v

				vertexNumber += vertexSize;
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
				mIndices[triangleCount * 3 + 1] = v2; // set v2
				mIndices[triangleCount * 3 + 2] = v3; // set v3

				triangleCount++;

				mIndices[triangleCount * 3] = v0; // set v0
				mIndices[triangleCount * 3 + 1] = v3; // set v3
				mIndices[triangleCount * 3 + 2] = v1; // set v1

				triangleCount++;
			}
		}

		mesh.setVertices(mVertices);
		mesh.setIndices(mIndices);

	}
}
