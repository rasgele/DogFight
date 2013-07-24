package com.havzan.DogFight;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class Assets {

	private static Model mTerrainMesh = null;

	public static class MeshLoader extends
			AsynchronousAssetLoader<Model, MeshLoader.MeshLoaderParams> {
		public MeshLoader(FileHandleResolver resolver) {
			super(resolver);
			// TODO Auto-generated constructor stub
		}

		public static class MeshLoaderParams extends
				AssetLoaderParameters<Model> {

		}

		@Override
		public void loadAsync(AssetManager manager, String fileName,
				MeshLoaderParams parameter) {
			// TODO Auto-generated method stub

		}

		@Override
		public Model loadSync(AssetManager manager, String fileName,
				MeshLoaderParams parameter) {
			ObjLoader loader;
			loader = new ObjLoader();
			return loader.loadModel(Gdx.files.internal(fileName));
		}

		@Override
		public Array<AssetDescriptor> getDependencies(String fileName,
				MeshLoaderParams parameter) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private static AssetManager mAssetManager;

	private final static String AircraftModelPath = "data/f22.obj";

	public static void load() {
		mAssetManager = new AssetManager();

		mAssetManager.setErrorListener(new AssetErrorListener() {
			@Override
			public void error(String fileName, Class type, Throwable throwable) {
				Gdx.app.log("AssetListener", "FAILED TO LOAD : " + fileName);
			}
		});

		mAssetManager.setLoader(Model.class, new MeshLoader(
				new InternalFileHandleResolver()));
		
		mAssetManager.load("data/ui/skinUI.json", Skin.class);		

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
		//mAssetManager.load("data/ui/red.png", Texture.class);
		mAssetManager.load("data/ui/sliderback.png", Texture.class);
		mAssetManager.load("data/ui/sliderHandle.png", Texture.class);
		mAssetManager.load("data/ui/switchCam.png", Texture.class);
		mAssetManager.load("data/ui/switchCamPressed.png", Texture.class);
		mAssetManager.load("data/ui/togMarkerPressed.png", Texture.class);
		mAssetManager.load("data/ui/togMarker.png", Texture.class);

		mAssetManager.load("data/grass.obj", Model.class);
		mAssetManager.load("data/missile.obj", Model.class);
		mAssetManager.load("data/grid.obj", Model.class);
		mAssetManager.load("data/gridHills.obj", Model.class);
		mAssetManager.load("data/marker.obj", Model.class);
		mAssetManager.load(AircraftModelPath, Model.class);
		mAssetManager.load("data/sky.obj", Model.class);
		mAssetManager.load("data/sphere.obj", Model.class);
		mAssetManager.load("data/xyzplaneZ.obj", Model.class);

		mAssetManager.load("data/sky2.obj", Model.class);
		mAssetManager.load("data/sky2.png", Texture.class);
		


		mAssetManager.finishLoading();

		Texture.setAssetManager(mAssetManager);
		
		// TODO initialize this properly
		models.put("f22", getAircraftModel());
		models.put("missile", getMissileModel());
	}

	public static void setTerrainModel(Model model) {
		mTerrainMesh = model;
	}

	public static <T> T getAsset(String name, Class<T> type) {
		return mAssetManager.get(name, type);
	}

	public static Model getAircraftModel() {
		return mAssetManager.get(AircraftModelPath, Model.class);
	}

	public static Texture getAircraftTexture() {
		return mAssetManager.get("data/camo.jpg", Texture.class);
	}

	public static Model getMissileModel() {
		return mAssetManager.get("data/missile.obj", Model.class);
	}

	public static Texture getMissileTexture() {
		return mAssetManager.get("data/camo.jpg", Texture.class);
	}

	public static Model getTerrainModel() {
		if (Assets.mTerrainMesh != null)
			return mTerrainMesh;
		return mAssetManager.get("data/gridHills.obj", Model.class);
	}

	public static Texture getTerrainTexture() {
		return mAssetManager.get("data/grass-texture.jpg", Texture.class);
	}
	//
	// public static Texture getTexture(String path) {
	// return mAssetManager.get(path, Texture.class);
	// }
	//
	// public static Mesh getObjMesh(String path) {
	// return null;
	// }
	
	private static HashMap<String, Model> models = new HashMap<String, Model>();
	
	public static Model getModelById(String id){
		return models.get(id);
	}
	
	public static Skin getSkinUI(){
		return mAssetManager.get("data/ui/skinUI.json", Skin.class);
	}

}
