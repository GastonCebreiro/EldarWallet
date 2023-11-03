package com.example.eldarwallet.feature_card.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eldarwallet.R
import com.example.eldarwallet.databinding.FragmentPayCardBinding
import com.example.eldarwallet.feature_card.presentation.view_model.PayCardViewModel
import com.example.eldarwallet.feature_card.utils.CardUtils
import com.example.eldarwallet.feature_card.utils.PayCardState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayCardFragment : Fragment() {

    private val viewModel: PayCardViewModel by viewModels()

    private lateinit var binding: FragmentPayCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.etAmount.text.clear()
        binding.etAmount.requestFocus()

        val cardSelected = PayCardFragmentArgs.fromBundle(requireArguments()).cardSelected

        binding.ivCardLogo.setImageResource(CardUtils.getResIdByCardType(cardSelected))
        binding.tvCardNumber.text = CardUtils.getCardNumberFormatted(cardSelected)
        binding.tvExpireDate.text = cardSelected.expireDate
        binding.tvFullName.text = cardSelected.fullName
        binding.clCard.setBackgroundColor(
            ContextCompat.getColor(
            binding.root.context,
            CardUtils.getCardColor(cardSelected)
        ))

        lifecycleScope.launchWhenStarted {
            viewModel.paymentState.collect { paymentState ->
                when (paymentState) {
                    is PayCardState.Success -> {
                        showMessage(resources.getString(R.string.success_msg_payment_registered))
                        navToHomeFragment()
                        viewModel.updateViewState(PayCardState.LoadingOff)
                    }
                    is PayCardState.Error -> {
                        showMessage(paymentState.errorMsg)
                        viewModel.updateViewState(PayCardState.LoadingOff)
                    }
                    PayCardState.LoadingOn -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    PayCardState.LoadingOff -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        binding.btnPay.setOnClickListener {
            viewModel.generatePayment(
                cardSelected,
                binding.etAmount.text.toString().trim()
            )
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navToHomeFragment() {
        findNavController().navigateUp()
    }
}