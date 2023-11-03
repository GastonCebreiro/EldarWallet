package com.example.eldarwallet.feature_card.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eldarwallet.R
import com.example.eldarwallet.databinding.FragmentNewCardBinding
import com.example.eldarwallet.feature_card.utils.CardTypeState
import com.example.eldarwallet.feature_card.presentation.view_model.NewCardViewModel
import com.example.eldarwallet.feature_card.utils.CardState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewCardFragment : Fragment() {

    private val viewModel: NewCardViewModel by viewModels()

    private lateinit var binding: FragmentNewCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.etFullName.filters = binding.etFullName.filters + InputFilter.AllCaps()

        binding.btnSave.setOnClickListener{
            saveButtonAction()
        }

        binding.etCardNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.etCardNumber.removeTextChangedListener(this)
                viewModel.setCardNumberFormat(s?.toString().orEmpty())
                binding.etCardNumber.addTextChangedListener(this)
            }
        })

        binding.etExpireDate.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.etExpireDate.hint =
                    resources.getString(R.string.fragment_new_card_date_format)
            else
                binding.etExpireDate.hint = null

        }

        binding.etExpireDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setCardExpireDateFormat(s?.toString().orEmpty())
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.formattedCardNumber.collect { cardNumber ->
                binding.etCardNumber.setText(cardNumber)
                binding.etCardNumber.setSelection(cardNumber.length)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.formattedCardExpireDate.collect { expireDate ->
                binding.etExpireDate.setText(expireDate)
                binding.etExpireDate.setSelection(binding.etExpireDate.text!!.length)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cardType.collect { cardType ->
                when (cardType) {
                    is CardTypeState.Visa -> {
                        setCardNumberMaxLength(VISA_CARD_NUMBER_MAX_LENGTH)
                        setCardCodeMaxLength(VISA_CARD_CODE_MAX_LENGTH)
                        setCardImage(R.drawable.visa_logo)
                    }
                    is CardTypeState.MasterCard -> {
                        setCardNumberMaxLength(MASTER_CARD_NUMBER_MAX_LENGTH)
                        setCardCodeMaxLength(MASTER_CARD_CODE_MAX_LENGTH)
                        setCardImage(R.drawable.mastercard_logo)
                    }
                    is CardTypeState.AmericanExpress -> {
                        setCardNumberMaxLength(AMEX_CARD_NUMBER_MAX_LENGTH)
                        setCardCodeMaxLength(AMEX_CARD_CODE_MAX_LENGTH)
                        setCardImage(R.drawable.amex_logo)
                    }
                    is CardTypeState.None -> {
                        setCardNumberMaxLength(DEFAULT_CARD_NUMBER_MAX_LENGTH)
                        setCardCodeMaxLength(DEFAULT_CARD_CODE_MAX_LENGTH)
                        binding.ivCardLogo.visibility = View.GONE
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cardRegister.collect { cardRegister ->
                when (cardRegister) {
                    is CardState.Success -> {
                        cleanErrors()
                        showMessage(resources.getString(R.string.success_msg_card_registered))
                        navToHomeFragment()
                        viewModel.updateViewState(CardState.LoadingOff)
                    }
                    is CardState.Error -> {
                        cleanErrors()
                        showMessage(cardRegister.errorMsg)
                        viewModel.updateViewState(CardState.LoadingOff)
                    }
                    is CardState.WrongCardNumber -> {
                        showMessage(cardRegister.errorMsg)
                        setCardNumberError(cardRegister.errorMsg)
                        viewModel.updateViewState(CardState.LoadingOff)
                    }
                    is CardState.WrongCardCode -> {
                        showMessage(cardRegister.errorMsg)
                        setCardCodeError(cardRegister.errorMsg)
                        viewModel.updateViewState(CardState.LoadingOff)
                    }
                    is CardState.WrongCardDate -> {
                        showMessage(cardRegister.errorMsg)
                        setExpireDateError(cardRegister.errorMsg)
                        viewModel.updateViewState(CardState.LoadingOff)
                    }
                    is CardState.WrongCardName -> {
                        showMessage(cardRegister.errorMsg)
                        setCardNameError(cardRegister.errorMsg)
                        viewModel.updateViewState(CardState.LoadingOff)
                    }
                    is CardState.LoadingOn -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is CardState.LoadingOff -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun saveButtonAction() {
        viewModel.checkCardToRegister(
            binding.etCardNumber.text.toString(),
            binding.etCardCode.text.toString(),
            binding.etExpireDate.text.toString(),
            binding.etFullName.text.toString()
        )
    }

    private fun setCardImage(resId: Int) {
        binding.ivCardLogo.visibility = View.VISIBLE
        binding.ivCardLogo.setImageResource(resId)
    }

    private fun setCardNumberMaxLength(maxLength: Int) {
        val inputFilterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        binding.etCardNumber.filters = inputFilterArray
    }

    private fun setCardCodeMaxLength(maxLength: Int) {
        val inputFilterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        binding.etCardCode.filters = inputFilterArray
    }

    private fun setCardNumberError(errorMsg: String) {
        cleanErrors()
        binding.lCardNumber.error = errorMsg
    }

    private fun setCardCodeError(errorMsg: String) {
        cleanErrors()
        binding.lCardCode.error = errorMsg
    }

    private fun setExpireDateError(errorMsg: String) {
        cleanErrors()
        binding.lExpireDate.error = errorMsg
    }

    private fun setCardNameError(errorMsg: String) {
        cleanErrors()
        binding.lFullName.error = errorMsg
    }

    private fun cleanErrors() {
        binding.lCardNumber.error = null
        binding.lCardCode.error = null
        binding.lExpireDate.error = null
        binding.lFullName.error = null
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private fun navToHomeFragment() {
        findNavController().navigateUp()
    }


    companion object {
        const val AMEX_CARD_NUMBER_MAX_LENGTH = 17
        const val VISA_CARD_NUMBER_MAX_LENGTH = 19
        const val MASTER_CARD_NUMBER_MAX_LENGTH = 19
        const val DEFAULT_CARD_NUMBER_MAX_LENGTH = 25

        const val AMEX_CARD_CODE_MAX_LENGTH = 4
        const val VISA_CARD_CODE_MAX_LENGTH = 3
        const val MASTER_CARD_CODE_MAX_LENGTH = 3
        const val DEFAULT_CARD_CODE_MAX_LENGTH = 4
    }


}