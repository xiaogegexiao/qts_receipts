package com.qtsreceipts.di.scopes


import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * Defines a dagger scope that only lasts for the life of an activity.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class FragmentScope