package com.test.myfirsttriangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLU;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g3d.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class SkyBox 
{
	private Mesh m_mesh;
	private Texture m_texture;
	private Vector3 m_camDirection;
	void create()
	{
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		m_mesh = ModelLoader.loadObj(Gdx.files.internal("data/sky.obj").read());
		Gdx.app.log("ObjTest", "obj bounds: " + m_mesh.calculateBoundingBox());
		m_texture = new Texture(Gdx.files.internal("data/skybox.png"), true);
		
		m_texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		m_texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
	}
	
	void setCameraDirection(Vector3 vector)
	{
		m_camDirection = vector;
	}
	
	void render(PerspectiveCamera cam)
	{
		GL10 gl = Gdx.graphics.getGL10();
		gl.glPushMatrix();
		
		Vector3 oldPos = cam.getPosition().cpy();
		cam.getPosition().set(0, 0, 0);
		cam.update();
		cam.setMatrices();
		
		//Matrix4 m = new Matrix4(); 
		//m.setToLookAt(new Vector3(), m_camDirection, new Vector3(1,0,0));
		//gl.glLoadIdentity();
		//gl.glMultMatrixf(m.val, 0);
		
		gl.glScalef(50, 50, 50);
		
	    gl.glDisable(GL10.GL_DEPTH_TEST);
	    gl.glDisable(GL10.GL_LIGHTING);
	    gl.glDisable(GL10.GL_BLEND);
	    
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	    
	    m_texture.bind();
	    m_mesh.render(GL10.GL_TRIANGLES);
	    
	    gl.glEnable(GL10.GL_DEPTH_TEST);
	    gl.glEnable(GL10.GL_LIGHTING);
	    gl.glEnable(GL10.GL_BLEND);
		
	    cam.getPosition().set(oldPos);
		gl.glPopMatrix();
	}
}
