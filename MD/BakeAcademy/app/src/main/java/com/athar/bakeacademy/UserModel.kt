package com.athar.bakeacademy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var token: String? = null,
    var nama: String? = null,
    var username: String? = null,
    var email: String? = null,
    var alergi: String? = null,
    var foto: String? = null,
    var userId: Int = 0
) : Parcelable