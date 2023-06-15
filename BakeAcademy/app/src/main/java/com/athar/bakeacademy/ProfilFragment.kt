package com.athar.bakeacademy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.athar.bakeacademy.databinding.FragmentProfilBinding
import com.bumptech.glide.Glide


class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    private lateinit var mUserPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUserPreference = UserPreference(requireActivity())

        onClick()

    }

    private fun onClick() {

        binding.apply {

            cvLogout.setOnClickListener {
                mUserPreference.removeUser()

                activity?.let{
                    val intent = Intent (it, LoginActivity::class.java)
                    it.startActivity(intent)
                }
            }

            cvEditProfile.setOnClickListener {
                activity?.let{
                    val intent = Intent (it, ProfileSettingActivity::class.java)
                    it.startActivity(intent)
                }
            }

            tvNamaUser.text = mUserPreference.getUser().nama

            Glide.with(imProfilUser)
                .load("https://storage.googleapis.com/bakeacademy-bucket/upload-foto/profile/" + mUserPreference.getUser().foto)
                .into(imProfilUser)
        }
    }

}