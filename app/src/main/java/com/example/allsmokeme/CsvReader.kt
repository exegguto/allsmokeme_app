package com.example.allsmokeme

import android.content.Context
import com.example.allsmokeme.product.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.File

//класс чтения файла и записи в firestore, не работает: переписывает collection в фирме, а должен добавлять
class CsvReader(val context: Context) {

    // Открыть файл и получить всё содержимое
    private fun open(fileName: String): String {
        val temp = "/storage/sdcard/Download/" + fileName
        val inputStream = File(temp)
        return inputStream.bufferedReader().use { it.readText() }
    }

    // Заполнить csv данными ArrayList
    private fun fullArrayList(csvData: String): ArrayList<Array<String>> {
        val simpleList = arrayListOf<Array<String>>()
        val rows: List<String> = csvData.split("\n").map { it.trim() }

        for (row in rows) {
            val cells: List<String> = row.split(";").map { it.trim() }


            val listTobaccoTemp = ProductModel()

            listTobaccoTemp.firm = cells[0]
            listTobaccoTemp.fortress = cells[1]
            listTobaccoTemp.name_eng = cells[2]
            listTobaccoTemp.name_ru = cells[3]
            listTobaccoTemp.weight = cells[4]
            listTobaccoTemp.price = cells[5].toLong()
            listTobaccoTemp.ratingSum = 0
            listTobaccoTemp.rating = 0.0F
            listTobaccoTemp.country = cells[7]
            listTobaccoTemp.nicotine = cells[8]
            listTobaccoTemp.composition = cells[9]
            listTobaccoTemp.description = cells[10]
//            listTobaccoTemp.photo = cells[11]
            listTobaccoTemp.id = cells[12]
            listTobaccoTemp.priceMix = cells[13].toLong()
            listTobaccoTemp.availability = cells[14].toLong()

            val db = FirebaseFirestore.getInstance()
            val dbCollection = db.collection("tobacco").document(cells[0])
            val dbTobacco =dbCollection.collection(cells[1]).document()

            db.runBatch { batch ->
                batch.set(dbCollection, hashMapOf("collection" to arrayListOf(cells[1])), SetOptions.merge())
                batch.set(dbTobacco, listTobaccoTemp)

            }.addOnCompleteListener {}
        }

        return simpleList
    }

    // Прочитать данных из csv файла в массив
    fun read(fileName: String): ArrayList<Array<String>> {
        val csvData = this.open(fileName)
        return fullArrayList(csvData)
    }
}