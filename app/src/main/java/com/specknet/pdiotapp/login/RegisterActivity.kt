package com.specknet.pdiotapp.login

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.specknet.pdiotapp.login.database.UserDatabase
import com.specknet.pdiotapp.login.utils.InputValidator
import android.view.inputmethod.InputMethodManager

import com.specknet.pdiotapp.R
import java.io.File

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private val activity = this@RegisterActivity

    private lateinit var textInputLayoutName: LinearLayout
    private lateinit var textInputLayoutEmail: LinearLayout
    private lateinit var textInputLayoutPassword: LinearLayout
    private lateinit var textInputLayoutConfirmPassword: LinearLayout

    private lateinit var textInputEditTextName: EditText
    private lateinit var textInputEditTextEmail: EditText
    private lateinit var textInputEditTextPassword: EditText
    private lateinit var textInputEditTextConfirmPassword: EditText

    private lateinit var appCompatButtonRegister: Button
    private lateinit var appCompatTextViewLoginLink: Button

    private lateinit var inputValidator: InputValidator
    private lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.register_page)

        // hiding the action bar
        supportActionBar!!.hide()

        // initializing the views
        initViews()

        // initializing the listeners
        initListeners()

        // initializing the objects
        initObjects()
    }


    private fun initViews() {

        textInputLayoutName = findViewById<LinearLayout>(R.id.S_UserName)
        textInputLayoutEmail = findViewById<LinearLayout>(R.id.S_Email)
        textInputLayoutPassword = findViewById<LinearLayout>(R.id.S_Password)
        textInputLayoutConfirmPassword = findViewById<LinearLayout>(R.id.S_ConfPassword)

        textInputEditTextName = findViewById<EditText>(R.id.S_UserNameInput)
        textInputEditTextEmail = findViewById<EditText>(R.id.S_EmailInput)
        textInputEditTextPassword = findViewById<EditText>(R.id.S_PasswordInput)
        textInputEditTextConfirmPassword = findViewById<EditText>(R.id.S_ConfPasswordInput)

        appCompatButtonRegister = findViewById<Button>(R.id.SignupSubmit)

        appCompatTextViewLoginLink = findViewById<Button>(R.id.HaveAccountButton)

    }

    private fun initListeners() {
        appCompatButtonRegister.setOnClickListener(this)
        appCompatTextViewLoginLink.setOnClickListener(this)
    }

    private fun initObjects() {
        userDatabase = UserDatabase(activity)
        inputValidator = InputValidator(activity)
    }


    override fun onClick(v: View) {
        when (v.id) {

            R.id.SignupSubmit -> postDataToSQLite()

            R.id.HaveAccountButton -> finish()
        }
    }

    private fun postDataToSQLite() {
        if (!inputValidator.isInputEditTextFilled(textInputEditTextName)) {
            hideKeyboardFrom(textInputLayoutEmail)
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }
        if (!inputValidator.isInputEditTextFilled(textInputEditTextEmail)) {
            hideKeyboardFrom(textInputLayoutEmail)
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }
        if (!inputValidator.isInputEditTextEmail(textInputEditTextEmail)) {
            hideKeyboardFrom(textInputLayoutEmail)
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }
        if (!inputValidator.isInputEditTextFilled(textInputEditTextPassword)) {
            hideKeyboardFrom(textInputLayoutEmail)
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }
        if (!inputValidator.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword)) {
            hideKeyboardFrom(textInputLayoutEmail)
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (!userDatabase.checkUser(textInputEditTextEmail.text.toString().trim())) {

            val user = User(username = textInputEditTextName.text.toString().trim(),
                email = textInputEditTextEmail.text.toString().trim(),
                password = textInputEditTextPassword.text.toString().trim()
            )

            val file = File(getExternalFilesDir(null)!!.absolutePath)
            val userFile = File(file, user.email)
            if(!userFile.exists()) userFile.mkdirs()

            userDatabase.addUser(user)



            // Snack Bar to show success message that record saved successfully
            Toast.makeText(this, "Registration successful. Please go back to login page", Toast.LENGTH_SHORT).show()
            emptyInputEditText()


        } else {
            // Snack Bar to show error message that record already exists
            Toast.makeText(this, "Email address already signed up", Toast.LENGTH_SHORT).show()
        }


    }

    private fun emptyInputEditText() {
        textInputEditTextName.text = null
        textInputEditTextEmail.text = null
        textInputEditTextPassword.text = null
        textInputEditTextConfirmPassword.text = null
    }

    private fun hideKeyboardFrom(view: View) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}