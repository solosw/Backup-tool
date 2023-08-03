package solosw.ServerHelper.Controller;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.rmi.server.ExportException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.*;



public class ZipHelper {

  public  static   int BUFFER ;

   public static  String hallFilePath;

   public static  String outPutPath;

    //由此开始是所有相关的工具方法
    public static  String emailTo;
    public  static  String password;
    public  static  int port;
    public  static  String host;
    public static  void compressDirectory(File sourceFile, File zipFile) throws IOException {

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            // 设置压缩方法
            zos.setMethod(ZipOutputStream.DEFLATED);
            zos.setLevel(Deflater.BEST_COMPRESSION); // 默认为-1,压缩级别，1速度快，效率低，9 速度满，效率高
            // zos.setLevel(Deflater.BEST_SPEED);
            zos.setComment("zip文件说明");
            // 处理文件夹
            if (sourceFile.exists() && sourceFile.isDirectory() && Objects.nonNull(sourceFile.listFiles())){
                Arrays.stream(Objects.requireNonNull(sourceFile.listFiles())).forEach(file -> {
                    addZipFile(file, zos);
                    System.out.println(file.getName());
               });
                addZipFile(sourceFile, zos);
            }else{
                addZipFile(sourceFile, zos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("压缩完成！");
    }
    private static void addZipFile(File file, ZipOutputStream zos){
        if (!file.exists()||file.isDirectory()||file.isHidden() ){
            System.out.println("文件不存在或该文件为文件夹，请检查");
            return;
        }
        try {
            // 读入文件
            FileInputStream fis = new FileInputStream(file);
            if(fis==null)
            {
                return;
            }
            // 创建压缩对象并设置一些属性
            ZipEntry entry = new ZipEntry(file.getName());
            entry.setMethod(ZipEntry.DEFLATED); // 压缩方法默认为DEFLATED
            // entry.setMethod(ZipEntry.STORED); // STORED（不压缩）。当使用STORED压缩方法时，需要设置未压缩的数据大小和CRC-32校验和，否则压缩和解压缩时会出现错误。
            entry.setSize(file.length()); // 设置未压缩的数据大小，这里设置的是文件大小
            // 计算 CRC-32 校验码
            // byte[] data = Files.readAllBytes(file.toPath());
            // CRC32 crc = new CRC32();
            // crc.update(data);
            // entry.setCrc(crc.getValue()); // 设置CRC-32校验和，用于保证压缩后的数据完整性，尽量别手动设置，可以通过CRC-32计算
            entry.setCompressedSize(file.length()); // 设置压缩后的数据大小，这里设置的是使用DEFLATED方法压缩后的数据大小
            entry.setExtra(new byte[]{}); // 设置额外的数据，这里设置为空
            entry.setComment("file comment"); // 设置ZipEntry的注释，即文件说明
            entry.setCreationTime(FileTime.from(Instant.now())); // 设置文件的创建时间
            entry.setLastAccessTime(FileTime.from(Instant.now())); // 设置文件的最后访问时间
            entry.setLastModifiedTime(FileTime.from(Instant.now())); // 设置文件的最后修改时间。
            // 向ZIP输出流中添加一个ZIP实体，构造方法中的name参数指定文件在ZIP包中的文件名
            zos.putNextEntry(entry);
            // 向ZIP实体中写入内容
            byte[] buf = new byte[1024*4];
            int MaxSize=1024*4*50;
            int len;
            int cnt=0;
            while ((len = fis.read(buf)) > 0) {
                zos.write(buf, 0, len);
                cnt+=len;
                if(cnt>=MaxSize) break;
            }
            // 关闭ZipEntry
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

