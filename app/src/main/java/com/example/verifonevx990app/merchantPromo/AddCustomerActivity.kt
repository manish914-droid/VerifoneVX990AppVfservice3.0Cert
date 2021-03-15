package com.example.verifonevx990app.merchantPromo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.verifonevx990app.databinding.ActivityAddCustomerBinding

class AddCustomerActivity : AppCompatActivity() {
    var binding: ActivityAddCustomerBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCustomerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}