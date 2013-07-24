package com.havzan.arena3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


public class Grid {
	
	private int cellCount = 1000;
	private float cellSize = 1.0f;
	private Color color = Color.LIGHT_GRAY;
	private Mesh mesh;
	private Model model;
	
	public float getGridSize() { return getCellCount() * getCellSize(); }
	
	public void create(){
		create(cellCount, cellSize, color);
	}
	public void create(int cellCount, float cellSize, Color color)
	{
		this.cellCount = cellCount;
		this.cellSize = cellSize;
		this.color = color;
		MeshBuilder mb = new MeshBuilder();

		mb.setColor(color);
		mb.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3, "a_pos"),
				new VertexAttribute(Usage.Color, 4, "a_col"))
		, GL10.GL_LINES);
		

		mb.setColor(color);
		
		Vector3 v0 = new Vector3();
		Vector3 v1 = new Vector3();
		
		
		v0.z = -getGridSize() / 2;
		v1.z = -v0.z;
		
		float x = -getGridSize() / 2;
		
		for (int i = 0; i < cellCount + 1 ; i++)
		{
			v0.x = x;
			v1.x = x;
			mb.line(v0, v1);
			x += cellSize;
		}
		
		v0.x = -getGridSize() / 2;
		v1.x = -v0.x;
		
		float z = -getGridSize() / 2;
		
		for (int i = 0; i < cellCount + 1; i++)
		{
			v0.z = z;
			v1.z = z;
			mb.line(v0, v1);
			z += cellSize;
		}
		
		mesh = mb.end();
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		modelBuilder.part("grid", mesh, GL10.GL_LINES, new Material());
		model = modelBuilder.end();
		
	}
	
	public void render(ModelBatch batch)
	{
		mesh.render(GL10.GL_LINES);
	}

	int getCellCount() {
		return cellCount;
	}

	void setCellCount(int cellCount) {
		this.cellCount = cellCount;
	}

	float getCellSize() {
		return cellSize;
	}

	void setCellSize(float cellSize) {
		this.cellSize = cellSize;
	}

	public Model getModel() {
		return model;
	}
}
