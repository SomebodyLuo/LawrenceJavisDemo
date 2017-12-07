package com.x.opengl.kernel.assetloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.x.opengl.kernel.Material;
import com.x.opengl.kernel.Texture;

/**
 * 加载OBJ模型文件及其MTL材质文件
 * 
 * @date 2013-08-20 16:58:09
 */
public class ObjLoader {
	
	private static final String		TAG						= "ObjLoader";

	protected static final String	TAG_MTLLIB				= "mtllib ";		// 材质库
	protected static final String	TAG_USEMTL				= "usemtl ";		// 使用材质
	protected static final String	TAG_VERTEX				= "v ";				// 顶点
	protected static final String	TAG_NORMAL				= "vn ";			// 法线
	protected static final String	TAG_TEXTURE				= "vt ";			// 贴图坐标
	protected static final String	TAG_FACE				= "f ";				// 面
	protected static final String	TAG_GROUP				= "g ";				// 组
	protected static final String	TAG_DEFAULT_MTL			= "defaultmtl ";	// 材质默认tag 
	protected static final String	TAG_NEWMTL				= "newmtl ";		// 材质定义
	protected static final String	TAG_SHININESS			= "Ns ";			// 光亮度
	protected static final String	TAG_OPTICAL_DENSITY		= "Ni ";			// 折射值 float[0.001,1.0,10]
	protected static final String	TAG_ALPHA				= "d ";				// 透明度  float[0, 1]  同TAG_TRANSPARENT
	protected static final String	TAG_TRANSPARENT			= "Tr ";			// 透明度 float [0, 1]  同TAG_ALPHA
	protected static final String	TAG_TRANSMISSION_FILTER	= "Tf ";			// 透射滤波 RGBA float [0, 1]
	protected static final String	TAG_SHARPNESS			= "sharpness "; 	// 锐度 1 =  Ks  not  use,  2  = Ks  is  required 同TAG_ILLUMINATION
	protected static final String	TAG_AMBIENT				= "Ka ";			// 环境光 RGBA float [0, 1]
	protected static final String	TAG_DIFFUSE				= "Kd ";			// 漫反射 RGBA float [0, 1]
	protected static final String	TAG_SPECULAR			= "Ks ";			// 镜面反射  RGBA  float [0, 1]
	protected static final String	TAG_EMISSIVE			= "Ke ";			// 自发光 RGBA float [0, 1]
	protected static final String	TAG_MAP_AMBIENT			= "map_Ka ";		// 环境贴图
	protected static final String	TAG_MAP_DIFFUSE			= "map_Kd ";		// 反射贴图
	protected static final String	TAG_MAP_SPECULAR		= "map_Ks ";		// 高光贴图

	
	// 0 =  Color on and  Ambient off  
	// 1 =  Color on and  Ambient  on  
	// 2 = Highlight on 
	// 3 = Reflection on and Ray trace  on
	// 4 = Transparency:  Glass  on Reflection: Ray trace on 
	// 5 = Reflection: Fresnel on and Ray trace  on  
	// 6 =  Transparency:  Refraction  on  Reflection:  Fresnel off  and  Ray  trace  on
	// 7 =  Transparency:  Refraction  on  Reflection:  Fresnel  on  and  Ray  trace  on
	// 8 =  Reflection  on  and  Ray  trace  off
	// 9 =  Transparency:  Glass  on  Reflection:  Ray  trace  off
	// 10  =  Casts  shadows  onto  invisible  surfaces
	
	protected static final String	TAG_ILLUMINATION		= "illum ";	

	private Context					mContext				= null;
	private CBResourcer				mResourcer				= null;
	private BufferedReader			mReader					= null;
	public List<MeshPackage>		mMeshes					= null;
	public List<MaterialPackage>	mMaterials				= null;
	private String					mObjFile				= null;
	private String					mMtlFile				= null;

	private int						mMaterialSize			= 0;			// 材质数量
	private int						mVertexSize				= 0;			// 顶点数量
	private int						mNormalSize				= 0;			// 法线数量
	private int						mTextureSize			= 0;			// 贴图坐标数量
	private int						mFaceSize				= 0;			// 面片数量
	private int						mGroupSize				= 0;			// 物件数量


	/**
	 * 
	 * @date 2013-08-20 18:37:15
	 */
	class MeshPackage {
		public float[]	mVertexes		= null;
		public float[]	mNormals		= null;
		public float[]	mTextures		= null;
		public short[]	mIndices		= null;
		public String	mName		= null;

		public int		mVertexCursor	= 0;
		public int		mNormalCursor	= 0;
		public int		mTextureCursor	= 0;
		public int		mIndiceCursor	= 0;
		
		public float  minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
		public float  maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
	}
 
	/**
	 * 
	 * @date 2013-08-21 14:01:50
	 */
	class MaterialPackage {
		public String	mName	= null;
		public Material	mMaterial		= null;

		public MaterialPackage() {
			mMaterial = new Material();
		}
	}

	public ObjLoader(CBResourcer resourcer, Context context ) {
		mResourcer = resourcer;
		mContext = context;
	}

	public void loadAsset(String assetFileName) {
		mObjFile = assetFileName;

//		listView.setBackgroundResource(R.drawable.t1);
		
		try {
			mReader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(assetFileName)));
			// 扫描OBJ文件
			Log.d("ming", "scanObjFile = "  );
			scanObjFile();

			// 扫描MTL文件
			Log.d("ming", "scanMTLFile = "  );
			scanMTLFile();

			// 读取OBJ文件
			// 由于需要把mReader回复到文件头，所以重开文件
			mReader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(assetFileName)));

			Log.d("ming", "loadMesh = "  );
			loadMesh();

			// 读取材质文件
			Log.d("ming", "loadMaterial = "  );
			loadMaterial(true);

			// 组装模型
			Log.d("ming", "assembleGameObject = "  );
//			assembleGameObject();

			// 清理垃圾
			recycle();
		} catch (IOException exp) {
			exp.printStackTrace();
//			return null;
		}
//		Log.d("ming", "objDrawable = "+  mTheObjDrawable);
//		return mTheObjDrawable;
	}

	public void loadFile(String fileName) throws FileNotFoundException {
		mObjFile = fileName;
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			mReader = new BufferedReader(new FileReader(file));
		} else {
//			return null;
		}

		try {
			// 扫描OBJ文件
			scanObjFile( );

			// 扫描MTL文件
			scanMTLFile();

			// 读取OBJ文件
			// 由于需要把mReader回复到文件头，所以重开文件
			mReader = new BufferedReader(new FileReader(file));
			loadMesh( );

			// 读取材质文件
			loadMaterial(false);

			// 组装模型
//			assembleGameObject();

			// 清理垃圾
			recycle();
		} catch (IOException exp) {
			exp.printStackTrace();
//			return null;
		}

//		return mTheObjDrawable;
	}

	
	private void loadMesh( ) throws IOException {
		
		int cursor = -1;
		boolean isNewObj = false;
		ArrayList<Float> vertexData = new ArrayList<Float>(); // Mesh的顶点数据堆
		ArrayList<Float> normalData = new ArrayList<Float>(); // Mesh的法线数据堆
		ArrayList<Float> textureData = new ArrayList<Float>(); // Mesh的贴图坐标数据堆
		int scanedVertexSize = 0;
		int scanedNormalSize = 0;
		int scanedTextureSize = 0;
		int meshVertexSize = 0;
		int meshNormalSize = 0;
		int meshTextureSize = 0;
		MeshPackage mesh = null;
		String buffer = null;

		if (mMeshes.size() == 0) {
			return;
		}

		while ((buffer = mReader.readLine()) != null) {
			buffer = buffer.trim();
			if (buffer.startsWith(TAG_MTLLIB)) {
				// 材质库
				// 忽略：扫描OBJ文件时已经读取
			} else if (buffer.startsWith(TAG_USEMTL)) {
				// 使用材质
				mMeshes.get(cursor).mName = getStringValue(buffer, TAG_USEMTL);
			} else if (buffer.startsWith(TAG_VERTEX)) {
				// 顶点
				if (!isNewObj) {
					isNewObj = true;
					cursor++;

					vertexData.clear();
					normalData.clear();
					textureData.clear();
					mesh = mMeshes.get(cursor);

					scanedVertexSize += meshVertexSize;
					scanedNormalSize += meshNormalSize;
					scanedTextureSize += meshTextureSize;

					meshVertexSize = 0;
					meshNormalSize = 0;
					meshTextureSize = 0;
				}

				meshVertexSize++;
				float[] vertexNode = getFloatVectorValue(buffer, TAG_VERTEX);
				int size = vertexNode.length;
				for (int i = 0; i < size; i++) {
					vertexData.add(vertexNode[i]);
				}
			} else if (buffer.startsWith(TAG_NORMAL)) {
				// 法线
				meshNormalSize++;
				float[] normalNode = getFloatVectorValue(buffer, TAG_NORMAL);
				int size = normalNode.length;
				for (int i = 0; i < size; i++) {
					normalData.add(normalNode[i]);
				}
			} else if (buffer.startsWith(TAG_TEXTURE)) {
				// 贴图坐标
				meshTextureSize++;
				float[] textureNode = getFloatVectorValue(buffer, TAG_TEXTURE);
				int size = textureNode.length;
				for (int i = 0; i < size; i++) {
					textureData.add(textureNode[i]);
				}
			} else if (buffer.startsWith(TAG_FACE)) {
				// 面
				short[] faceNode = getShortVectorValue(buffer, TAG_FACE);

				// 设定此模型终点
				isNewObj = false;

				// faceNode = [顶点索引,贴图索引,法线索引,顶点索引,贴图索引,法线索引,顶点索引,贴图索引,法线索引]
				// 顶点数组不动，把顶点数组的索引拷贝到索引数组中
				// 通过贴图坐标索引和法线索引取出数据堆中的值，并放入到空的贴图坐标数组和法线数组中
				int size = faceNode.length;
				for (int i = 0; i < size; i++) {
					if (i % 3 == 0) {
						// 顶点索引
						if (faceNode[i] != 0 && mesh.mVertexes.length > mesh.mVertexCursor) {
							int pointIndex = (faceNode[i] - 1 - scanedVertexSize) * 3;
							float x = vertexData.get(pointIndex + 0);
							float y = vertexData.get(pointIndex + 1);
							float z = vertexData.get(pointIndex + 2);

							mesh.mVertexes[mesh.mVertexCursor++] = x; // x
							mesh.mVertexes[mesh.mVertexCursor++] = y; // y
							mesh.mVertexes[mesh.mVertexCursor++] = z; // z

							mesh.minX = mesh.minX < x ? mesh.minX : x;
							mesh.maxX = mesh.maxX > x ? mesh.maxX : x;
							
							mesh.minY = mesh.minY < y ? mesh.minY : y;
							mesh.maxY = mesh.maxY > y ? mesh.maxY : y;
							
							mesh.minZ = mesh.minZ < z ? mesh.minZ : z;
							mesh.maxZ = mesh.maxZ > z ? mesh.maxZ : z;

							mesh.mIndices[mesh.mIndiceCursor++] = (short) (mesh.mIndiceCursor - 1); //(short)(faceNode[i] - 1);
							
						}
					}
					if (i % 3 == 1) {
						// 贴图索引
						if (faceNode[i] != 0 && mesh.mTextures.length > mesh.mTextureCursor) {
							int pointIndex = (faceNode[i] - 1 - scanedTextureSize) * 3;
							mesh.mTextures[mesh.mTextureCursor++] = textureData.get(pointIndex + 0); // u
							mesh.mTextures[mesh.mTextureCursor++] = 1f-textureData.get(pointIndex + 1); // v
							// mesh.mTextures[mesh.mTextureCursor++] =
							// textureData.get(pointIndex + 2); // w, ignore
							// this one
						}
					}
					if (i % 3 == 2) {
						// 法线索引
						if (faceNode[i] != 0 && mesh.mNormals.length > mesh.mNormalCursor) {
							int pointIndex = (faceNode[i] - 1 - scanedNormalSize) * 3;
							mesh.mNormals[mesh.mNormalCursor++] = normalData.get(pointIndex + 0); // x
							mesh.mNormals[mesh.mNormalCursor++] = normalData.get(pointIndex + 1); // y
							mesh.mNormals[mesh.mNormalCursor++] = normalData.get(pointIndex + 2); // z
						}
					}
				}
			} else if (buffer.startsWith(TAG_GROUP)) {
				// 组

			}
		}

		Log.d("ming", "mesh = "+vertexData.size() /3  );
	}

	private void loadMaterial(boolean isAssetFile) throws IOException {
		int cursor = -1;
		String buffer = null;
		MaterialPackage material = null;
		BufferedReader reader = null;

		if (isAssetFile) {
			reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(mMtlFile)));
		} else {
			reader = new BufferedReader(new FileReader(mMtlFile));
		}

		while ((buffer = reader.readLine()) != null) {
			buffer = buffer.trim();
			if (buffer.startsWith(TAG_NEWMTL)) {
				
				
				// 新材质定义
				cursor++;
				material = new MaterialPackage();
				material.mName = getStringValue(buffer, TAG_NEWMTL);

				if (mMaterials == null) {
					mMaterials = new ArrayList<MaterialPackage>();
				}
				mMaterials.add(material);
			} else if (buffer.startsWith(TAG_SHININESS)) {
				// 光亮度
				material.mMaterial.setShininess(getFloatValue(buffer, TAG_SHININESS));
			} else if (buffer.startsWith(TAG_OPTICAL_DENSITY)) {
				// 折射值
				material.mMaterial.setOpticalDensity(getFloatValue(buffer, TAG_OPTICAL_DENSITY));
			} else if (buffer.startsWith(TAG_ALPHA)) {
				// 透明度
				material.mMaterial.setAlpha(getFloatValue(buffer, TAG_ALPHA));
			} else if (buffer.startsWith(TAG_TRANSPARENT)) {
				// 透明度
				material.mMaterial.setTransparent(getFloatValue(buffer, TAG_TRANSPARENT));
			} else if (buffer.startsWith(TAG_TRANSMISSION_FILTER)) {
				// 透射滤波
				material.mMaterial.setTransmissionFilter(getFloatVectorValue(buffer, TAG_TRANSMISSION_FILTER));
			} else if (buffer.startsWith(TAG_ILLUMINATION)) {
				// 照明度
				material.mMaterial.setIllumination((int) getFloatValue(buffer, TAG_ILLUMINATION));
			} else if (buffer.startsWith(TAG_SHARPNESS)) {
				// 锐度
				material.mMaterial.setSharpness(getFloatValue(buffer, TAG_SHARPNESS));
			} else if (buffer.startsWith(TAG_AMBIENT)) {
				// 环境光
				material.mMaterial.setAmbient(getFloatVectorValue(buffer, TAG_AMBIENT));
			} else if (buffer.startsWith(TAG_DIFFUSE)) {
				// 漫反射
				material.mMaterial.setDiffuse(getFloatVectorValue(buffer, TAG_DIFFUSE));
			} else if (buffer.startsWith(TAG_SPECULAR)) {
				// 镜面反射
				material.mMaterial.setSpecular(getFloatVectorValue(buffer, TAG_SPECULAR));
			} else if (buffer.startsWith(TAG_EMISSIVE)) {
				// 自发光
				material.mMaterial.setEmission(getFloatVectorValue(buffer, TAG_EMISSIVE));
			} else if (buffer.startsWith(TAG_MAP_AMBIENT)) {
				// 环境贴图
				// TODO
			} else if (buffer.startsWith(TAG_MAP_DIFFUSE)) {
				// 漫反射贴图
				long time = System.currentTimeMillis();
				Texture texture = null;
				String textureFileName = getStringValue(buffer, TAG_MAP_DIFFUSE);

				textureFileName = mObjFile.substring(0, mObjFile.lastIndexOf("/")) + "/" + textureFileName;
				if (isAssetFile) {
					texture = mResourcer.generateTexture(BitmapFactory.decodeStream(mContext.getAssets().open(textureFileName)));
				} else {
					texture = mResourcer.generateTexture(BitmapFactory.decodeFile(textureFileName));
				}
				material.mMaterial.Texture = texture;
//				Log.d("debug", "time 300 = "+(System.currentTimeMillis() - time));
//				Log.d("debug", "tag = "+TAG_MAP_DIFFUSE+",,,textureFileName = "+textureFileName);
			} else if (buffer.startsWith(TAG_MAP_SPECULAR)) {
				// 高光贴图
				// TODO
			}
			
		}
		//如果没有找到纹理图定义,需要增加一个默认纹理
		for (int i = 0; i < mMaterials.size(); i++) {
			MaterialPackage mat  = mMaterials.get(i);
			if(  mat.mMaterial.Texture == null){

				//如果没有纹理图，则使用材质颜色构造一张纹理
				float[] ambient = mat.mMaterial.getAmbient();
				float[] diffuse = mat.mMaterial.getDiffuse();
				float[] specular = mat.mMaterial.getSpecular();
				float[] color =  new float[]{
						(ambient[0] + diffuse[0] + specular[0])/3.0f,
						(ambient[1] + diffuse[1] + specular[1])/3.0f,
						(ambient[2] + diffuse[2] + specular[2])/3.0f,
						(ambient[3] + diffuse[3] + specular[3])/3.0f,
						}; 
				int c   = Color.rgb((int)(color[0]*255),(int)(color[1]*255),(int)(color[2]*255) );
				int widthe = 100;
				int height = 100;
				Bitmap bitmap = Bitmap.createBitmap(widthe, height, Config.ARGB_8888);
				bitmap.eraseColor(c);
				Paint p = new Paint();
				p.setColor( Color.rgb(255-(int)(color[0]*255),255-(int)(color[1]*255),255-(int)(color[2]*255) ));
				p.setTextSize(widthe * 0.2f);
				p.setTextAlign(Align.CENTER);
				Canvas can = new Canvas(bitmap);
				can.drawText("光能蜗牛", widthe/2, height/2+p.ascent()/2, p);
				mat.mMaterial.Texture = mResourcer.generateTexture(bitmap);
			}
		}
		
		reader.close();
	}

	/**
	 * 扫描OBJ文件
	 * 
	 * @throws IOException
	 */
	private void scanObjFile( ) throws IOException {

		
		int meshVertexSize = 0;
		int meshNormalSize = 0;
		int meshTextureSize = 0;
		int meshIndiceSize = 0;
		int meshFaceSize = 0;
		MeshPackage meshPackage = null;
		String buffer = null;
		boolean mIsFaceEnd = true;

		mMeshes = new ArrayList<MeshPackage>();
		while ((buffer = mReader.readLine()) != null) {
			if (buffer.startsWith(TAG_MTLLIB)) {
				// 材质库
				mMtlFile = getStringValue(buffer, TAG_MTLLIB);

				// 重建文件名
				mMtlFile = mObjFile.substring(0, mObjFile.lastIndexOf("/")) + "/" + mMtlFile;
			} else if (buffer.startsWith(TAG_USEMTL)) {
				// 使用材质
				mMaterialSize++;
			} else if (buffer.startsWith(TAG_VERTEX)) {
				// 顶点
				if (!mIsFaceEnd) {
					mIsFaceEnd = true;
					meshPackage = new MeshPackage();
					if (meshVertexSize > 0) {
						meshPackage.mVertexes = new float[3 * 3 * meshFaceSize];
					}
					if (meshNormalSize > 0) {
						meshPackage.mNormals = new float[3 * 3 * meshFaceSize];
					}
					if (meshTextureSize > 0) {
						meshPackage.mTextures = new float[2 * 3 * meshFaceSize];
					}
					meshPackage.mIndices = new short[3 * meshFaceSize];
					mMeshes.add(meshPackage);
					meshVertexSize = 0;
					meshNormalSize = 0;
					meshTextureSize = 0;
					meshFaceSize = 0;
				}
				mVertexSize++;
				meshVertexSize++;
			} else if (buffer.startsWith(TAG_NORMAL)) {
				// 法线
				mNormalSize++;
				meshNormalSize++;
			} else if (buffer.startsWith(TAG_TEXTURE)) {
				// 贴图坐标
				mTextureSize++;
				meshTextureSize++;
			} else if (buffer.startsWith(TAG_FACE)) {
				// 面
				mFaceSize++;
				meshIndiceSize += 3;
				meshFaceSize++;
				mIsFaceEnd = false;
			} else if (buffer.startsWith(TAG_GROUP)) {
				// 组
				mGroupSize++;
			}
		}

		// 最后一段Face数据
		// 保存前一个Mesh
		meshPackage = new MeshPackage();
		if (meshVertexSize > 0) {
			meshPackage.mVertexes = new float[3 * 3 * meshFaceSize];
		}
		if (meshNormalSize > 0) {
			meshPackage.mNormals = new float[3 * 3 * meshFaceSize];
		}
		if (meshTextureSize > 0) {
			meshPackage.mTextures = new float[2 * 3 * meshFaceSize];
		}
		meshPackage.mIndices = new short[3 * meshFaceSize];
		mMeshes.add(meshPackage);
		meshVertexSize = 0;
		meshNormalSize = 0;
		meshTextureSize = 0;
		meshFaceSize = 0;

		mReader.close();
		
//		for (int i = 0; i < mMeshes.size(); i++) {
//			MeshPackage s = mMeshes.get(i);
//			int trangelCount = s.mVertexes.length/3/3;
//			Log.d("debug","trangelCount = "+trangelCount);
//		}
		
	}
	
	/**
	 * 扫描MTL文件
	 */
	private void scanMTLFile() {
		// 似乎没有扫描的必要
	}

	private void recycle() {

		if (mReader != null) {
			try {
				mReader.close();
			} catch (IOException exp) {
				exp.printStackTrace();
			} finally {
				mReader = null;
			}
		}
//		if (mMaterials != null) {
//			mMaterials.clear();
//			mMaterials = null;
//		}
//		if (mMeshes != null) {
//			mMeshes.clear();
//			mMeshes = null;
//		}
	}

	/**
	 * 获取字符串
	 * 
	 * @param buffer
	 * @param TAG
	 * @return
	 */
	private String getStringValue(String buffer, String TAG) {
		buffer = buffer.trim();
		return buffer.substring(TAG.length()).trim();
	}

	/**
	 * 获取实数值
	 * 
	 * @param buffer
	 * @param TAG
	 * @return
	 */
	private float getFloatValue(String buffer, String TAG) {
		buffer = buffer.trim();
		return Float.valueOf(buffer.substring(TAG.length()).trim());
	}

	/**
	 * 获取实数组值
	 * 
	 * @param buffer
	 * @param TAG
	 * @return
	 */
	private float[] getFloatVectorValue(String buffer, String TAG) {
		String[] splitedString = null;
		float[] values = null;

		buffer = buffer.trim();
		splitedString = buffer.substring(TAG.length()).trim().split(" ");
		values = new float[splitedString.length];

		for (int i = 0; i < values.length; i++) {
			values[i] = Float.valueOf(splitedString[i]);
		}

		return values;
	}

	/**
	 * 获取单精度组值
	 * 
	 * @param buffer
	 * @param TAG
	 * @return
	 */
	private short[] getShortVectorValue(String buffer, String TAG) {
		String[] splitedString = null;
		String[] subSplitedString = null;
		short[] values = null;

		buffer = buffer.trim();
		splitedString = buffer.substring(TAG.length()).trim().split(" ");
		values = new short[3 * splitedString.length];

		int size = splitedString.length;
		for (int i = 0; i < size; i++) {
			subSplitedString = splitedString[i].trim().split("/");
			for (int j = 0; j < subSplitedString.length; j++) {
				try {
					values[i * 3 + j] = Short.valueOf(subSplitedString[j]);
				} catch (NumberFormatException exp) {
					values[i * 3 + j] = 0;
				}
			}
		}

		return values;
	}

	private float[] getFloatArray(List<Float> list) {
		int size = list.size();
		float[] values = new float[size];
		for (int i = 0; i < size; i++) {
			values[i] = list.get(i);
		}

		return values;
	}

}
