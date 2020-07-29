package io.nuite.common.utils;

import io.nuite.common.exception.RRException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;

public class ZipUtils {
    /**
     * 递归压缩文件夹
     *
     * @param srcRootDir 压缩文件夹根目录的子路径
     * @param file       当前递归压缩的文件或目录对象
     * @param zos        压缩文件存储对象
     * @throws Exception
     */
    private static void zip(String srcRootDir, File file, ZipOutputStream zos) throws Exception {
        if (file == null) {
            return;
        }

        //如果是文件，则直接压缩该文件
        if (file.isFile()) {
            int count, bufferLen = 1024;
            byte data[] = new byte[bufferLen];

            //获取文件相对于压缩文件夹根目录的子路径
            String subPath = file.getAbsolutePath();
            int index = subPath.indexOf(srcRootDir);
            if (index != -1) {
                subPath = subPath.substring(srcRootDir.length() + File.separator.length());
            }
            ZipEntry entry = new ZipEntry(subPath);
            zos.putNextEntry(entry);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while ((count = bis.read(data, 0, bufferLen)) != -1) {
                zos.write(data, 0, count);
            }
            bis.close();
            zos.closeEntry();
        }
        //如果是目录，则压缩整个目录
        else {
            //压缩目录中的文件或子目录
            File[] childFileList = file.listFiles();
            for (int n = 0; n < childFileList.length; n++) {
                childFileList[n].getAbsolutePath().indexOf(file.getAbsolutePath());
                zip(srcRootDir, childFileList[n], zos);
            }
        }
    }

    /**
     * 对文件或文件目录进行压缩
     *
     * @param srcPath     要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
     * @param zipPath     压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
     * @param zipFileName 压缩文件名
     * @throws Exception
     */
    public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception {
        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(zipPath) || StringUtils.isEmpty(zipFileName)) {
            throw new RRException("path is null");
        }
        CheckedOutputStream cos;
        ZipOutputStream zos = null;
        try {
            File srcFile = new File(srcPath);

            //判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
            if (srcFile.isDirectory() && zipPath.contains(srcPath)) {
                throw new RRException("zipPath must not be the child directory of srcPath.");
            }

            //判断压缩文件保存的路径是否存在，如果不存在，则创建目录
            File zipDir = new File(zipPath);
            if (!zipDir.exists() || !zipDir.isDirectory()) {
                zipDir.mkdirs();
            }

            //创建压缩文件保存的文件对象
            String zipFilePath = zipPath + File.separator + zipFileName;
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
                SecurityManager securityManager = new SecurityManager();
                securityManager.checkDelete(zipFilePath);
                //删除已存在的目标文件
                zipFile.delete();
            }

            cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
            zos = new ZipOutputStream(cos);

            //如果只是压缩一个文件，则需要截取该文件的父目录
            String srcRootDir = srcPath;
            if (srcFile.isFile()) {
                int index = srcPath.lastIndexOf(File.separator);
                if (index != -1) {
                    srcRootDir = srcPath.substring(0, index);
                }
            }
            //调用递归压缩方法进行目录或文件压缩
            zip(srcRootDir, srcFile, zos);
            zos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 解压货品素材文件到指定目录
     * @param decpPath 解压路径，比如C:\\home\\myblog\\project\\
     * @param multipartFile 上传压缩文件
     */
    public static Map<String, Map<String, Integer>> unZipFiles(String decpPath, MultipartFile multipartFile){
		try {
			//1.创建解压目录
			File decpPathDir = new File(decpPath);
			if (!decpPathDir.exists()) {
				decpPathDir.mkdirs();
			}

			//读取压缩文件的输入流
			ZipInputStream zipInputStream = new ZipInputStream(multipartFile.getInputStream(), Charset.forName("GBK"));
			//压缩文档中每一个项为一个zipEntry对象，可以通过getNextEntry方法获得，zipEntry可以是文件，也可以是路径，比如abc/test/路径下
			ZipEntry zipEntry;
			Map<String, Integer> Dmap = new HashMap<String, Integer>(); //详情图片个数
			Map<String, Integer> Mmap = new HashMap<String, Integer>(); //轮播图图片个数
			Map<String, Integer> Vmap = new HashMap<String, Integer>(); //视频是否存在
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
			    String zipEntryName = zipEntry.getName().replaceAll("\\+", "/").replaceAll("货品素材/", "");
			    if(zipEntryName.equals("")) {
			    	continue;
			    }
			    /**  货品素材文件 个性化路径处理 （针对素材路径的处理，各种素材个数的统计）start **/
				String goodId = zipEntryName.substring(0, zipEntryName.indexOf("/"));
				
				if(zipEntryName.contains("/D/")) {
					zipEntryName = zipEntryName.replaceAll("/D/", "/");
					if(Dmap.containsKey(goodId)) {
						Dmap.put(goodId, Dmap.get(goodId) + 1);
					} else {
						Dmap.put(goodId, 0);
					}
				}
				if(zipEntryName.contains("/M/")) {
					zipEntryName = zipEntryName.replaceAll("/M/", "/");
					if(Mmap.containsKey(goodId)) {
						Mmap.put(goodId, Mmap.get(goodId) + 1);
					} else {
						Mmap.put(goodId, 0);
					}
				}
				if(zipEntryName.contains("/video.mp4")) {
					Vmap.put(goodId, 1);
				}
			    /**  货品素材文件 个性化路径处理  （针对素材路径的处理，各种素材个数的统计）end**/
			    
			    //将目录中的1个或者多个\替换为/，因为在windows目录下，以\或者\\为文件目录分隔符，linux却是/
			    String outPath = (decpPath + zipEntryName).replaceAll("\\+", "/");
			    
			    //判断路径是否存在,不存在则创建文件路径
			    File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			    if (!file.exists()) {
			        file.mkdirs();
			    }
			    
			    //判断文件全路径是否为文件夹,如果是,在上面三行已经创建,不需要解压
			    if (new File(outPath).isDirectory()) {
			        continue;
			    }

			    OutputStream outputStream = new FileOutputStream(outPath);
			    byte[] bytes = new byte[4096];
			    int len;
			    //当read的返回值为-1，表示碰到当前项的结尾，而不是碰到zip文件的末尾
			    while ((len = zipInputStream.read(bytes)) > 0) {
			        outputStream.write(bytes, 0, len);
			    }
			    outputStream.close();
			    //必须调用closeEntry()方法来读入下一项
			    zipInputStream.closeEntry();
			}
			zipInputStream.close();

			Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
			result.put("Dmap", Dmap);
			result.put("Mmap", Mmap);
			result.put("Vmap", Vmap);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

}
