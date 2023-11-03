package com.example.eldarwallet.feature_login.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eldarwallet.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

}