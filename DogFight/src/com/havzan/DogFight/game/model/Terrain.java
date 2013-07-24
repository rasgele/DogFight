package com.havzan.dogfight.game.model;

public class Terrain {
	public final int sizeX;
	public final int sizeY;
	public final float[][] mHeightMap;
	
	public Terrain(int sizeX, int sizeY)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		mHeightMap = new float [sizeX][sizeY];
	}
}
