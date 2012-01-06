package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class SkyBox {
	Mesh m_mesh;
	private Texture m_texture;

	public void create() {
		m_mesh = Assets.getAsset("data/sky2.obj", Mesh.class);
		Gdx.app.log("ObjTest", "obj bounds: " + m_mesh.calculateBoundingBox());

		m_texture = Assets.getAsset("data/sky2.png", Texture.class);

		m_texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
		m_texture.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
	}

	// void create()
	// {
	// Gdx.app.log("ObjTest", "obj bounds: " + m_mesh.calculateBoundingBox());
	// m_texture = new Texture(Gdx.files.internal("data/skybox.png"), true);
	//
	// m_texture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
	// m_texture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
	// }

	void setCameraDirection(Vector3 vector) {
	}

	void render(PerspectiveCamera cam) {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glPushMatrix();

		// gl.glLoadIdentity();

		// Vector3 oldPos = cam.position.cpy();
		// cam.position.set(0, 0, 0);
		// cam.update();
		// cam.apply(gl);

		// Matrix4 m = new Matrix4();
		// m.setToLookAt(new Vector3(), m_camDirection, new Vector3(1,0,0));
		// gl.glLoadIdentity();
		// gl.glMultMatrixf(m.val, 0);

		Matrix4 m = new Matrix4();
		m.translate(cam.position.x, cam.position.y, cam.position.z).scale(2000,
				2000, 2000);

		gl.glMultMatrixf(m.val, 0);

		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_BLEND);
		// gl.glDisable(GL10.GL_CULL_FACE);
		// gl.glEnable(GL10.GL_TEXTURE_2D);

		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				TextureWrap.ClampToEdge.getGLEnum());
		Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				TextureWrap.ClampToEdge.getGLEnum());

		m_texture.bind();
		m_mesh.render(GL10.GL_TRIANGLES);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_BLEND);
		// cam.position.set(oldPos);
		gl.glPopMatrix();
	}
}
