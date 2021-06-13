package com.example.hidefolder.ui.login


import android.accounts.Account
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.hidefolder.R
import com.example.hidefolder.databinding.ActivityLoginBinding
import com.example.hidefolder.ui.main.MainActivity
import com.example.hidefolder.untils.RC_SIGN_IN
import com.example.hidefolder.untils.USER
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.NullPointerException


class LoginActivity : AppCompatActivity() {
    private lateinit var authenticationCallback: BiometricPrompt.AuthenticationCallback
    private var cancellationSignal: CancellationSignal? = null
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var account: GoogleSignInAccount
    private lateinit var gso: GoogleSignInOptions
    private lateinit var auth:FirebaseAuth

    override fun onStart() {
        super.onStart()
        Hawk.init(this).build()
        auth = Firebase.auth
        val user = auth.currentUser
        if (user!=null){
            val intent = Intent(applicationContext,MainActivity::class.java)
            Hawk.put(USER,user.displayName)
            "INFO USER".log()
            user.displayName?.log()
            user.tenantId?.log()
            biometrikFind()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animBackground()
        createRequest()


        btLogin.setOnClickListener {
            val user = Hawk.get<String>(USER)
            if (user ==null){
                "Siz avval ro`yxatdan o`tmagansiz!".show(this)
            }else{
            biometrikFind()
            }

        }
        binding.signInButton.setOnClickListener {
            signIn()
        }


    }
    private fun createRequest(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("471527569781-fstgonnjlelib7ph15m8ptvvi922skik.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun animBackground(){
        val latout = layout
        val animationDrawable = latout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(1000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TTT", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TTT", "Google sign in failed", e)
                e.message?.show(this)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TTT", "signInWithCredential:success")
                    val user = auth.currentUser
                    Hawk.put(USER,user?.displayName)
                    val intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TTT", "signInWithCredential:failure", task.exception)
                    "Sorry auth failed".show(this)
                }
            }
    }

    private fun biometrikFind() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            var k = checkBiometricSupport()
            authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    k = true
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    "succest".show(this@LoginActivity)
                    finish()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }

            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("DataSecurity")
                .setTitle("DataSecurity")
//                .setSubtitle("Biometrik tasdiq")
                .setDescription("Iltimos shaxsiy ma`lumotlarga o`tish uchun barmoq izi tekshiruvidan o`ting!\nBarmog`ingizni sensorga qo`ying!")
                .setNegativeButton(
                    getString(R.string.cancel),
                    this.mainExecutor,

                    DialogInterface.OnClickListener { dialog, which ->
                        k = true
                    }).build().also {
                    it.authenticate(
                        getCancellationSignal(),
                        this.mainExecutor,
                        authenticationCallback
                    )
                }

        } else {
            "Sizning qurilmangiz barmoq tekshiruvinni qo`llab quvvatlamaydi!".show(this)
            finish()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            "Cancel".show(this)
        }
        return cancellationSignal as CancellationSignal
    }


    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = this
            .getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            "Check1".log()
            finish()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            "Sizda barmoq izi skaneri funksiyasi yoqilmagan".show(this)
            "Check2".log()
            return false
        }
        return if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

}

fun String.show(context: Context) = Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
fun String.log() = Log.d("TTT", this)