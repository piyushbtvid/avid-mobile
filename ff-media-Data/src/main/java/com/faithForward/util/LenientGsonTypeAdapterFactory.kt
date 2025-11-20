package com.faithForward.util

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class LenientGsonTypeAdapterFactory: TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val delegate: TypeAdapter<T> = gson.getDelegateAdapter(this, type)

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T?) {
                delegate.write(out, value)
            }

            override fun read(reader: JsonReader): T? {
                return try {
                    delegate.read(reader) // Normal parsing
                } catch (e: JsonSyntaxException) {
                    handleException(reader, e)
                } catch (e: NumberFormatException) {
                    handleException(reader, e)
                } catch (e: IllegalStateException) {
                    handleException(reader, e)
                } catch (e: IOException) {
                    handleException(reader, e)
                }
            }
        }.nullSafe()
    }

    private fun <T> handleException(reader: JsonReader, e: Exception): T? {
        try {
            reader.skipValue() // Skip the invalid value
        } catch (ignored: IOException) {
        }
        return null // Return null instead of crashing
    }
}