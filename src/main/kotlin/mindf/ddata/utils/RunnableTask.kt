package mindf.utils

import java.util.*

abstract class RunnableTask(private val message: String = "Task complete", private val displayProgress: Boolean = true) : Runnable {

    internal var conditionsDone: MutableMap<String, Boolean> = HashMap()
    var progress = intArrayOf(0)
    internal var isRunning = booleanArrayOf(false)
    abstract override fun run()
    internal abstract fun progress()  // user has to set the progress after condition is done
    internal open fun end() {}
    private var i = 0

    //todo make a builder patter to avoid putting default value each time
    internal fun start(refreshRateMilliseconds: Int) {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!isRunning[0]) {
                    isRunning[0] = true
                    Thread(this@RunnableTask).run()
                } else if (progress[0] >= 100) {
                    conditionsDone.clear()
                    progress[0] = 0
                    isRunning[0] = false
                    stopTimer(timer)
                    if (displayProgress) {
                        println(message)
                    }
                    end()
                } else {
                    progress()
                }
            }
        }, 0, refreshRateMilliseconds.toLong())
    }

    internal fun atProgress(progress: Int, function: () -> Unit, message: String = "", progressAmount: Int = 100 - progress) {
        if (getCurrentProgress() == progress) {
            function()
            setProgress(progress + progressAmount)
            println(RunnableTask::class.java.simpleName + " $message progress made: $progressAmount!")
        }
    }

    internal fun getCurrentProgress(): Int {
        var currentProgress = 0
        progress.forEach { value ->
            currentProgress += value
        }
        return currentProgress
    }

    internal fun setStartingProgress(amount: Int) {
        increaseProgress(amount)
    }

    internal fun progressAfter(condition: Boolean, name: String, progressAmount: Int) {
        if (condition) {
            if (conditionsDone[name] == null) {
                conditionsDone[name] = true
                increaseProgress(progressAmount)
                if (displayProgress) {
                    println(RunnableTask::class.java.simpleName + " " + name + " progress made: " + progressAmount + "!")
                }
            }
        }
    }

    internal fun stopTimer(timer: Timer) {
        timer.cancel()
        timer.purge()
    }

    internal fun increaseProgress(value: Int) {
        progress[0] += value //todo refactor this for the good position in the map
    }

    internal fun isRunning(): Boolean {
        return isRunning[0]
    }

    internal fun setProgress(progress: Int) {
        this.progress[0] = progress
    }

    internal fun getProgress(): Int {
        return progress[0]
    }

    internal fun stopRunning() {
        isRunning[0] = false
    }

    private fun defaultMessage() {

    }
}