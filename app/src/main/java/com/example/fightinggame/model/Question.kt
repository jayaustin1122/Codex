package com.example.fightinggame.model

import android.os.Parcel
import android.os.Parcelable

data class Question(
    val questionText: String,
    val choices: List<String>,
    val correctAnswerIndex: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",  // Handle null string by providing an empty string as default
        parcel.createStringArrayList() ?: listOf(),  // Ensure an empty list if the parcel is null
        parcel.readInt()  // Read integer as is
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(questionText)  // Write the question text
        parcel.writeStringList(choices)  // Write the choices list
        parcel.writeInt(correctAnswerIndex)  // Write the correct answer index
    }

    override fun describeContents(): Int = 0  // No special contents for the parcel

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question = Question(parcel)
        override fun newArray(size: Int): Array<Question?> = arrayOfNulls(size)
    }
}
