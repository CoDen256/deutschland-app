package de.app.data.model

import android.location.Address

fun Address.simplify(): de.app.data.model.Address{
    return Address(
        country = countryName,
        postalCode = postalCode,
        city = locality,
        address = "$thoroughfare $featureName"
    )
}