package com.example.test2

data class ItemClass(val main_image_source: Int, val user_name: String, val resource_data: String,
                     val description: String, val language: String, val stars: String,
                     val shares: String, var expandable: Boolean = false)