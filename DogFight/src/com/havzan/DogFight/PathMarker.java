package com.havzan.DogFight;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class PathMarker {
	LinkedList<Vector3> m_positions = new LinkedList<Vector3>();
	int m_maxPositions = 500;
	float m_minDistance = 3.0f;
	Vector3 m_lastPos = null;
	
	Mesh m_mesh;
	Texture m_texture;
	Color mColor;
	
	ImmediateModeRenderer10 m_immediateRenderer;
	
	PathMarker()
	{
		Random r = new Random();
		mColor = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
	}
	
	void create()
	{
		m_mesh = ObjLoader.loadObj(Gdx.files.internal("data/sphere.obj").read());
		Gdx.app.log("ObjTest", "obj bounds: " + m_mesh.calculateBoundingBox());
		m_texture = new Texture(Gdx.files.internal("data/ui/red.png"), true);
		m_texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		m_immediateRenderer = new ImmediateModeRenderer10();
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
	
	void Render(Matrix4 projection)
	{
		GL10 gl = Gdx.graphics.getGL10();

		gl.glDisable(GL10.GL_TEXTURE_2D);
		//m_texture.bind();
		gl.glColor4f(mColor.r, mColor.g, mColor.b, mColor.a / 4);
		final float finalScale = 3;
		float step = finalScale / m_positions.size();
		int stepCount = 0;
		for (Vector3 pos : m_positions)
		{
			gl.glPushMatrix();
			gl.glTranslatef(pos.x, pos.y, pos.z);			
			gl.glScalef(finalScale - step * stepCount, finalScale - step * stepCount, finalScale - step * stepCount);
			stepCount++;
			m_mesh.render(GL10.GL_TRIANGLES);
			
			gl.glPopMatrix();
		}

		gl.glColor4f(1,1,1,0);

		gl.glDisable(GL10.GL_TEXTURE_2D);
		//gl.glDisable(GL10.GL_LIGHTING);
		
		m_immediateRenderer.begin(GL10.GL_LINES);

		m_immediateRenderer.color(1, 1, 1, 1);
		
		//gl.glPushMatrix();
		//gl.glLoadIdentity();
		Vector3 previousPos = null;
		
		for (Vector3 pos : m_positions)
		{
			m_immediateRenderer.color(1, 1, 1, 1);
			m_immediateRenderer.vertex(pos.x, pos.y, 0);
			m_immediateRenderer.color(1, 1, 1, 1);
			m_immediateRenderer.vertex(pos.x, pos.y, 0);
			
			if (previousPos != null)
			{
				m_immediateRenderer.color(1, 1, 1, 1);
				m_immediateRenderer.vertex(previousPos.x, previousPos.y, previousPos.z);
				m_immediateRenderer.color(1, 1, 1, 1);
				m_immediateRenderer.vertex(pos.x, pos.y, pos.z);
			}
			
			previousPos = pos;
		}
		m_immediateRenderer.end();
		
		gl.glPopMatrix();
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}
}
