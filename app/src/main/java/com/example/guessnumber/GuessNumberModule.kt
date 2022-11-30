package com.example.guessnumber

import org.koin.dsl.module

val guessNumberModule = module {
    single { GuessNumberViewModel() }
}