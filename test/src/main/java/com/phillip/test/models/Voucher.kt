package com.phillip.test.models

data class Voucher(
    var uid: String = "",
    var code: String = "",
    var lable: String = "",
    var value: Double = 0.0,
    var type: Int = 0,
    var condition_type_label: String = "",
    var max_applied_value: Long = 0,
    var condition_type: Int = 0,
    var condition_value: Long = 0,
    var applied_value: Double = 0.0,
)


