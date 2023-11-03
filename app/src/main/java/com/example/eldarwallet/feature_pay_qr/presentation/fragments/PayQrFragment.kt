package com.example.eldarwallet.feature_pay_qr.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.eldarwallet.databinding.FragmentPayQrBinding
import com.example.eldarwallet.feature_pay_qr.presentation.view_model.PayQrViewModel
import com.example.eldarwallet.feature_pay_qr.utils.QRCodeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayQrFragment : Fragment() {

    private val viewModel: PayQrViewModel by viewModels()

    private lateinit var binding: FragmentPayQrBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getQRCodeBitmap()

        lifecycleScope.launchWhenStarted {
            viewModel.qrCodeState.collect { qrCodeState ->
                when (qrCodeState) {
                    is QRCodeState.Success -> {
                        val bitmap = qrCodeState.qrBitmap
                        binding.ivQRCode.setImageBitmap(bitmap)
                        viewModel.updateViewState(QRCodeState.LoadingOff)
                    }
                    is QRCodeState.Error -> {
                        showMessage(qrCodeState.errorMsg)
                        viewModel.updateViewState(QRCodeState.LoadingOff)
                    }
                    is QRCodeState.LoadingOn -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is QRCodeState.LoadingOff -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}