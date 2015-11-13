package com.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityMainVerical extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.layout_gallery_vertical);
        
        Integer[] images = { R.drawable.a, R.drawable.b,
                R.drawable.c, R.drawable.d, R.drawable.e,
                R.drawable.f, R.drawable.g, R.drawable.h};
        
        VerticalImageAdapter adapter = new VerticalImageAdapter(this, images);
        //adapter.createReflectedImages();
        adapter.createImages();

        final VericalGalleryFlow galleryFlow = (VericalGalleryFlow) findViewById(R.id.Gallery01);
        galleryFlow.setAdapter(adapter);
        galleryFlow.setSelection(images.length/2);
//        galleryFlow.setOnItemClickListener(new OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				galleryFlow.setSelection(arg2);
//			}
//        	
//		});
        //设置
        //galleryFlow.setSpacing(20);
        galleryFlow.setUnselectedAlpha(0.5f);
	}
}