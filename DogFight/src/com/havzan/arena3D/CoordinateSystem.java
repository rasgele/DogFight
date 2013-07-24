package com.havzan.arena3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class CoordinateSystem {
	private int division = 8;
	private float depth;
	private float height;
	private float width;	
	
	private Mesh mesh;
	private Model model;
	private ModelInstance ms;
	
	private float size = 1;
	
	

	public CoordinateSystem create()
	{
		MeshBuilder mb = new MeshBuilder();
		
		mb.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3, "a_pos"),
				new VertexAttribute(Usage.Color, 4, "a_col")), GL10.GL_LINES);
		
		VertexInfo v0 = new VertexInfo();
		VertexInfo v1 = new VertexInfo();
		
		v0.setPos(new Vector3(0,0,0));
		
		v0.setCol(Color.RED);
		
		v1.setPos(new Vector3(size, 0, 0));
		v1.setCol(Color.RED);
		
		mb.line(v0, v1);
		
		v0.setCol(Color.GREEN);
		
		v1.setPos(new Vector3(0, size, 0));
		v1.setCol(Color.GREEN);
		
		mb.line(v0, v1);
		
		v0.setCol(Color.BLUE);
		
		v1.setPos(new Vector3(0, 0, size));
		v1.setCol(Color.BLUE);
		
		mb.line(v0, v1);
		
		mesh = mb.end();
		
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		modelBuilder.part("cs", mesh, GL10.GL_LINES, new Material());
		model = modelBuilder.end();
		
		ms = new ModelInstance(model);
		
		return this;
		
	}
	
	public void render(ModelBatch batch)
	{
		batch.render(ms);
		//mesh.render(GL10.GL_LINES);
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}
}
