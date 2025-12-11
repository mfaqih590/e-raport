package id.kai.eraport.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AppUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);
    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 100;

    private static boolean isCollectionEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isObjectEmpty(Object object) {
        if (object == null) return true;
        else if (object instanceof String) {
            return ((String) object).trim().length() == 0;
        } else if (object instanceof Collection) {
            return isCollectionEmpty((Collection<?>) object);
        }
        return false;
    }

    public static String handleFileUploadMultipart(MultipartFile file, String SAVE_DIR){
        //Format di windows, karena tidak bisa menggunakan colon (:)
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        LOGGER.info("cek : "+extension);

        if(extension.equals("jpg") || extension.equals("jpeg")||extension.equals("png")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String dateString = format.format(new Date());
            String outputFilePath = "file" + "_" + dateString + "." + extension;

            //Format di linux dapat menggunakan colon (:)
            //String outputFilePath = "file" + "_" + new Timestamp(System.currentTimeMillis()) + "." + extension;

            File uploadedFile = new File(SAVE_DIR, outputFilePath);
            if (!file.isEmpty()) {
                try {
                    File fileSaveDir = new File(SAVE_DIR);
                    if (!fileSaveDir.exists()) {
                        LOGGER.info("Creates the save directory if it does not exists");
                        fileSaveDir.mkdirs();
                    }

                    uploadedFile.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(uploadedFile);
                    fileOutputStream.write(file.getBytes());
                    fileOutputStream.close();

                    return outputFilePath;
                } catch (IOException e) {
                    LOGGER.error("You failed to upload file => " + e.getMessage());
                    return "1";
                }
            }else {
                LOGGER.error("You failed to upload file because the file is empty.");
                return "2";
            }
        }else{
            LOGGER.error("You failed to upload file because extension file incorrect.");
            return "3";
        }
    }

    public static String handleFileUpload(String scanFile, String SAVE_DIR, String ext) {

        //Format di windows, karena tidak bisa menggunakan colon (:)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String dateString = format.format(new Date());
        String outputFilePath = "file" + "_" + dateString + "." + ext;

        //Format di linux dapat menggunakan colon (:)
//        String outputFilePath = "file" + "_" + new Timestamp(System.currentTimeMillis()) + "." + ext;

        if (!scanFile.isEmpty()) {

            try {
                LOGGER.info("Decoding base64");

                File fileSaveDir = new File(SAVE_DIR);
                if (!fileSaveDir.exists()) {
                    LOGGER.info("Creates the save directory if it does not exists");
                    fileSaveDir.mkdirs();
                }

                // decode the string and write to file
                byte[] decodedBytes = Base64
                        .getMimeDecoder()
                        .decode(scanFile);

                // create output file
                File outputFile = new File(fileSaveDir
                        //.getParentFile()
                        .getAbsolutePath()
                        + "/" + outputFilePath);

                FileUtils.writeByteArrayToFile(outputFile, decodedBytes);
                LOGGER.info("nyoba tampil"+String.valueOf(outputFile));

//                File input = new File(String.valueOf(outputFile));
//                BufferedImage image = ImageIO.read(input);
//                int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
//
//                BufferedImage resized = resizeImage(image,type);
//                File output = new File(String.valueOf(outputFile));
//                ImageIO.write(resized, ext, output);

                return outputFilePath;

            } catch (Exception e) {
                LOGGER.error("You failed to upload file => " + e.getMessage());
                return "1";
            }
        } else {
            LOGGER.error("You failed to upload file because the file is empty.");
            return "2";
        }

    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }

    public static String handleConvert(String scanFile, String SAVE_DIR) throws IOException {
        String img = SAVE_DIR+"/"+scanFile;
        byte[] fileContent = FileUtils.readFileToByteArray(new File(img));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }

    public static String handleFileRemove(String SAVE_DIR, String urlFile) {
        if (!urlFile.isEmpty()) {
            try {
                LOGGER.info("path : "+SAVE_DIR + "/" + urlFile);
                FileUtils.touch(new File(SAVE_DIR + "/" + urlFile));
                File fileToDelete = FileUtils.getFile(SAVE_DIR + "/" + urlFile);
                boolean success = FileUtils.deleteQuietly(fileToDelete);
                LOGGER.info("success delete file");
                return "success";

            } catch (Exception e) {
                LOGGER.error("You failed to delete file => " + e.getMessage());
                return "1";
            }
        } else {
            LOGGER.error("You failed to delete file because the file is empty.");
            return "2";
        }
    }

    public static String dayFormatDuration(long duration) {

        long diffDays = duration / (24 * 60 * 60 * 1000);

        return String.format("%02d", diffDays);
    }

    public static String timeFormatDuration(long duration) {

        long diffSeconds = duration / 1000 % 60;
        long diffMinutes = duration / (60 * 1000) % 60;
        long diffHours = duration / (60 * 60 * 1000) % 24;
        long diffDays = duration / (24 * 60 * 60 * 1000);

        return String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
    }

    public static boolean checkArrayContains(String[] arr, String targetValue) {
        Set<String> set = new HashSet<String>(Arrays.asList(arr));
        return set.contains(targetValue);
    }

    public static String convertToCurrentTimeZone(String Date) {
        String converted_date = "";
        try {

            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = utcFormat.parse(String.valueOf(Date));

            DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTFormat.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));

            converted_date =  currentTFormat.format(date);
        }catch (Exception e){ e.printStackTrace();}

        return converted_date;
    }

//get the current time zone

    public static String getCurrentTimeZone(){
        TimeZone tz = Calendar.getInstance().getTimeZone();
//        System.out.println(tz.getDisplayName());
        return tz.getID();
    }
}
