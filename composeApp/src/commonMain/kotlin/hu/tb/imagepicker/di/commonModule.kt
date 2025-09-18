package hu.tb.imagepicker.di

import hu.tb.imagepicker.CommonViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    viewModelOf(::CommonViewModel)
}