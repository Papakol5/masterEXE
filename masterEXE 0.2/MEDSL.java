import java.security.*;
import java.util.Base64;

public class MEDSL {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public MEDSL() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes());
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public boolean verify(String message, String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(message.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static void main(String[] args) {
        try {
            MEDSL medsl = new MEDSL();
            String message = "Bu bir test mesajıdır.";
            String signature = medsl.sign(message);
            boolean isVerified = medsl.verify(message, signature);

            System.out.println("Mesaj: " + message);
            System.out.println("İmza: " + signature);
            System.out.println("Doğrulama: " + (isVerified ? "Geçerli" : "Geçersiz"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
