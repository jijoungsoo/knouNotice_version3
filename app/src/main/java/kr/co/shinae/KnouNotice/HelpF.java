package kr.co.shinae.KnouNotice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import kr.co.shinae.KnouNotice.HelpF.Log;
import kr.co.shinae.KnouNotice.second.LocationInfo;

import org.w3c.dom.Document;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore.Images;

import android.widget.Toast;

public class HelpF {
	public static boolean DEBUG=true;
	//public static String AdamClientID="TestClientId";


	public static void SSMessage(Activity context,String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void SLMessage(Context context,String text){
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static class Log{
		public static void d(Object context ,String str){
			if(HelpF.DEBUG)
				android.util.Log.d("KnouNotice", str);
		}
		public static void e(Object context ,String str){
			if(HelpF.DEBUG)
				android.util.Log.e("KnouNotice", str);
		}

		public static void i(Object context ,String str){
			if(HelpF.DEBUG)
				android.util.Log.e("KnouNotice", str);
		}
		public static void w(Object context ,String str){
			if(HelpF.DEBUG)
				android.util.Log.e("KnouNotice", str);
		}
	}

	public static boolean  Wifi3gConnectivity(Activity context){

		ConnectivityManager manager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 3G 연결확인
		boolean Mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		// Wifi 연결확인
		boolean wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		return (Mobile||wifi);
	}

	public static SimpleDateFormat sdformat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.KOREA);
	public static SimpleDateFormat sdformathhmmfilename = new SimpleDateFormat(
			"yyyyMMddhhmm", Locale.KOREA);
	public static SimpleDateFormat sdformathhmmss = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss", Locale.KOREA);

	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}



	public void logMemory() {
		HelpF.Log.i("HAN", "### freeMemory : " + Runtime.getRuntime().freeMemory()
				/ (1024 * 1024) + " MB");
		HelpF.Log.i("HAN", "### Heap maxMemory : " + Runtime.getRuntime().maxMemory()
				/ (1024 * 1024) + " MB");
		HelpF.Log.i("HAN", "### totalMemory : " + Runtime.getRuntime().totalMemory()
				/ (1024 * 1024) + " MB");
		HelpF.Log.i("HAN",
				"### getNativeHeapFreeSize : " + Debug.getNativeHeapFreeSize()
						/ (1024 * 1024) + " MB");
		HelpF.Log.i("HAN",
				"### getNativeHeapAllocatedSize : "
						+ Debug.getNativeHeapAllocatedSize() / (1024 * 1024)
						+ " MB");
		HelpF.Log.i("HAN", "### getNativeHeapSize : " + Debug.getNativeHeapSize());
	}

	final static String copyFile(String targetFilePath, File sdpath) {
		File inFile = new File(targetFilePath);
		String fileName = inFile.getName();
		fileName = HelpF.getUniqueFileName(sdpath.getAbsolutePath(),
				fileName);
		File copyFile = new File(sdpath.getAbsolutePath(), fileName);
		String result = "ERROR";
		FileInputStream in = null;
		FileOutputStream out = null;
		try {

			copyFile.createNewFile();
			out = new FileOutputStream(copyFile); // 쓸파일
			in = new FileInputStream(inFile); // 원본파일
			byte buf[] = new byte[4096];
			int count = 0;
			while ((count = in.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			result = copyFile.getAbsoluteFile().toString();
		} catch (Exception e) {
			// Log.d("HAN", e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	final static String copyFile(long _id, String locationSrc, File inFile) {
		File sdpath = getFileLoc(locationSrc);
		String fileName = String.valueOf(_id) + "_" + inFile.getName();
		// Log.d("HAN","aa:"+fileName);
		fileName = HelpF.getUniqueFileName(sdpath.getAbsolutePath(),
				fileName);
		// Log.d("HAN","bb:"+fileName);
		File copyFile = new File(sdpath.getAbsolutePath(), fileName);
		String result = "ERROR";
		FileInputStream in = null;
		FileOutputStream out = null;
		try {

			copyFile.createNewFile();
			out = new FileOutputStream(copyFile); // 쓸파일
			in = new FileInputStream(inFile); // 원본파일
			byte buf[] = new byte[4096];
			int count = 0;
			while ((count = in.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			result = copyFile.getAbsoluteFile().toString();
		} catch (Exception e) {
			// Log.d("HAN", e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	final static String getDateFormatName() {
		return HelpF.sdformathhmmfilename.format(new Date());
	}

	final static String makeThumbnailMini(String locationSrc, File inFile) {
		File sdpath = getFileLoc(locationSrc);
		/*
		 * Bitmap bmp = Images.Thumbnails.getThumbnail(contentResolver, id,
		 * Images.Thumbnails.MINI_KIND, null);
		 */
		Bitmap bmp = BitmapFactory.decodeFile(inFile.getAbsolutePath(), null);
		String result = "ERROR";
		File copyFile = new File(sdpath.getAbsolutePath() + File.separator
				+ inFile.getAbsoluteFile().getName());
		try {
			FileOutputStream out = new FileOutputStream(copyFile);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
			result = copyFile.getAbsoluteFile().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean checkUniqueName(String[] strfileList,
										  String targetFileName) {
		boolean flag = false;
		// Log.d("HAN", "targetFileName:" + targetFileName);
		for (int i = 0; i < strfileList.length; i++) {
			String fileName = strfileList[i];
			// Log.d("HAN", "fileName[" + i + "]:" + fileName);

			if (fileName.equals(targetFileName)) {

				flag = true;
				break;
			}
		}

		return flag;
	}

	public static String getUniqueFileName(String locationFilePath,
										   String targetFileName) {
		int i = 0;
		File sourceFile = new File(locationFilePath);
		if (!sourceFile.isDirectory()) {
			HelpF.Log.d("HAN",
					"sourceFile.getAbsolutePath():"
							+ sourceFile.getAbsolutePath());
			HelpF.Log.d("HAN", "return null");
			return null;
		}
		String fileName = targetFileName.substring(0,
				targetFileName.lastIndexOf("."));
		String ext = targetFileName.substring(targetFileName.lastIndexOf("."));
		while ((new File(locationFilePath, targetFileName).exists())) {
			targetFileName = fileName + "[" + (i++) + "]" + ext;
		}
		/*
		 * String[] fileList = sourceFile.list(); int i = 0; String fileName =
		 * targetFileName.substring(0, targetFileName.lastIndexOf(".")); String
		 * ext = targetFileName.substring(targetFileName.lastIndexOf("."));
		 * 
		 * while (checkUniqueName(fileList, targetFileName)) { targetFileName =
		 * fileName + "[" + (i++) + "]" + ext; //
		 * Log.d("HAN","uu:"+targetFileName); }
		 */

		return targetFileName;
	}

	public static void logCatCursor(String tag, String tableName, Cursor mCursor) {
		// Log.d(tag, tableName + " rowCount" + mCursor.getCount());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mCursor.getColumnCount(); i++) {
			sb.append(String.format("%-15s", mCursor.getColumnName(i)));
		}
		HelpF.Log.d(tag, tableName + " COLUMN:" + sb.toString());
		sb = null;
		while (mCursor.moveToNext()) {
			StringBuilder sb2 = new StringBuilder();
			for (int i = 0; i < mCursor.getColumnCount(); i++) {
				sb2.append(String.format("%-15s", mCursor.getString(mCursor
						.getColumnIndex(mCursor.getColumnName(i)))));
			}
			HelpF.Log.d(tag, tableName + "   CURSR:" + sb2.toString());
			sb2 = null;
		}
		mCursor.moveToFirst();
	}

	public static File getHomeFileLoc() {
		File sdcard = Environment.getExternalStorageDirectory();
		File sdpath = new File(sdcard.getAbsolutePath() + File.separator
				+ "KnouNotice");
		if (!sdpath.exists()) {
			boolean flag = sdpath.mkdirs();
		}
		return sdpath;
	}

	public static File getFileLoc(String locationSrc) {
		File sdpath = getHomeFileLoc();
		sdpath = new File(sdpath.getAbsolutePath() + File.separator
				+ locationSrc);
		if (!sdpath.exists()) {
			boolean flag = sdpath.mkdirs();
			HelpF.Log.d("HAN", "sdpath.mkdirs():" + flag);
		}

		return sdpath;
	}

	public static boolean deleteFile(String locationSrc) {
		File sdpath = new File(locationSrc);
		return sdpath.delete();

	}

	public static Bitmap loadThumbMiniBitmap(ContentResolver contentResolver,
											 long origId) {
		if (true) {
			Bitmap bmp = Images.Thumbnails.getThumbnail(contentResolver,
					origId, Images.Thumbnails.MINI_KIND, null);
			return bmp;
		} else {
			Cursor c = Images.Thumbnails.queryMiniThumbnail(contentResolver,
					origId, Images.Thumbnails.MINI_KIND, null);
			Bitmap bmp = null;
			if (c.moveToNext()) {
				bmp = BitmapFactory.decodeFile(c.getString(c
						.getColumnIndex(Images.Thumbnails.DATA)));
			}
			c.close();
			return bmp;
		}
	}

	public static String getThumbMiniData(ContentResolver contentResolver,
										  long origId) {
		String fileName = null;
		Cursor c = Images.Thumbnails.queryMiniThumbnail(contentResolver,
				origId, Images.Thumbnails.MINI_KIND, null);
		Bitmap bmp = null;
		if (c.moveToNext()) {
			fileName = c.getString(c.getColumnIndex(Images.Thumbnails.DATA));
		}
		c.close();
		return fileName;

	}

	public static Bitmap loadThumbMicroBitmapForMini(Context context,
													 String miniFilename) {
		Bitmap bmp = null;
		try {

			Bitmap tmp = BitmapFactory.decodeFile(miniFilename, null);
			bmp = ThumbnailUtils.createImageMicroThumbnail(tmp);
			tmp.recycle();
			tmp = null;
		} catch (NullPointerException e) {
			if (bmp == null) {
				BitmapDrawable drawable = (BitmapDrawable) context
						.getResources().getDrawable(R.drawable.icon);
				bmp = drawable.getBitmap();
			}
		}
		return bmp;
	}

	public static Bitmap loadThumbMicroKindBitmap(
			ContentResolver contentResolver, long origId) {
		if (true) {
			Bitmap bmp = Images.Thumbnails.getThumbnail(contentResolver,
					origId, Images.Thumbnails.MICRO_KIND, null);
			return bmp;
		} else {
			Cursor c = Images.Thumbnails.queryMiniThumbnail(contentResolver,
					origId, Images.Thumbnails.MINI_KIND, null);
			Bitmap bmp = null;
			if (c.moveToNext()) {
				String filename = c.getString(c
						.getColumnIndex(Images.Thumbnails.DATA));
				Bitmap tmp = BitmapFactory.decodeFile(filename, null);
				bmp = ThumbnailUtils.createImageMicroThumbnail(tmp);

				tmp.recycle();
				tmp = null;
			}
			return bmp;
		}
	}

	public static Bitmap loadResizedBitmap(String filename, int width,
										   int height, boolean exact) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		if (options.outHeight > 0 && options.outWidth > 0) {
			options.inJustDecodeBounds = false;
			options.inSampleSize = 2;
			while (options.outWidth / options.inSampleSize > width
					&& options.outHeight / options.inSampleSize > height) {
				options.inSampleSize++;
			}
			options.inSampleSize--;

			bitmap = BitmapFactory.decodeFile(filename, options);
			if (bitmap != null && exact) {
				bitmap = Bitmap
						.createScaledBitmap(bitmap, width, height, false);
			}
		}
		return bitmap;
	}

	/** Get Bitmap's Width **/
	public static int getBitmapOfWidth(String fileName) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);
			return options.outWidth;
		} catch (Exception e) {
			return 0;
		}
	}

	/** Get Bitmap's height **/
	public static int getBitmapOfHeight(String fileName) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);

			return options.outHeight;
		} catch (Exception e) {
			return 0;
		}
	}

	public static void writeDebugFile(String content) {
		// 폴더명이 중복되지 않도록 해야한다.
		// 이미지와,음성파일이 복사되도록 해야한다.
		String Date = sdformat.format((new Date()));
		StringBuilder sb = new StringBuilder();
		File sdpath = HelpF.getFileLoc("debug");
		String fileName = "document" + Date.substring(0, 10) + ".txt";
		File oFile = new File(sdpath.getAbsolutePath() + File.separator
				+ fileName);
		String lastName = oFile.getAbsolutePath();
		try {
			FileOutputStream fos = new FileOutputStream(oFile);
			fos.write(content.getBytes("ksc5601"));
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String convertStringToXml(Document xmlDocument) {
		// 이것이 쓰는 소스 구나 */
		DOMSource domSource = new DOMSource(xmlDocument);
		StringWriter writer = new StringWriter();
		StreamResult result2 = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer2 = null;
		try {
			transformer2 = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			transformer2.transform(domSource, result2);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();
	}

	public static void writeFile(InputStream is, OutputStream os)
			throws IOException {
		int c = 0;
		while ((c = is.read()) != -1)
			os.write(c);
		os.flush();
	}

	public static File fileDownload(String downloadURL, String fileName) {
		File fileHome = getHomeFileLoc();

		InputStream inputStream = null;
		try {
			inputStream = new URL(downloadURL).openStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// fileName = getUniqueFileName(fileHome.getAbsolutePath(), fileName);
		File file = new File(fileHome.getAbsolutePath(), fileName);
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writeFile(inputStream, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	private static ArrayList<CategoryInfo> alCategory = new ArrayList<CategoryInfo>();
	static {//srchAnncClsNo
		alCategory.add(new CategoryInfo("입학", "150"));
		alCategory.add(new CategoryInfo("등록", "151"));
		alCategory.add(new CategoryInfo("수강신청", "152"));
		alCategory.add(new CategoryInfo("수업", "153"));
		alCategory.add(new CategoryInfo("시험/성적", "154"));
		alCategory.add(new CategoryInfo("졸업논문", "155"));
		alCategory.add(new CategoryInfo("졸업", "156"));
		alCategory.add(new CategoryInfo("계절수업", "157"));
		alCategory.add(new CategoryInfo("행사", "158"));
		alCategory.add(new CategoryInfo("기타", "245"));
		alCategory.add(new CategoryInfo("교육", "246"));
		alCategory.add(new CategoryInfo("대학원", "275"));

	}

	public static ArrayList<CategoryInfo> Category() {
		return alCategory;
	}

	private static ArrayList<DepartmentInfo> alDepartment = new ArrayList<DepartmentInfo>();
	static {//blngCd
		alDepartment.add(new DepartmentInfo("영문학과", "11"));
		alDepartment.add(new DepartmentInfo("국문학과", "10"));
		alDepartment.add(new DepartmentInfo("중문학과", "12"));
		alDepartment.add(new DepartmentInfo("불어불문학과", "13"));
		alDepartment.add(new DepartmentInfo("일본학과", "14"));
		alDepartment.add(new DepartmentInfo("행정학과", "22"));
		alDepartment.add(new DepartmentInfo("법학과", "21"));
		alDepartment.add(new DepartmentInfo("경제학과", "23"));
		alDepartment.add(new DepartmentInfo("미디어영상학과", "26"));
		alDepartment.add(new DepartmentInfo("컴퓨터과학과", "34"));
		alDepartment.add(new DepartmentInfo("경영학과", "24"));
		alDepartment.add(new DepartmentInfo("무역학과", "25"));
		alDepartment.add(new DepartmentInfo("관광학과", "27"));
		alDepartment.add(new DepartmentInfo("농학과", "31"));
		alDepartment.add(new DepartmentInfo("가정학과", "33"));
		alDepartment.add(new DepartmentInfo("정보통계학과", "35"));
		alDepartment.add(new DepartmentInfo("환경보건학과", "36"));
		alDepartment.add(new DepartmentInfo("간호학과", "37"));
		alDepartment.add(new DepartmentInfo("교육과", "41"));
		alDepartment.add(new DepartmentInfo("유아교육과", "42"));
		alDepartment.add(new DepartmentInfo("청소년교육과", "45"));
		alDepartment.add(new DepartmentInfo("문화교양학과", "43"));
	}

	public static DepartmentInfo getDepartment(String key){
		for(int i=0;i<alDepartment.size();i++)
		{
			if(alDepartment.get(i).no.equals(key))
				return alDepartment.get(i);
		}
		return null;
	}

	public static ArrayList<DepartmentInfo> Department() {
		return alDepartment;
	}

	private static ArrayList<LocationInfo> alLocationInfo = new ArrayList<LocationInfo>();
	static {
		alLocationInfo.add(new LocationInfo("서울지역대학", "010"));
		alLocationInfo.add(new LocationInfo("서울제2지역대학", "011"));
		alLocationInfo.add(new LocationInfo("인천지역대학", "020"));
		alLocationInfo.add(new LocationInfo("경기지역대학", "030"));
		alLocationInfo.add(new LocationInfo("강원지역대학", "040"));
		alLocationInfo.add(new LocationInfo("충북지역대학", "050"));
		alLocationInfo.add(new LocationInfo("대구/경북지역대학", "070"));
		alLocationInfo.add(new LocationInfo("광주/전남지역대학", "120"));
		alLocationInfo.add(new LocationInfo("대전/충남지역대학", "060"));
		alLocationInfo.add(new LocationInfo("울산지역대학", "080"));
		alLocationInfo.add(new LocationInfo("부산지역대학", "090"));
		alLocationInfo.add(new LocationInfo("경남지역대학", "100"));
		alLocationInfo.add(new LocationInfo("전북지역대학", "110"));
		alLocationInfo.add(new LocationInfo("제주지역대학", "130"));
	}


	public static LocationInfo getLocation(String key){
		for(int i=0;i<alLocationInfo.size();i++)
		{
			if(alLocationInfo.get(i).no.equals(key))
				return alLocationInfo.get(i);
		}
		return null;
	}


	public static ArrayList<LocationInfo> Location() {


		return alLocationInfo;
	}

	public static void goIntentFileView(Context context,String filePath) {

		File file = new File(filePath);
		String targetFileName = file.getName();
		String ext = targetFileName.substring(targetFileName.lastIndexOf("."));
		String type="";
		if (ext.toUpperCase().equals(".DOC")) {
			type= "application/msword";
		} else if (ext.toUpperCase().equals(".DOCX")) {
			type= "application/msword";
		} else if (ext.toUpperCase().equals(".XLS")) {
			type= "application/vnd.ms-excel";
		} else if (ext.toUpperCase().equals(".XLSX")) {
			type= "application/vnd.ms-excel";
		} else if (ext.toUpperCase().equals(".PPT")) {
			type= "application/vnd.ms-powerpoint";
		} else if (ext.toUpperCase().equals(".PPTX")) {
			type= "application/vnd.ms-powerpoint";
		} else if (ext.toUpperCase().equals(".PDF")) {
			type= "application/pdf";
		} else if (ext.toUpperCase().equals(".HWP")) {
			type= "application/x-hwp";
		}


		ext=ext.replace(".", "");
		if(type.equals("")){
			Toast.makeText(context, ext +" 확장자는 열 수 없는 파일입니다.", Toast.LENGTH_SHORT).show();
			return;
		}

		Uri path = Uri.fromFile(file);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(path, type);

		// DOC, DOCX, XLS, XLSX, PPT, PPTX

		// 이거안됨
		// intent.setDataAndType(path,new
		// MimetypesFileTypeMap().getContentType(file));
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			context.startActivity(intent);
		}catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context,"파일을 읽을 수있는 프로그램이 없습니다.",Toast.LENGTH_SHORT).show();

		}
	}

}
