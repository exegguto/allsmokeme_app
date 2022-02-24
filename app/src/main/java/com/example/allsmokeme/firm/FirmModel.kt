package com.example.allsmokeme.firm

class FirmModel { //модель фирм
    var firmName: String? = null //Название фирмы
    var firmPhoto: String? = null //Ссылка на фото

    constructor() {}
    constructor(firmName: String, firmPhoto: String) {

        this.firmName = firmName
        this.firmPhoto = firmPhoto
    }
}