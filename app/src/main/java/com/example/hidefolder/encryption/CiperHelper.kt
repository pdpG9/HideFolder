package com.example.hidefolder.encryption

import com.example.hidefolder.ui.login.log
import com.google.android.gms.common.util.Hex
import java.math.BigInteger
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


object CiperHelper {
    //hash
    private var messageDigest: MessageDigest
    private var digest: ByteArray

    //asimmmetric
    private var publicKey: Key? = null;
    private var privateKey: Key? = null;

    //simmmetric
    private lateinit var secretKey: SecretKey;

    init {
        messageDigest = MessageDigest.getInstance("MD5");
        digest = ByteArray(0)

        ///asimmetric
        try {
            val keyFactory = KeyPairGenerator.getInstance("RSA")

            keyFactory.initialize(2048)
            val kp = keyFactory.genKeyPair()
            publicKey = kp.public
            privateKey = kp.private
        } catch (e: Exception) {
            println(("Crypto RSA key pair error ${e.message}"))
        }
        ///simmetric
        try {
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(256)
            secretKey = keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        encrByAsimmetric("hello")
        decrByAsimmetric("hello")
    }

    fun encrBySimmetric(message: String): String {
        val endryptedString = makeAes(message.toByteArray(), Cipher.ENCRYPT_MODE)
        var temp = ""
        endryptedString.forEach {
            temp += it.toInt().toChar()
        }
        return temp
    }

    fun decrBySimmetric(endryptedString: String): String {
        println("decrBySimmetric")
        val decodedString = makeAes(endryptedString.toByteArray(), Cipher.DECRYPT_MODE)
        println(decodedString.size.toString())
        var temp = ""
        decodedString.forEach {
            temp += it.toInt().toChar()
        }
        println("return matn $temp")
        return temp

    }

    private fun makeAes(rawMessage: ByteArray, ciperMode: Int): ByteArray {
        try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(ciperMode, this.secretKey)
            val output: ByteArray = cipher.doFinal(rawMessage)
            return output
        } catch (e: Exception) {
            e.printStackTrace()
            return ByteArray(0)
        }
    }

    fun encrByAsimmetric(message: String): String {
        val encrMess = makeCiperRSA(message.toByteArray(), Cipher.ENCRYPT_MODE)

        var temp = ""
        encrMess.forEach {
            temp += it.toInt().toChar()
        }
        return temp

    }

    fun decrByAsimmetric(encrMessage: String): String {
        val decrMess = makeCiperRSA(encrMessage.toByteArray(), Cipher.DECRYPT_MODE)

        var temp = ""
        decrMess.forEach {
            temp += it.toInt().toChar()
        }
        return temp
    }

    private fun makeCiperRSA(rawMessage: ByteArray, cipherType: Int): ByteArray {
        try {
            val c = Cipher.getInstance("RSA");
            if (cipherType == Cipher.ENCRYPT_MODE) {
                c.init(cipherType, privateKey);
            } else {
                c.init(cipherType, publicKey);
            }
            val output = c.doFinal(rawMessage);
            return output
        } catch (e: Exception) {
            println(("Crypto RSA encryption error: ${e.message}"))
            return ByteArray(0)
        }
    }

    fun getHash(message: String): String {
        try {
            messageDigest.reset()
            messageDigest.update(message.toByteArray())
            digest = messageDigest.digest()
        } catch (e: Exception) {
            println(e.message)
        }

        val bigInt = BigInteger(1, digest);
        var md5Hex = bigInt.toString(16);

        while (md5Hex.length < 32) {
            md5Hex = "0" + md5Hex;
        }

        println(("Encoded string: " + md5Hex))
        return md5Hex
    }


    private fun hmac(key: String, data: String): String {
        try {
            val sha256_HMAC = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA256")
            sha256_HMAC.init(secretKey)
            val t = Hex.stringToBytes(
                sha256_HMAC.doFinal(data.toByteArray(charset("UTF-8"))).toString()
            )
            var temp = ""
            t.forEach {
                temp += it
            }
            return temp
        } catch (e: Exception) {
            println("Exeption")
            println(e.message)
            return ""
        }
    }

    private fun curve25519() {

        val random = SecureRandom()
        val alice_secret_key = ECDHCurve25519().generate_secret_key(random)

        val alice_public_key = ECDHCurve25519().generate_public_key(alice_secret_key)

        val bob_secret_key = ECDHCurve25519().generate_secret_key(random);
        val bob_public_key = ECDHCurve25519().generate_public_key(bob_secret_key)

        val alice_shared_secret = ECDHCurve25519().generate_shared_secret(
            alice_secret_key, bob_public_key
        )

        val bob_shared_secret = ECDHCurve25519().generate_shared_secret(
            bob_secret_key, alice_public_key
        )
    }

    private class ECDHCurve25519 {
        fun generate_secret_key(random: SecureRandom) {

        }

        fun generate_public_key(random: Unit) {

        }

        fun generate_shared_secret(random: Unit, bob_public_key: Unit) {

        }
    }
}