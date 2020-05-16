package com.yrdtechnologies.executors

import ikartiks.expensetracker.executors.DiskIOThreadExecutor
import java.util.concurrent.Executor

class AppExecutors  constructor(
        private val diskIOExecutor: DiskIOThreadExecutor,
        private val mainThreadExecutor: MainThreadExecutor
) {

    fun diskIO(): Executor {
        return diskIOExecutor
    }

    fun mainThread(): Executor {
        return mainThreadExecutor
    }
}
