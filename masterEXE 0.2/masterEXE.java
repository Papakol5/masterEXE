import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class masterEXE {

    // .sig dosyasını .exe dosyasına enjekte eden yardımcı metot
    public static void injectSigToExe(String exeFilePath, String sigFilePath) {
        try {
            File exeFile = new File(exeFilePath);
            File sigFile = new File(sigFilePath);

            // EXE dosyasını oku
            byte[] exeBytes = new byte[(int) exeFile.length()];
            try (FileInputStream exeInputStream = new FileInputStream(exeFile)) {
                exeInputStream.read(exeBytes);
            }

            // SIG dosyasını oku
            byte[] sigBytes = new byte[(int) sigFile.length()];
            try (FileInputStream sigInputStream = new FileInputStream(sigFile)) {
                sigInputStream.read(sigBytes);
            }

            // EXE ve SIG dosyalarını birleştir
            try (FileOutputStream outputStream = new FileOutputStream(exeFile, true)) {
                outputStream.write(sigBytes);
            }

            System.out.println("SIG dosyası EXE dosyasına başarıyla eklendi.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Dosya okuma/yazma sırasında bir hata oluştu.");
        }
    }

    public static void main(String[] args) {
        try {
            MEDSL medsl = new MEDSL();
            Scanner scanner = new Scanner(System.in);

            System.out.println("1. Dosya İmzala");
            System.out.println("2. İmza Doğrula");
            System.out.println("3. SIG Dosyasını EXE'ye Enjekte Et");
            System.out.print("Bir seçenek girin: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // satır sonu karakterini tüket

            if (choice == 1) {
                System.out.print("Dosya yolunu girin: ");
                String filePath = scanner.nextLine();
                String message = new String(Files.readAllBytes(Paths.get(filePath)));
                String signature = medsl.sign(message);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".sig"))) {
                    writer.write(signature);
                }
                System.out.println("Dosya başarıyla imzalandı.");
            } else if (choice == 2) {
                System.out.print("Dosya yolunu girin: ");
                String filePath = scanner.nextLine();
                System.out.print("İmza dosyasını girin: ");
                String sigPath = scanner.nextLine();
                String message = new String(Files.readAllBytes(Paths.get(filePath)));
                String signature = new String(Files.readAllBytes(Paths.get(sigPath)));
                boolean isVerified = medsl.verify(message, signature);
                System.out.println("Doğrulama: " + (isVerified ? "Geçerli" : "Geçersiz"));
            } else if (choice == 3) {
                System.out.print("EXE dosyasının yolunu girin: ");
                String exeFilePath = scanner.nextLine();
                System.out.print("SIG dosyasının yolunu girin: ");
                String sigFilePath = scanner.nextLine();
                injectSigToExe(exeFilePath, sigFilePath);
            } else {
                System.out.println("Geçersiz seçenek.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

