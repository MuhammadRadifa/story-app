package com.dicoding.storyapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.ResultState
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.helper.ViewModelFactory
import com.dicoding.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRegisterBinding

    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val registerViewModel:RegisterViewModel by viewModels<RegisterViewModel>{
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()



        val loginActivity = Intent(this, LoginActivity::class.java)
        binding.apply {
            hyperlinkLogin.setOnClickListener{
                startActivity(loginActivity)
            }

            registerButton.setOnClickListener {
                if (edRegisterEmail.text.isNotEmpty() && edRegisterName.text.isNotEmpty() && edRegisterPassword.text?.length!! >= 8){
                    registerViewModel.submitRegister(
                        name = edRegisterName.text.toString(),
                        email = edRegisterEmail.text.toString(),
                        password = edRegisterPassword.text.toString()
                    )
                }else{
                    Toast.makeText(this@RegisterActivity, "Please fill the form correctly", Toast.LENGTH_SHORT).show()
                }

            }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this@RegisterActivity)
            builder.setView(R.layout.dialog_loading)
            val dialog: AlertDialog = builder.create()

            registerViewModel.responseResult.observe(this@RegisterActivity) {
                response ->
                when (response) {
                    is ResultState.Loading -> dialog.show()
                    is ResultState.Error -> {
                        dialog.dismiss()
                        Toast.makeText(
                            this@RegisterActivity,
                            response.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ResultState.Success -> {
                        dialog.dismiss()
                        val homeActivity = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(homeActivity)
                        finish()
                    }

                    else -> dialog.dismiss()
                }
            }
        }
    }


}