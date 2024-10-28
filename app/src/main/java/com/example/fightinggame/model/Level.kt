package com.example.fightinggame.model

import android.os.Parcel
import android.os.Parcelable

data class Level(
    val levelNumber: Int,
    val questions: List<Question>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(Question) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(levelNumber)
        parcel.writeTypedList(questions)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Level> {
        override fun createFromParcel(parcel: Parcel): Level = Level(parcel)
        override fun newArray(size: Int): Array<Level?> = arrayOfNulls(size)
    }
}