package dev.shadowmeld.viewdaydream.model

/*
{
    "hasTaste": false,
    "code": 200,
    "category": 0,
    "result": ...
}
 */
data class BaseResultInfo<T>(
    val hasTaste: String,
    val code: String,
    val category: String,
    val result: T
)