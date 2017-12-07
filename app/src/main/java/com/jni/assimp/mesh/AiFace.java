package com.jni.assimp.mesh;

public class AiFace {

    //! Number of indices defining this face.
    //! The maximum value for this member is #AI_MAX_FACE_INDICES.
    public int mNumIndices;

    //! Pointer to the indices array. Size of the array is given in numIndices.
    public int[] mIndices;
}
