package com.athar.bakeacademy

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.athar.bakeacademy.databinding.FragmentNutrisiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NutrisiFragment : Fragment() {

    private lateinit var binding: FragmentNutrisiBinding
    private lateinit var mUserPreference: UserPreference

    companion object {
        const val ID = "id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentNutrisiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserPreference = UserPreference(requireActivity())

        getBakery()
    }

    private fun getBakery() {
        val token: String? = mUserPreference.getUser().token
        val id = arguments?.getString(BahanFragment.ID)
        val client = ApiConfig.getApiService().getBakery("Bearer $token", id)
        client.enqueue(object : Callback<BakeryResponse> {

            override fun onResponse(
                call: Call<BakeryResponse>,
                response: Response<BakeryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setDetailResepData(responseBody)
                    }
                }  else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<BakeryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setDetailResepData(bakery: BakeryResponse) {
        val kalori: String = bakery.nutrisi.calories.toString() + "KCAL"
        val lemakTotal: String = bakery.nutrisi.totalNutrients.fAT.quantity.toString().take(2) + "g"
        val lemakJenuh: String = bakery.nutrisi.totalNutrients.fASAT.quantity.toString().take(2) + "g"
        val lemakTrans: String = bakery.nutrisi.totalNutrients.fATRN.quantity.toString().take(4) + "g"
        val kolestrol: String = bakery.nutrisi.totalNutrients.cHOLE.quantity.toString().take(3) + "mg"
        val sodium: String = bakery.nutrisi.totalNutrients.nA.quantity.toString().take(4) + "mg"
        val karbohidrat: String = bakery.nutrisi.totalNutrients.cHOCDF.quantity.toString().take(3) + "g"
        val fiber: String = bakery.nutrisi.totalNutrients.fIBTG.quantity.toString().take(1) + "g"
        val sugar: String = bakery.nutrisi.totalNutrients.sUGAR.quantity.toString().take(2) + "g"
        val protein: String = bakery.nutrisi.totalNutrients.pROCNT.quantity.toString().take(2) + "g"

        binding.apply {
            tvKalori.text = kalori
            tvLemakTotal.text = lemakTotal
            tvLemakJenuh.text = lemakJenuh
            tvLemakTrans.text = lemakTrans
            tvKolestrol.text = kolestrol
            tvSodium.text = sodium
            tvKarbohidrat.text = karbohidrat
            tvFiber.text = fiber
            tvSugar.text = sugar
            tvProtein.text = protein
        }

    }

}