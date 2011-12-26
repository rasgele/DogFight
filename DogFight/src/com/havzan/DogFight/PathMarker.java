package com.test.myfirsttriangle;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.math.Vector3;

public class PathMarker {
	LinkedList<Vector3> m_positions = new LinkedList<Vector3>();
	int m_maxPositions = 50;
	float m_minDistance = 3.0f;
	Vector3 m_lastPos = null;
	
	Mesh m_mesh;
	Texture m_texture;
	Color mColor;
	
	ImmediateModeRenderer m_immediateRenderer;
	
	PathMarker()
	{
		Random r = new Random();
		mColor = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
	}
	
	void create()
	{
		m_mesh = ModelLoader.loadObj(Gdx.files.internal("data/sphere.obj").read());
		Gdx.app.log("ObjTest", "obj bounds: " + m_mesh.calculateBoundingBox());
		m_texture = new Texture(Gdx.files.internal("data/red.png"), true);
		m_texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		m_immediateRenderer = new ImmediateModeRenderer();
	}
	
	void addLocation(Vector3 vec)
	{
		if (m_lastPos != null)
		{
			Vector3 delta = vec.cpy().sub(m_lastPos);
			
			if (delta.len2() < m_minDistance * m_minDistance)
			{
				return;
			}
		}
		if (m_positions.size() == m_maxPositions)
		{
			m_positions.poll();
		}
		m_positions.add(vec.cpy());
		
		m_lastPos = vec.cpy();
	}
	
	void Render()
	{
		GL10 gl = Gdx.graphics.getGL10();

		gl.glDisable(GL10.GL_TEXTURE_2D);
		m_texture.bind();
		gl.glColor4f(mColor.r, mColor.g, mColor.b, mColor.a);
		for (Vector3 pos : m_positions)
		{
			gl.glPushMatrix();
			gl.glTranslatef(pos.x, pos.y, pos.z);			
			gl.glScalef(.2f, .2f, .2f);
			
			m_mesh.render(GL10.GL_TRIANGLES);
			
			gl.glPopMatrix();
		}

		gl.glColor4f(1,1,1,0);
		
		m_immediateRenderer.begin(GL10.GL_LINES);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_LIGHTING);
		
		Vector3 previousPos = null;
		
		for (Vector3 pos : m_positions)
		{
			m_immediateRenderer.color(1, 1, 1, 1);
			m_immediateRenderer.vertex(pos.x, pos.y, pos.z);
			m_immediateRenderer.vertex(0, pos.y, pos.z);
			
			if (previousPos != null)
			{
				m_immediateRenderer.color(1,1,1,1);
				m_immediateRenderer.vertex(previousPos.x, previousPos.y, previousPos.z);
				m_immediateRenderer.vertex(pos.x, pos.y, pos.z);
			}
			
			previousPos = pos;
		}
		m_immediateRenderer.end();
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}
}
