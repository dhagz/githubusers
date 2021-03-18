package com.dhagz.githubusers._di

import android.content.Context
import com.dhagz.githubusers._di.qualifiers.AppContextQualifier
import dagger.Module
import dagger.Provides

/**
 * This module will provide the application context which will be used by Dagger injections
 * especially in providing strings. E.g. Retrofit URL
 */
@Module
class ContextModule(private val context: Context) {

    @Provides
    @AppContextQualifier
    fun context(): Context {
        return context
    }
}