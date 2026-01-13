package id.kai.eraport.common.util;

import id.kai.eraport.exception.BadRequestException;
import id.kai.eraport.exception.DatabaseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utility class untuk kebutuhan umum aplikasi:
 * - File upload
 * - File delete
 * - Konversi Base64
 * - Format waktu
 */
public final class AppUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);

    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 100;

    private AppUtil() {
        // Mencegah instansiasi
    }

    /**
     * Mengecek apakah object kosong (null, string kosong, atau collection kosong)
     */
    public static boolean isObjectEmpty(Object object) {
        if (object == null) return true;
        if (object instanceof String str) {
            return str.trim().isEmpty();
        }
        if (object instanceof Collection<?> col) {
            return col.isEmpty();
        }
        return false;
    }

    /**
     * Upload file multipart (jpg, jpeg, png)
     * @return nama file yang tersimpan
     */
    public static String handleFileUploadMultipart(MultipartFile file, String saveDir) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File tidak boleh kosong");
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        Set<String> allowedExt = Set.of("jpg", "jpeg", "png");

        if (!allowedExt.contains(extension)) {
            throw new BadRequestException("Format file tidak didukung");
        }

        String fileName = generateFileName(extension);
        Path targetPath = Path.of(saveDir, fileName);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, file.getBytes());
            return fileName;
        } catch (IOException e) {
            LOGGER.error("Gagal upload file", e);
            throw new DatabaseException("Gagal menyimpan file");
        }
    }

    /**
     * Upload file dari string Base64
     * @return nama file yang tersimpan
     */
    public static String handleFileUploadBase64(String base64, String saveDir, String ext) {

        if (base64 == null || base64.isBlank()) {
            throw new BadRequestException("Data file kosong");
        }

        String fileName = generateFileName(ext);
        Path targetPath = Path.of(saveDir, fileName);

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, decodedBytes);
            return fileName;
        } catch (Exception e) {
            LOGGER.error("Gagal upload base64", e);
            throw new DatabaseException("Gagal menyimpan file");
        }
    }

    /**
     * Menghapus file berdasarkan nama file
     */
    public static void handleFileRemove(String saveDir, String fileName) {

        if (fileName == null || fileName.isBlank()) {
            throw new BadRequestException("Nama file kosong");
        }

        try {
            FileUtils.deleteQuietly(new File(saveDir, fileName));
        } catch (Exception e) {
            LOGGER.error("Gagal hapus file", e);
            throw new DatabaseException("Gagal menghapus file");
        }
    }

    /**
     * Mengubah file menjadi Base64 string
     */
    public static String convertFileToBase64(String saveDir, String fileName) {

        try {
            byte[] bytes = Files.readAllBytes(Path.of(saveDir, fileName));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            LOGGER.error("Gagal convert file ke base64", e);
            throw new DatabaseException("Gagal membaca file");
        }
    }

    /**
     * Resize gambar ke ukuran default
     */
    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {

        BufferedImage resizedImage =
                new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);

        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        graphics.dispose();

        return resizedImage;
    }

    /**
     * Format durasi ke jumlah hari
     */
    public static String dayFormatDuration(long durationMillis) {
        long days = Duration.ofMillis(durationMillis).toDays();
        return String.format("%02d", days);
    }

    /**
     * Format durasi ke HH:mm:ss
     */
    public static String timeFormatDuration(long durationMillis) {

        Duration duration = Duration.ofMillis(durationMillis);

        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Mengecek apakah array mengandung nilai tertentu
     */
    public static boolean checkArrayContains(String[] arr, String targetValue) {
        return Set.of(arr).contains(targetValue);
    }

    /**
     * Konversi waktu UTC ke timezone lokal server
     */
    public static String convertToCurrentTimeZone(String utcDate) {

        Instant instant = Instant.parse(utcDate);
        ZonedDateTime zonedDateTime =
                instant.atZone(ZoneId.systemDefault());

        return zonedDateTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
    }

    /**
     * Mendapatkan timezone server saat ini
     */
    public static String getCurrentTimeZone() {
        return ZoneId.systemDefault().getId();
    }

    /**
     * Generate nama file unik berdasarkan waktu saat ini
     */
    private static String generateFileName(String extension) {
        return "file_" +
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                ) +
                "." + extension;
    }
}
