package com.jni.assimp.mesh;


public class AiMesh {

	final static  int AI_MAX_NUMBER_OF_COLOR_SETS = 0x8; 
    final static int AI_MAX_NUMBER_OF_TEXTURECOORDS =  0x8;
	/** Bitwise combination of the members of the #aiPrimitiveType enum.
     * This specifies which types of primitives are present in the mesh.
     * The "SortByPrimitiveType"-Step can be used to make sure the
     * output meshes consist of one primitive type each.
     */
     int mPrimitiveTypes;

    /** The number of vertices in this mesh.
    * This is also the size of all of the per-vertex data arrays.
    * The maximum value for this member is #AI_MAX_VERTICES.
    */
   public int mNumVertices;

    /** The number of primitives (triangles, polygons, lines) in this  mesh.
    * This is also the size of the mFaces array.
    * The maximum value for this member is #AI_MAX_FACES.
    */
    public int mNumFaces;

    /** Vertex positions.
    * This array is always present in a mesh. The array is
    * mNumVertices in size.
    */
    public  AiVector3D[]  mVertices;

    /** Vertex normals.
    * The array contains normalized vectors, NULL if not present.
    * The array is mNumVertices in size. Normals are undefined for
    * point and line primitives. A mesh consisting of points and
    * lines only may not have normal vectors. Meshes with mixed
    * primitive types (i.e. lines and triangles) may have normals,
    * but the normals for vertices that are only referenced by
    * point or line primitives are undefined and set to QNaN (WARN:
    * qNaN compares to inequal to *everything*, even to qNaN itself.
    * Using code like this to check whether a field is qnan is:
    * @code
    * #define IS_QNAN(f) (f != f)
    * @endcode
    * still dangerous because even 1.f == 1.f could evaluate to false! (
    * remember the subtleties of IEEE754 artithmetics). Use stuff like
    * @c fpclassify instead.
    * @note Normal vectors computed by Assimp are always unit-length.
    * However, this needn't apply for normals that have been taken
    *   directly from the model file.
    */
    public  AiVector3D[]  mNormals;

    /** Vertex tangents.
    * The tangent of a vertex points in the direction of the positive
    * X texture axis. The array contains normalized vectors, NULL if
    * not present. The array is mNumVertices in size. A mesh consisting
    * of points and lines only may not have normal vectors. Meshes with
    * mixed primitive types (i.e. lines and triangles) may have
    * normals, but the normals for vertices that are only referenced by
    * point or line primitives are undefined and set to qNaN.  See
    * the #mNormals member for a detailed discussion of qNaNs.
    * @note If the mesh contains tangents, it automatically also
    * contains bitangents.
    */
    AiVector3D  mTangents;
    /** Vertex bitangents.
    * The bitangent of a vertex points in the direction of the positive
    * Y texture axis. The array contains normalized vectors, NULL if not
    * present. The array is mNumVertices in size.
    * @note If the mesh contains tangents, it automatically also contains
    * bitangents.
    */
    AiVector3D  mBitangents;

    /** Vertex color sets.
    * A mesh may contain 0 to #AI_MAX_NUMBER_OF_COLOR_SETS vertex
    * colors per vertex. NULL if not present. Each array is
    * mNumVertices in size if present.
    */
//    AiColor4D  mColors[AI_MAX_NUMBER_OF_COLOR_SETS];
    AiColor4D  mColors[ ];
    /** Vertex texture coords, also known as UV channels.
    * A mesh may contain 0 to AI_MAX_NUMBER_OF_TEXTURECOORDS per
    * vertex. NULL if not present. The array is mNumVertices in size.
    */
//    AiVector3D  mTextureCoords[AI_MAX_NUMBER_OF_TEXTURECOORDS];
    public AiTextureCoord   mTextureCoords[ ];

    /** Specifies the number of components for a given UV channel.
    * Up to three channels are supported (UVW, for accessing volume
    * or cube maps). If the value is 2 for a given channel n, the
    * component p.z of mTextureCoords[n][p] is set to 0.0f.
    * If the value is 1 for a given channel, p.y is set to 0.0f, too.
    * @note 4D coords are not supported
    */
//    int mNumUVComponents[AI_MAX_NUMBER_OF_TEXTURECOORDS];
    int mNumUVComponents[ ];

    /** The faces the mesh is constructed from.
    * Each face refers to a number of vertices by their indices.
    * This array is always present in a mesh, its size is given
    * in mNumFaces. If the #AI_SCENE_FLAGS_NON_VERBOSE_FORMAT
    * is NOT set each face references an unique set of vertices.
    */
    public AiFace[]  mFaces;

    /** The number of bones this mesh contains.
    * Can be 0, in which case the mBones array is NULL.
    */
    int mNumBones;

    /** The bones of this mesh.
    * A bone consists of a name by which it can be found in the
    * frame hierarchy and a set of vertex weights.
    */
    AiBone  mBones;

    /** The material used by this mesh.
     * A mesh uses only a single material. If an imported model uses
     * multiple materials, the import splits up the mesh. Use this value
     * as index into the scene's material list.
     */
   public int mMaterialIndex;

    /** Name of the mesh. Meshes can be named, but this is not a
     *  requirement and leaving this field empty is totally fine.
     *  There are mainly three uses for mesh names:
     *   - some formats name nodes and meshes independently.
     *   - importers tend to split meshes up to meet the
     *      one-material-per-mesh requirement. Assigning
     *      the same (dummy) name to each of the result meshes
     *      aids the caller at recovering the original mesh
     *      partitioning.
     *   - Vertex animations refer to meshes by their names.
     **/
    public String mName;


    /** NOT CURRENTLY IN USE. The number of attachment meshes */
    int mNumAnimMeshes;

    /** NOT CURRENTLY IN USE. Attachment meshes for this mesh, for vertex-based animation.
     *  Attachment meshes carry replacement data for some of the
     *  mesh'es vertex components (usually positions, normals). */
    AiAnimMesh  mAnimMeshes;

    public boolean HasPositions(){
    	return mVertices != null;
    }
    public boolean HasTextureCoords(int pIndex){

        if( pIndex >= AI_MAX_NUMBER_OF_TEXTURECOORDS)
            return false;
        else
            return mTextureCoords[pIndex] != null && mNumVertices > 0;
    }
	public boolean HasNormals() {
        return mNormals != null && mNumVertices > 0;
	}
}
