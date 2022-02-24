package com.example.allsmokeme.cart

class QuantityOutOfRangeException : RuntimeException {
    constructor() : super(DEFAULT_MESSAGE) {}
    constructor(message: String?) : super(message) {}

    companion object {
        private const val serialVersionUID = 44L
        private const val DEFAULT_MESSAGE = "Quantity is out of range"
    }
}