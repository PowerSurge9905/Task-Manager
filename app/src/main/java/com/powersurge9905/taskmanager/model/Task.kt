package com.powersurge9905.taskmanager.model

import androidx.annotation.BoolRes
import androidx.annotation.StringRes

data class Task(
    @StringRes val stringResourceId: Int,
    @BoolRes val boolResourceId: Int
)
