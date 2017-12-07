package com.x.opengl.kernel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class MaterialGroup implements Cloneable{

	private float                     mBodyAlpha     = 1f;
	private HashMap<String, Material> mMaterialGroup = null;
	private List<String> mIndexList = new ArrayList<String>();
	
	public MaterialGroup() {
		mMaterialGroup = new HashMap<String, Material>();
	}
	
	@Override
	public MaterialGroup clone(){
		MaterialGroup cloned = null;
		try {
			cloned = (MaterialGroup) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			cloned = new MaterialGroup();
		}
		
		cloned.mMaterialGroup.clone();
		return cloned;
	}
	
	public Material getMaterial(String materialName) {
		return mMaterialGroup.get(materialName);
	}

	public Material getMaterial(int index) {
		return mMaterialGroup.get(mIndexList.get(index));
	}
	public List<Material> getMaterials() {
		return (List<Material>) mMaterialGroup.values();
	}
	
	public int size() {
		return mMaterialGroup.size();
	}
	
	public void addMaterial(Material material) {
		String materialName = material.Name;
		if (materialName == null || materialName.equals("")) {
			materialName = "material";
		}
		mIndexList.add(materialName);
		mMaterialGroup.put(materialName, material);
	}
	
	public void clear() {
		recycle();
		mMaterialGroup.clear();
	}
	
	public void setAlpha(float alpha) {
		mBodyAlpha = alpha;
	}

	public float getAlpha() {
		return mBodyAlpha;
	}

	public void recycle() {
		Iterator<Entry<String, Material>> iter = mMaterialGroup.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Material> entry = (Entry<String, Material>) iter.next();
			entry.getValue().recycle();
		}
	}
}
