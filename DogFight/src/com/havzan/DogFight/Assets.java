package com.havzan.DogFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.utils.Array;

public class Assets {

	public static class MeshLoader extends AsynchronousAssetLoader<Mesh, MeshLoader.MeshLoaderParams>
	{
		public MeshLoader(FileHandleResolver resolver) {
			super(resolver);
			// TODO Auto-generated constructor stub
		}

		public static class MeshLoaderParams extends AssetLoaderParameters<Mesh> {

		}

		@Override
		public void loadAsync(AssetManager manager, String fileName,
				MeshLoaderParams parameter) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Mesh loadSync(AssetManager manager, String fileName,
				MeshLoaderParams parameter) {
			return ObjLoader.loadObj(Gdx.files.internal(fileName)
					.read());
		}

		@Override
		public Array<AssetDescriptor> getDependencies(String fileName,
				MeshLoaderParams parameter) {
			// TODO Auto-generated method stub
			return null;
		}		
	}
	
	private static AssetManager mAssetManager;

	public static void load() {
		mAssetManager = new AssetManager();

		mAssetManager.setErrorListener(new AssetErrorListener() {
			@Override
			public void error(String fileName, Class type, Throwable throwable) {
				Gdx.app.log("AssetListener", "FAILED TO LOAD : " + fileName);
			}
		});
		
		mAssetManager.setLoader(Mesh.class, new MeshLoader(new InternalFileHandleResolver()));

		TextureParameter para = new TextureLoader.TextureParameter();
		para.genMipMaps = true;

		mAssetManager.load("data/camo.jpg", Texture.class, para);
		mAssetManager.load("data/checkerBigg.jpg", Texture.class, para);

		para = new TextureLoader.TextureParameter();
		para.genMipMaps = true;
		mAssetManager.load("data/grass-texture.jpg", Texture.class, para);

		para = new TextureLoader.TextureParameter();
		para.genMipMaps = true;
		mAssetManager.load("data/greenchecker.png", Texture.class, para);
		mAssetManager.load("data/marker.png", Texture.class);

		para = new TextureLoader.TextureParameter();
		para.genMipMaps = true;
		mAssetManager.load("data/skybox.png", Texture.class, para);
		
		mAssetManager.load("data/triangle.png", Texture.class);
		mAssetManager.load("data/locked.png", Texture.class);

		mAssetManager.load("data/ui/fire.png", Texture.class);
		mAssetManager.load("data/ui/firePressed.png", Texture.class);
		mAssetManager.load("data/ui/radar.png", Texture.class);
		mAssetManager.load("data/ui/red.png", Texture.class);
		mAssetManager.load("data/ui/sliderback.png", Texture.class);
		mAssetManager.load("data/ui/sliderHandle.png", Texture.class);
		mAssetManager.load("data/ui/switchCam.png", Texture.class);
		mAssetManager.load("data/ui/switchCamPressed.png", Texture.class);
		mAssetManager.load("data/ui/togMarkerPressed.png", Texture.class);
		mAssetManager.load("data/ui/togMarker.png", Texture.class);
		
		mAssetManager.load("data/grass.obj", Mesh.class);
		mAssetManager.load("data/missile.obj", Mesh.class);
		mAssetManager.load("data/grid.obj", Mesh.class);
		mAssetManager.load("data/gridHills.obj", Mesh.class);
		mAssetManager.load("data/marker.obj", Mesh.class);
		mAssetManager.load("data/f15.obj", Mesh.class);
		mAssetManager.load("data/sky.obj", Mesh.class);
		mAssetManager.load("data/sphere.obj", Mesh.class);
		mAssetManager.load("data/xyzplaneZ.obj", Mesh.class);		

		mAssetManager.finishLoading();

		Texture.setAssetManager(mAssetManager);
	}
	
	public static <T> T getAsset(String name, Class<T> type){
		return mAssetManager.get(name, type);
	}
	
	public static Mesh getAircraftModel(){
		return mAssetManager.get("data/f15.obj", Mesh.class);
	}
	public static Texture getAircraftTexture(){
		return mAssetManager.get("data/camo.jpg", Texture.class);
	}
	
	public static Mesh getMissileModel(){
		return mAssetManager.get("data/missile.obj", Mesh.class);
	}
	public static Texture getMissileTexture(){
		return mAssetManager.get("data/camo.jpg", Texture.class);
	}
	
	public static Mesh getTerrainModel(){
		return mAssetManager.get("data/gridHills.obj", Mesh.class);		
	}
	
	public static Texture getTerrainTexture(){
		return mAssetManager.get("data/greenchecker.png", Texture.class);
	}
//
//	public static Texture getTexture(String path) {
//		return mAssetManager.get(path, Texture.class);
//	}
//
//	public static Mesh getObjMesh(String path) {
//		return null;
//	}

}
