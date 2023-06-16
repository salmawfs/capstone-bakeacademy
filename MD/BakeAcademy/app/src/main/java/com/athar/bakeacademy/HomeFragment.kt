package com.athar.bakeacademy

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.athar.bakeacademy.databinding.FragmentHomeBinding
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var mShimmerViewContainer: ShimmerFrameLayout
    private lateinit var adapter: ListBookmarkAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mShimmerViewContainer = binding.shimmerViewContainer
        binding.textView33.visibility = View.GONE
        mUserPreference = UserPreference(requireActivity())
        val itemDecoration = DividerItemDecoration(requireActivity(), LinearLayoutManager(requireActivity()).orientation)
        binding.rvBookmark.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvBookmark.addItemDecoration(itemDecoration)
        hideIcon()

        getBookmark()

        binding.apply {
            btnScan.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, CameraActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }

    }

    private fun getBookmark() {
        val token: String? = mUserPreference.getUser().token
        val id: Int = mUserPreference.getUser().userId
        val client = ApiConfig.getApiService().getBookmark("Bearer $token", id)
        client.enqueue(object : Callback<BookmarkResponse> {

            override fun onResponse(
                call: Call<BookmarkResponse>,
                response: Response<BookmarkResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setBookmarkData(responseBody.bookmark)
                        binding.textView33.visibility = View.GONE
                    }
                    hideIcon()
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.visibility = View.GONE;
                } else {
                    showIcon()
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.visibility = View.GONE;
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    binding.rvBookmark.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<BookmarkResponse>, t: Throwable) {
                showIcon()
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.visibility = View.GONE;
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                binding.rvBookmark.visibility = View.GONE
            }
        })
    }

    private fun getDetailBookmark(idBookmark: Int, namaRoti: String, fotoRoti: String) {
        activity?.let{
            val intent = Intent (it, BookmarkResultActivity::class.java)
            intent.putExtra("ID", idBookmark)
            intent.putExtra("NAMAROTI", namaRoti)
            intent.putExtra("FOTOROTI", fotoRoti)
            it.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        mShimmerViewContainer.startShimmerAnimation()
    }

    override fun onPause() {
        mShimmerViewContainer.stopShimmerAnimation()
        super.onPause()
    }

    private fun showIcon() {
        binding.apply {
            textView33.visibility = View.VISIBLE
            textView7.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
            btnScan.visibility = View.VISIBLE
        }
    }

    private fun hideIcon() {
        binding.apply {
            textView33.visibility = View.GONE
            textView7.visibility = View.GONE
            imageView.visibility = View.GONE
            btnScan.visibility = View.GONE
        }
    }

    private fun setBookmarkData(listBookmark: List<BookmarkItem>) {
        adapter = ListBookmarkAdapter(listBookmark)

        binding.rvBookmark.adapter = adapter

        adapter.setOnItemClickCallback(object : ListBookmarkAdapter.OnItemClickCallback {
                override fun onItemClicked(data: BookmarkItem) {
                    getDetailBookmark(data.id, data.namaRoti, data.foto)
            }
        })

    }


}