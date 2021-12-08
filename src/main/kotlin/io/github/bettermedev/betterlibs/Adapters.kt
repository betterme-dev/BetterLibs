package io.github.bettermedev.betterlibs

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Adapters {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val DependencyGraph: JsonAdapter<DependencyGraph> = adapter()
    inline fun <reified T> adapter(): JsonAdapter<T> = moshi.adapter(T::class.java)
}
