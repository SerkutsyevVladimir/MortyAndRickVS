package com.example.rickandmortyvs.domain.mappers

interface Mapper <IN, OUT> {
    fun map(input: IN): OUT
}