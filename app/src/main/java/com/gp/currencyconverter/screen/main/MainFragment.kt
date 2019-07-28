package com.gp.currencyconverter.screen.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.droid.hp.room.entities.Balance
import com.droid.hp.room.entities.BalanceTransactionHistory
import com.droid.hp.room.entities.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gp.currencyconverter.R
import com.gp.currencyconverter.app.App
import com.gp.currencyconverter.network.model.CurrencyCode
import com.gp.currencyconverter.screen.base.BaseFragment
import com.gp.currencyconverter.utils.SharedPreferenceUtil
import com.gp.gojek.util.AssetReaderUtil
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


class MainFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreference: SharedPreferenceUtil

    private lateinit var selectedCurrencyCode: Currency
    private lateinit var convertCurrencyCode: Currency

    private lateinit var viewModel: MainViewModel

    private val currencyFormat = NumberFormat.getNumberInstance(Locale.US)
    private lateinit var balanceTransactionHistory: BalanceTransactionHistory
    private lateinit var currencies: List<Currency>

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = context as Activity

        let {
            DaggerMainComponent.builder()
                    .appComponent((activity.application as App).appComponent)
                    .mainModule(MainModule())
                    .build().inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    private fun getCurrenciesFromAssets(): List<Currency> {
        val rawJson = AssetReaderUtil.asset(context!!, "currencyCodeList.json")
        val type = object : TypeToken<MutableMap<String, CurrencyCode>>() {
        }.type
        val rawCurrencies = Gson().fromJson<Map<String, CurrencyCode>>(rawJson, type)

        return rawCurrencies.values.map {
            Currency(
                    name = it.name,
                    symbol = it.symbol,
                    name_plural = it.namePlural,
                    code = it.code
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.insertCurrencies(getCurrenciesFromAssets())
        viewModel.getLiveDataTransactionHistory().observe(viewLifecycleOwner, Observer { response ->
            if (response.throwable == null) {
                btnCancel.visibility = View.GONE
                btnConvert.text = getString(R.string.btn_convert)
                containerConversion.visibility = View.GONE
                etAmountConvert.text?.clear()
                viewModel.getLatestBalance(sharedPreference.selectedBalanceId)
            } else {
                AlertDialog.Builder(context!!, R.style.AlertDialogTheme)
                        .setMessage(response.throwable.message)
                        .setTitle(R.string.title_generic_error)
                        .setPositiveButton(R.string.btn_dismiss) { dialog, _ ->
                        }.show()
            }
        })

        viewModel.getLiveDataBalanceHistory().observe(viewLifecycleOwner, Observer { response ->
            if (response.throwable == null) {
                sharedPreference.selectedBalanceId = response.data?.balance?.id ?: 0
                selectedCurrencyCode = currencies.single { currency ->
                    currency.id == response?.data?.balance?.currencyId
                }

                this.balanceTransactionHistory = response.data!!

                lblAmount1.text = currencyFormat.format(balanceTransactionHistory.balance.balance)
                lblCurrencyCode1.text =
                        getString(R.string.lbl_currency_symbol, selectedCurrencyCode.symbol, selectedCurrencyCode.code)
                lblCurrencyName1.text = selectedCurrencyCode.name
            } else {
                showGenericErrorDialog(response.throwable.message ?: "")
            }
        })

        viewModel.getLiveDataCurrencies().observe(viewLifecycleOwner, Observer { response ->
            if (response.throwable == null) {
                currencies = response.data!!
                convertCurrencyCode = response.data!![0]
                lblCurrencyCode2.text = convertCurrencyCode.code
                lblCurrencyName2.text = convertCurrencyCode.name

                viewModel.insertAndGetBalance(
                        Balance(currencyId = 1, balance = 1000000.0),
                        sharedPreference.selectedBalanceId
                )
            } else {
                showGenericErrorDialog(response.throwable.message ?: "")
            }
        })

        viewModel.getLiveDataConvertCurrency().observe(viewLifecycleOwner, Observer { response ->
            if (response.throwable == null) {
                val converted = response.data!!
                val message = if (converted.commissionFee == 0.0) {
                    getString(
                            R.string.message_success_conversion_free,
                            currencyFormat.format(converted.baseAmount),
                            converted.sourceCurrency,
                            converted.transferredAmount,
                            converted.destinationCurrency
                    )
                } else {
                    getString(
                            R.string.message_success_conversion_with_commission,
                            currencyFormat.format(converted.baseAmount),
                            converted.sourceCurrency,
                            converted.transferredAmount,
                            converted.destinationCurrency,
                            currencyFormat.format(converted.commissionFee),
                            converted.sourceCurrency
                    )
                }
                AlertDialog.Builder(context!!, R.style.AlertDialogTheme)
                        .setMessage(message)
                        .setTitle(R.string.title_success_conversion)
                        .setPositiveButton(R.string.btn_dismiss) { dialog, _ ->
                            dialog.dismiss()
                        }.show()
            } else {
                showGenericErrorDialog(response.throwable.message ?: "")
            }
        })

        viewModel.getLiveDataProgress().observe(viewLifecycleOwner, Observer { show ->
            if (show) {
                containerProgress.visibility = View.VISIBLE
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                containerProgress.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        initViews()
    }

    private fun showGenericErrorDialog(message: String) {
        AlertDialog.Builder(context!!, R.style.AlertDialogTheme)
                .setMessage(message)
                .setTitle(R.string.title_generic_error)
                .setPositiveButton(R.string.btn_dismiss) { _, _ ->
                }.show()
    }

    private fun initViews() {
        btnConvert.setOnClickListener {
            if (btnConvert.text == getString(R.string.btn_done)) {
                try {
                    viewModel.convertCurrency(
                            etAmountConvert.text.toString().toDouble(),
                            selectedCurrencyCode.code,
                            convertCurrencyCode.code,
                            balanceTransactionHistory.balance.id,
                            convertCurrencyCode.id
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, "Not allowed.", Toast.LENGTH_SHORT).show()
                }
            }
            containerConversion.visibility = View.VISIBLE
            btnCancel.visibility = View.VISIBLE
            btnConvert.text = getString(R.string.btn_done)
        }

        btnCancel.setOnClickListener {
            containerConversion.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnConvert.text = getString(R.string.btn_convert)
        }

        containerCurrencyDropDown2.setOnClickListener { view ->
            PopupMenu(context!!, view).apply {
                for (currencyCode in currencies) {
                    menu.add(currencyCode.name)
                }

                gravity = Gravity.BOTTOM

                setOnMenuItemClickListener { menuItem ->
                    convertCurrencyCode = currencies
                            .single { currencyCode ->
                                currencyCode.name == menuItem.title
                            }
                    lblCurrencyName2.text = convertCurrencyCode.name
                    lblCurrencyCode2.text =
                            getString(R.string.lbl_currency_symbol, convertCurrencyCode.symbol, convertCurrencyCode.code)
                    return@setOnMenuItemClickListener true
                }
            }.show()
        }

        containerCurrencyDropDown1.setOnClickListener { view ->
            PopupMenu(context!!, view).apply {
                val balances = viewModel.getLiveDataBalanceTransactionHistorySet().value
                balances?.distinctBy {
                    it.balance.currencyId
                }?.forEach { balanceHistory ->
                    menu.add(currencies.single {
                        it.id == balanceHistory.balance.currencyId
                    }.name)
                }

                gravity = Gravity.BOTTOM

                setOnMenuItemClickListener { menuItem ->
                    selectedCurrencyCode = currencies
                            .single { currencyCode ->
                                currencyCode.name == menuItem.title
                            }
                    lblCurrencyName1.text = selectedCurrencyCode.name
                    lblCurrencyCode1.text =
                            getString(R.string.lbl_currency_symbol, selectedCurrencyCode.symbol, selectedCurrencyCode.code)
                    viewModel.getLatestBalance(balances?.distinctBy {
                        it.balance.currencyId
                    }?.single {
                        it.balance.currencyId == selectedCurrencyCode.id
                    }?.balance?.id ?: 0)
                    return@setOnMenuItemClickListener true
                }
            }.show()
        }
    }

    companion object {
        val TAG = "MainFragment"

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}