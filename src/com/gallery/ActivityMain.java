package com.gallery;

import android.app.Activity;
import android.os.Bundle;

public class ActivityMain extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.layout_gallery);
        
        Integer[] images = { R.drawable.a, R.drawable.b,
                R.drawable.c, R.drawable.d, R.drawable.e,
                R.drawable.f, R.drawable.g, R.drawable.h};
        
        ImageAdapter adapter = new ImageAdapter(this, images);
        //倒影
        //adapter.createReflectedImages();
        adapter.createImages();
        //adapter.createNormalImages();
        
        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
        galleryFlow.setAdapter(adapter);
        galleryFlow.setSelection(images.length/2);
        
	}
}