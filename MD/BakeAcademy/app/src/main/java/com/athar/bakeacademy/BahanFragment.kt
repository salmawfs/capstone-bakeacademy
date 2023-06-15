package com.athar.bakeacademy

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.athar.bakeacademy.databinding.FragmentBahanBinding
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BahanFragment : Fragment() {

    companion object {
        const val TOKEN = "token"
        const val ID = "id"
    }

    private lateinit var binding: FragmentBahanBinding
    private lateinit var adapterBahan: ListBahanStepAdapter
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentBahanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mShimmerViewContainer = binding.shimmerViewContainer
        val itemDecoration = DividerItemDecoration(requireActivity(), LinearLayoutManager(requireActivity()).orientation)
        binding.rvBahan.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvBahan.addItemDecoration(itemDecoration)

        getBakery()

    }

    private fun getBakery() {
        val token: String? = arguments?.getString(TOKEN)
        val id = arguments?.getString(ID)
        val client = ApiConfig.getApiService().getBakery("Bearer $token", id)
        client.enqueue(object : Callback<BakeryResponse> {

            override fun onResponse(
                call: Call<BakeryResponse>,
                response: Response<BakeryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val bahan = responseBody.trsBahan
                        val listBahan: List<String> = bahan.split("_").map { it -> it.trim() }

//                        val langkah = responseBody.data.langkah
//                        val listlangkah: List<String> = langkah.split("_").map { it -> it.trim() }

                        setDetailResepData(listBahan)
                    }
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.visibility = View.GONE;
                }  else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.visibility = View.GONE;
                }
            }

            override fun onFailure(call: Call<BakeryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.visibility = View.GONE;
            }
        })
    }

    private fun setDetailResepData(listBahan: List<String>) {
        adapterBahan = ListBahanStepAdapter(listBahan)
        binding.rvBahan.adapter = adapterBahan

    }
    override fun onResume() {
        super.onResume()
        mShimmerViewContainer.startShimmerAnimation()
    }

    override fun onPause() {
        mShimmerViewContainer.stopShimmerAnimation()
        super.onPause()
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}