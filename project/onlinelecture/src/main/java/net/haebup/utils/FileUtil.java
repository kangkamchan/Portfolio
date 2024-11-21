package net.haebup.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class FileUtil {
	public FileUtil() {
		
	}
	//파일업로드
	public static String uploadFile(HttpServletRequest req, String saveDir, String fileTagName) throws ServletException, IOException {
		System.out.println("--------------------------------------------");
		System.out.println("FileUtil - upload start");
		//Part 객체
		Part part = req.getPart(fileTagName);
		//헤더값에서 파일 객체 부분 읽기
		String partHeader = part.getHeader("content-disposition");
		//파일객체 부분 : form-data;name="attatchFile";filename="파일명"
		String oFileName = partHeader.split("filename=")[1].trim().replace("\"","");
		if(!oFileName.isEmpty()) {
			System.out.println("oFileName : " + oFileName);
			//파일 업로드 경로 	
			part.write(saveDir + File.separator + oFileName);
		}else {
			return null;
		}
				
		System.out.println("partHeader : " + partHeader);

		System.out.println("FileUtil - upload end");
		System.out.println("--------------------------------------------");
		return oFileName;
	}
	//멀티파일업로드
	public static List<String> uploadMultiFile(HttpServletRequest req, String saveDir, String fileTagName) throws ServletException, IOException{
		System.out.println("--------------------------------------------");
		System.out.println("FileUtil - uploadMultiFile start");
		List<String> fileList = new ArrayList<String>();
		int cnt = 0;
		Collection<Part> parts = req.getParts();
		for(Part part : parts) {
			if(!part.getName().equals(fileTagName)) {
				continue;
			}
			cnt++;
			String partHeader = part.getHeader("content-disposition");
			String oFileName = partHeader.split("filename=")[1].trim().replace("\"","");
			if(!oFileName.isEmpty()) {
				System.out.println("oFileName : " + oFileName);
				//파일 업로드 경로 	
				part.write(saveDir + File.separator + oFileName);
			}
			fileList.add(oFileName);
		}
		if(cnt!=fileList.size()) {
			for(String fileName : fileList) {
				fileDelete(req,saveDir,fileName);
			}
			System.out.println("FileUtil - uploadMultiFile end");
			System.out.println("--------------------------------------------");
			return null;
		}
		System.out.println("FileUtil - uploadMultiFile end");
		System.out.println("--------------------------------------------");
		return fileList;
	}
	//사용자가 등록한 파일 수를 반환
	public static int getMultiFileCnt (HttpServletRequest req, String fileTagName) throws ServletException, IOException{
		int cnt = 0;
		Collection<Part> parts = req.getParts();
		for(Part part : parts) {
			if(!part.getName().equals(fileTagName)) {
				continue;
			}
			cnt++;
		}
		return cnt;
	}
	public static Map<String,Map<String,String>> renameMultiFile(List<String> fileList, HttpServletRequest req, String saveDir, String fileTagName) throws ServletException, IOException {
		System.out.println("--------------------------------------------");
		System.out.println("FileUtil - renameMultiFile start");
		Map<String,Map<String,String>> fileListMap = new HashMap<String,Map<String,String>>();
		for(String oFileName : fileList) {
			fileListMap.put(oFileName, fileRename(saveDir,oFileName));
		}
		System.out.println("FileUtil - renameMultiFile end");
		System.out.println("--------------------------------------------");
		return fileListMap;
	}
	
	// 업로드된 파일명 변경
	public static Map<String, String> fileRename(String saveDir, String fileName) {
		if(saveDir == null || saveDir.isEmpty() || fileName==null ||fileName.isEmpty()) {
			return null;
		}
		//파일확장자 추출
		String fileExt = fileName.substring(fileName.lastIndexOf("."));
		//새로운 파일명 조합
		String newName = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date()) + fileExt;
		//기존파일명 -> 새로운파일명 변경
		File oldFile = new File(saveDir + File.separator + fileName);
		File newFile = new File(saveDir + File.separator + newName);
		oldFile.renameTo(newFile);
		
		Map<String, String> fMap = new HashMap<String,String>();
		fMap.put("filePath", saveDir);
		fMap.put("orgFileName", fileName.replace(fileExt, ""));
		fMap.put("newFileName", newName.replace(fileExt, ""));
		fMap.put("fileExt",fileExt);
		fMap.put("fileSize", String.valueOf(oldFile.length()));
		
		return fMap;
	}
	// 업로드된 파일명 변경
	public static Map<String, String> fileRename(String saveDir, String orgFileName, String newFileName) {
		if(saveDir == null || saveDir.isEmpty() || orgFileName==null ||orgFileName.isEmpty()) {
			return null;
		}
		//파일확장자 추출
		String fileExt = orgFileName.substring(orgFileName.lastIndexOf("."));
		//새로운 파일명 조합
		//기존파일명 -> 새로운파일명 변경
		File oldFile = new File(saveDir + File.separator + orgFileName);
		File newFile = new File(saveDir + File.separator + newFileName);
		oldFile.renameTo(newFile);
		
		Map<String, String> fMap = new HashMap<String,String>();
		fMap.put("filePath", saveDir);
		fMap.put("orgFileName", orgFileName);
		fMap.put("newFileName", newFileName);
		fMap.put("fileExt",fileExt);
		fMap.put("fileSize", String.valueOf(oldFile.length()));
		
		return fMap;
	}
	//파일다운로드
	public static void downloadFile(HttpServletRequest req, HttpServletResponse res, String saveDir, String orgFileName, String outFileName)
			throws ServletException, IOException {
		//dir(물리적경로) : D:\java7\~~~\'uploads
		//dir(컨텍스트경로) : uploads
		//String realPath = req.getServletContext().getRealPath(saveDir);
		try {
			File file = new File(saveDir,orgFileName);
			InputStream is = new FileInputStream(file);
			
			//클라이언트의 브라우저 체크
			String client = req.getHeader("User-Agent");
			if(client.indexOf("WOW64") == -1) {
				outFileName = new String(outFileName.getBytes("UTF-8"),"ISO-8859-1");
			}else {
				outFileName = new String(outFileName.getBytes("KES5601"),"ISO-8859-1");
			}
			
			// 다운로드할 파일의 응답헤더 설정
			res.reset();
			res.setContentType("application/octet-stream");
			res.setHeader("Content-Disposition","attachment; filename=\""+outFileName + "\"");
			res.setHeader("Content-Length",String.valueOf(file.length()));

			//출력스트림 초기화
			
			//출력스트림 객체 생성
			OutputStream out = res.getOutputStream();
			
			//출력스트림에 파일 내용 출력시킴
			byte[] b = new byte[(int)file.length()];
			int readBuffer = 0;
			while((readBuffer = is.read(b))>0) {
				out.write(b,0,readBuffer);
			}
			//입출력스트림 닫음
			is.close();
			out.close();
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//파일삭제
	public static boolean fileDelete(HttpServletRequest req, String dir, String fileName){
		//파일이 존재할경우 삭제
		System.out.println("--------------------------------------------");
		System.out.println("FileUtil - delete start");
		try {
			System.out.println("FileUtil - delete - filedir + fileName : " +dir+ File.separator +fileName);
			File file = new File(dir+ File.separator +fileName);
			if(file.exists()) {
				file.delete();
				System.out.println(dir+ File.separator +fileName + "삭제완료");
				System.out.println("FileUtil - delete end");
				System.out.println("-------------------------------------------");
				return true;
			}else {
				System.out.println("파일없음");
			}
		}catch(Exception e) {
			System.out.println("파일삭제시 에러발생");
			System.out.println(e.getMessage());
			System.out.println("FileUtil - delete end");
			System.out.println("-------------------------------------------");
			return false;
		}
		
		System.out.println("FileUtil - delete end");
		System.out.println("-------------------------------------------");
		return false;
	}
}
