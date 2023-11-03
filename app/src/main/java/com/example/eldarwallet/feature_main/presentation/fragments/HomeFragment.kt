package com.example.eldarwallet.feature_main.presentation.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eldarwallet.databinding.FragmentHomeBinding
import com.example.eldarwallet.feature_card.domain.model.CardDomainModel
import com.example.eldarwallet.feature_main.presentation.adapter.CardAdapter
import com.example.eldarwallet.feature_main.presentation.view_model.HomeViewModel
import com.example.eldarwallet.feature_main.utils.BalanceState
import com.example.eldarwallet.feature_main.utils.CardListState
import com.example.eldarwallet.feature_payment.utils.PaymentUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.btnNewCard.setOnClickListener {
            newCardButtonAction()
        }

        binding.btnPayQr.setOnClickListener {
            payQrButtonAction()
        }

        viewModel.getCardsByUser()
        viewModel.getBalanceByUser()

        lifecycleScope.launchWhenStarted {
            viewModel.cardState.collect { cardState ->
                when (cardState) {
                    is CardListState.Success -> {
                        val cardAdapter = CardAdapter(cardState.cardList) { cardSelected ->
                            navToPayCardFragment(cardSelected)
                        }
                        binding.rvCards.layoutManager = LinearLayoutManager(context)
                        binding.rvCards.adapter = cardAdapter
                        viewModel.updateCardState(CardListState.LoadingOff)
                    }
                    is CardListState.Error -> {
                        showMessage(cardState.errorMsg)
                        viewModel.updateCardState(CardListState.LoadingOff)
                    }
                    CardListState.LoadingOn -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    CardListState.LoadingOff -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.balanceState.collect { balanceState ->
                when (balanceState) {
                    is BalanceState.Success -> {
                        binding.tvBalance.text = balanceState.balanceAmount.toString()
                        val colorId = ContextCompat.getColor(
                            binding.root.context,
                            PaymentUtils.getBalanceColor(balanceState.balanceStatus)
                        )
                        binding.tvBalance.setTextColor(colorId)
                        binding.tvSymbol.setTextColor(colorId)
                        viewModel.updateBalanceState(BalanceState.LoadingOff)
                    }
                    is BalanceState.Error -> {
                        showMessage(balanceState.errorMsg)
                        viewModel.updateBalanceState(BalanceState.LoadingOff)
                    }
                    BalanceState.LoadingOn -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    BalanceState.LoadingOff -> {
                        binding.progressBar.visibility = View.GONE
                    }
                }

            }
        }

    }

    private fun payQrButtonAction() {
        navToPayQrFragment()
    }

    private fun navToPayCardFragment(cardSelected: CardDomainModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToPayCardFragment(cardSelected)
        findNavController().navigate(action)
    }

    private fun navToPayQrFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToPayQrFragment()
        findNavController().navigate(action)
    }

    private fun newCardButtonAction() {
        navToNewCardFragment()
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navToNewCardFragment() {
        val action = HomeFragmentDirections.actionHomeFragmentToNewCardFragment()
        findNavController().navigate(action)
    }


}