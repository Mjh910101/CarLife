package com.itkc_carlife.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.itkc_carlife.download.DownloadImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpFlieBox {

	private Map<String, String> headMap;
	private Map<String, String> paramsMap;
	private List<String> list;

	private String fileKey;

	public HttpFlieBox() {
		headMap = new HashMap<String, String>();
		paramsMap = new HashMap<String, String>();
	}

	public void addHead(String k, String v) {
		headMap.put(k, v);
	}

	public void addParams(String k, String v) {
		paramsMap.put(k, v);
	}

	public void addFileList(String k, List<String> list) {
		this.fileKey = k;
		this.list = list;
	}

	public String getFileKey() {
		return fileKey;
	}

	public List<String> getFilePathList() {
		return list;
	}

	public Map<String, String> getHeadMap() {
		return headMap;
	}

	public Map<String, String> getParamMap() {
		return paramsMap;
	}

	public List<File> getFileList() {
		List<File> fileList = new ArrayList<File>();
		if (list != null) {
			for (String path : list) {
				if (!path.equals("null")) {
					Log.e("", path);
					fileList.add(compressPicture(path));
				}
			}
		}
		return fileList;
	}

	// public List<FileInfo> getFileInfoList() {
	// List<FileInfo> fileList = new ArrayList<FileInfo>();
	// if (list != null) {
	// for (String path : list) {
	// if (!path.equals("null")) {
	// FileInfo fileInfo = new FileInfo();
	// fileInfo.setFile(new File(path));
	// fileInfo.setFileTextName(fileKey);
	// fileList.add(fileInfo);
	// }
	// }
	// }
	// return fileList;
	// }

	private synchronized File compressPicture(String picPath) {

		int maxWidth = 640, maxHeight = 480; // 设置新图片的大小
		String fileName = picPath.substring(picPath.lastIndexOf("/"));

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap image = BitmapFactory.decodeFile(picPath, options);
		double ratio = 1D;
		if (maxWidth < options.outWidth && maxWidth < options.outHeight) {
			double _widthRatio = Math.ceil(options.outWidth / maxWidth);
			double _heightRatio = Math.ceil(options.outHeight / maxHeight);
			ratio = _widthRatio > _heightRatio ? _widthRatio : _heightRatio;
		} else if (maxWidth < options.outWidth) {
			ratio = Math.ceil(options.outWidth / maxWidth);// 限定宽度，高度不做限制
		} else if (maxHeight < options.outHeight) {
			ratio = Math.ceil(options.outHeight / maxHeight);// 限定高度，不限制宽度
		}
		if (ratio > 1) {
			options.inSampleSize = (int) ratio;
		}
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		image = BitmapFactory.decodeFile(picPath, options);

		File rootFile = new File(getImagePath());// 保存入sdCard
		if (!rootFile.exists()) {
			rootFile.mkdir();
		}
		File file = new File(getImagePath() + fileName);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (image.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new File(picPath);
		} finally {
			if (image != null && !image.isRecycled()) {
				image.recycle();
			}
		}
		return file;
	}

	public static String getImagePath() {
		return DownloadImageLoader.getImagePath() ;
	}
}
