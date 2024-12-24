public class EncryptionDecryption {
    public static final int e = 65537; // Public exponent (common choice)
    private final int n;  // Modulus
    private final int d;  // Private exponent

    // Constructor to generate keys
    public EncryptionDecryption(int p, int q) {
        n = p * q;  // Modulus n
        int phi_n = (p - 1) * (q - 1); // Euler's Totient
        d = ModInverse(e, phi_n);  // Compute the modular inverse to get private exponent
    }

    static int ModInverse(int val, int phi_n) {
        int d = 0;
        int x1 = 0;
        int x2 = 1;
        int y1 = 1;
        int tempPhiN = phi_n;

        while (val > 0) {
            int temp1 = tempPhiN / val;
            int temp2 = tempPhiN - temp1 * val;

            tempPhiN = val;
            val = temp2;

            int x = x2 - temp1 * x1;
            int y = d - temp1 * y1;

            x2 = x1;
            x1 = x;

            d = y1;
            y1 = y;
        }

        if (tempPhiN == 1) return (d + phi_n) % phi_n;
        return -1;
    }

    // Encrypt data
    public int encrypt(int data) {
        return ModularExponentiation(data, e, n);  // Encrypt using public exponent e and modulus n
    }

    // Decrypt data
    public int decrypt(int data) {
        return ModularExponentiation(data, d, n);  // Decrypt using private exponent d and modulus n
    }

    static int ModularExponentiation(int base, int exponent, int modulus) {
        int acumulator = 1;
        base = base % modulus;

        while (exponent > 0) {
            if ((exponent & 1) == 1) acumulator = (acumulator * base) % modulus;

            exponent = exponent >> 1;
            base = (base * base) % modulus;
        }

        return acumulator;
    }

    static int ChineseRemainderTheorem(int c1, int c2, int p, int q) {
        int m1 = c1 * q * ModInverse(q, p) % (p * q);
        int m2 = c2 * p * ModInverse(p, q) % (p * q);

        return (m1 + m2) % (p * q);
    }


    // Getter for the public key components
    public int getPublicKeyModulus() {
        return n;
    }

    public int getPublicKeyExponent() {
        return e;
    }

    // Getter for the private key component
    public int getPrivateKey() {
        return d;
    }
}
