package com.jni.assimp;

import com.jni.assimp.mesh.AiMesh;

public class AiScene {

	
	private String mName = "aaaaaaaaaaaaa";
	

    /** Any combination of the AI_SCENE_FLAGS_XXX flags. By default
    * this value is 0, no flags are set. Most applications will
    * want to reject all scenes with the AI_SCENE_FLAGS_INCOMPLETE
    * bit set.
    */
	public int mFlags;
    

    /** The root node of the hierarchy.
    *
    * There will always be at least the root node if the import
    * was successful (and no special flags have been set).
    * Presence of further nodes depends on the format and content
    * of the imported file.
    */
    AiNode  mRootNode;


    /** The number of meshes in the scene. */
    public int mNumMeshes;

    /** The array of meshes.
    *
    * Use the indices given in the aiNode structure to access
    * this array. The array is mNumMeshes in size. If the
    * AI_SCENE_FLAGS_INCOMPLETE flag is not set there will always
    * be at least ONE material.
    */
    public AiMeshManager  mMeshes;



    /** The number of materials in the scene. */
    public int mNumMaterials;

    /** The array of materials.
    *
    * Use the index given in each aiMesh structure to access this
    * array. The array is mNumMaterials in size. If the
    * AI_SCENE_FLAGS_INCOMPLETE flag is not set there will always
    * be at least ONE material.
    */
    public AiMaterial  mMaterials;



    /** The number of animations in the scene. */
    public  int mNumAnimations;

    /** The array of animations.
    *
    * All animations imported from the given file are listed here.
    * The array is mNumAnimations in size.
    */
    AiAnimation  mAnimations;



    /** The number of textures embedded into the file */
    public int mNumTextures;

    /** The array of embedded textures.
    *
    * Not many file formats embed their textures into the file.
    * An example is Quake's MDL format (which is also used by
    * some GameStudio versions)
    */
    AiTexture mTextures;


    /** The number of light sources in the scene. Light sources
    * are fully optional, in most cases this attribute will be 0
        */
    public int mNumLights;

    /** The array of light sources.
    *
    * All light sources imported from the given file are
    * listed here. The array is mNumLights in size.
    */
    AiLight  mLights;


    /** The number of cameras in the scene. Cameras
    * are fully optional, in most cases this attribute will be 0
        */
    public  int mNumCameras;

    /** The array of cameras.
    *
    * All cameras imported from the given file are listed here.
    * The array is mNumCameras in size. The first camera in the
    * array (if existing) is the default camera view into
    * the scene.
    */
    AiCamera  mCameras;
    
    
	public final float cubePosition[] = 
         {
                 // Front face
                 -1.0f, 1.0f, 1.0f,                
                 -1.0f, -1.0f, 1.0f,
                 1.0f, 1.0f, 1.0f, 
                 -1.0f, -1.0f, 1.0f,                 
                 1.0f, -1.0f, 1.0f,
                 1.0f, 1.0f, 1.0f,
                 
                 // Right face
                 1.0f, 1.0f, 1.0f,                
                 1.0f, -1.0f, 1.0f,
                 1.0f, 1.0f, -1.0f,
                 1.0f, -1.0f, 1.0f,                
                 1.0f, -1.0f, -1.0f,
                 1.0f, 1.0f, -1.0f,
                 
                 // Back face
                 1.0f, 1.0f, -1.0f,                
                 1.0f, -1.0f, -1.0f,
                 -1.0f, 1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,                
                 -1.0f, -1.0f, -1.0f,
                 -1.0f, 1.0f, -1.0f,
                 
                 // Left face
                 -1.0f, 1.0f, -1.0f,                
                 -1.0f, -1.0f, -1.0f,
                 -1.0f, 1.0f, 1.0f, 
                 -1.0f, -1.0f, -1.0f,                
                 -1.0f, -1.0f, 1.0f, 
                 -1.0f, 1.0f, 1.0f, 
                 
                 // Top face
                 -1.0f, 1.0f, -1.0f,                
                 -1.0f, 1.0f, 1.0f, 
                 1.0f, 1.0f, -1.0f, 
                 -1.0f, 1.0f, 1.0f,                 
                 1.0f, 1.0f, 1.0f, 
                 1.0f, 1.0f, -1.0f,
                 
                 // Bottom face
                 1.0f, -1.0f, -1.0f,                
                 1.0f, -1.0f, 1.0f, 
                 -1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, 1.0f,                 
                 -1.0f, -1.0f, 1.0f,
                 -1.0f, -1.0f, -1.0f,    
         };
         
	public   final float[] cubeColor = 
         {
                 // Front face (red)
                 1.0f, 0.0f, 0.0f, 1.0f,                
                 1.0f, 0.0f, 0.0f, 1.0f,
                 1.0f, 0.0f, 0.0f, 1.0f,
                 1.0f, 0.0f, 0.0f, 1.0f,                
                 1.0f, 0.0f, 0.0f, 1.0f,
                 1.0f, 0.0f, 0.0f, 1.0f,
                 
                 // Right face (green)
                 0.0f, 1.0f, 0.0f, 1.0f,                
                 0.0f, 1.0f, 0.0f, 1.0f,
                 0.0f, 1.0f, 0.0f, 1.0f,
                 0.0f, 1.0f, 0.0f, 1.0f,                
                 0.0f, 1.0f, 0.0f, 1.0f,
                 0.0f, 1.0f, 0.0f, 1.0f,
                 
                 // Back face (blue)
                 0.0f, 0.0f, 1.0f, 1.0f,                
                 0.0f, 0.0f, 1.0f, 1.0f,
                 0.0f, 0.0f, 1.0f, 1.0f,
                 0.0f, 0.0f, 1.0f, 1.0f,                
                 0.0f, 0.0f, 1.0f, 1.0f,
                 0.0f, 0.0f, 1.0f, 1.0f,
                 
                 // Left face (yellow)
                 1.0f, 1.0f, 0.0f, 1.0f,                
                 1.0f, 1.0f, 0.0f, 1.0f,
                 1.0f, 1.0f, 0.0f, 1.0f,
                 1.0f, 1.0f, 0.0f, 1.0f,                
                 1.0f, 1.0f, 0.0f, 1.0f,
                 1.0f, 1.0f, 0.0f, 1.0f,
                 
                 // Top face (cyan)
                 0.0f, 1.0f, 1.0f, 1.0f,                
                 0.0f, 1.0f, 1.0f, 1.0f,
                 0.0f, 1.0f, 1.0f, 1.0f,
                 0.0f, 1.0f, 1.0f, 1.0f,                
                 0.0f, 1.0f, 1.0f, 1.0f,
                 0.0f, 1.0f, 1.0f, 1.0f,
                 
                 // Bottom face (magenta)
                 1.0f, 0.0f, 1.0f, 1.0f,                
                 1.0f, 0.0f, 1.0f, 1.0f,
                 1.0f, 0.0f, 1.0f, 1.0f,
                 1.0f, 0.0f, 1.0f, 1.0f,                
                 1.0f, 0.0f, 1.0f, 1.0f,
                 1.0f, 0.0f, 1.0f, 1.0f    
         };

	public String getName() {
		return mName;
	}
	
}