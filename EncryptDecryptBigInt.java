import java.math.BigInteger;

public class EncryptDecryptBigInt {
    public static final BigInteger e = new BigInteger("65537"); // Public exponent (common choice)
    private BigInteger n;  // Modulus
    private BigInteger d;  // Private exponent

    // Constructor to generate keys
    public EncryptDecryptBigInt(BigInteger p, BigInteger q) {
        n = p.multiply(q);  // Modulus n
        BigInteger phi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)); // Euler's Totient
        d = e.modInverse(phi_n);  // Compute the modular inverse to get private exponent
    }

    // Encrypt data
    public BigInteger encrypt(BigInteger data) {
        return data.modPow(e, n);  // Encrypt using public exponent e and modulus n
    }

    // Decrypt data
    public BigInteger decrypt(BigInteger data) {
        return data.modPow(d, n);  // Decrypt using private exponent d and modulus n
    }

    // Getter for the public key components
    public BigInteger getPublicKeyModulus() {
        return n;
    }

    public BigInteger getPublicKeyExponent() {
        return e;
    }

    // Getter for the private key component
    public BigInteger getPrivateKey() {
        return d;
    }
}
