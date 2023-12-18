package com.example.rickandmortyvs.domain.models

enum class Status(val statusType: String) {
    ALIVE("Alive"),
    DEAD("Dead"),
    UNKNOWN("unknown"),
    EMPTY("")
}