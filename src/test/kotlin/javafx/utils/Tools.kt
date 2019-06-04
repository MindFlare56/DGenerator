package javafx.utils

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.Parent
import mindf.utils.DateTime
import tornadofx.getChildList
import tornadofx.indexInParent
import tornadofx.removeFromParent
import tornadofx.replaceWith
import java.security.MessageDigest
import java.time.LocalDate
import java.util.*

class Tools {

    companion object {

        fun wait(waitForFunction: () -> Unit, condition: Boolean, delay: Long = 100, doOnEnd: (() -> Unit)?) { //todo test the boolean condition
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Thread { waitForFunction() }.start()
                    if (condition) {
                        doOnEnd?.invoke()
                        timer.cancel()
                        timer.purge()
                    }
                }
            }, delay)
        }

        fun wait(waitForFunction: () -> Unit, durationMilliseconds: Long, delay: Long = 0, refreshRate: Long = 100, doOnEnd: (() -> Unit)?) {
            val timer = Timer()
            var elapsedTime: Long = 0
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (elapsedTime == 0L) {
                        Thread { waitForFunction() }.start()
                    }
                    if (elapsedTime >= durationMilliseconds) {
                        doOnEnd?.invoke()
                        timer.cancel()
                        timer.purge()
                    }
                    elapsedTime += refreshRate
                }
            }, delay, refreshRate)
        }

        fun stringListToObservable(list: List<String>): ObservableList<String> {
            return FXCollections.observableList(list)
        }

        fun getNodeById(node: Node, id: String): MutableList<Node> {
            val nodes: MutableList<Node> = arrayListOf()
            node.getChildList()!!.forEach { child ->
                if (child.id!!.contentEquals(id)) {
                    nodes.add(child)
                }
            }
            return nodes
        }

        fun replaceWith(destination: Node, source: Node) {
            source.removeFromParent()
            destination.replaceWith(source) //todo replace those method for custom 1
        }

        fun replaceNode(destination: Node, source: Node) {
            val parent = destination.parent
            val index = getChildNodeIndex(parent, destination)!!
            val childList = parent.getChildList()!!
            childList.removeAt(index)
            childList.add(index, source)
            //todo test this
        }

        fun getDatePickerText(date: String): String {
            val year = DateTime.extractYear(date)
            val month = DateTime.extractMonth(date)
            val day = DateTime.extractDay(date)
            return LocalDate.of(year.toInt(), month.toInt(), day.toInt()).toString()
        }

        fun getChildNodeIndex(parent: Parent, node: Node): Int? {
            parent.getChildList()!!.forEach { child ->
                if (child == node) {
                    return child.indexInParent
                }
            }
            return null
        }

        object HashUtils {
            fun sha512(input: String) = hashString("SHA-512", input)

            fun sha256(input: String) = hashString("SHA-256", input)

            fun sha1(input: String) = hashString("SHA-1", input)

            /**
             * Supported algorithms on Android:
             *
             * Algorithm	Supported API Levels
             * MD5          1+
             * SHA-1	    1+
             * SHA-224	    1-8,22+
             * SHA-256	    1+
             * SHA-384	    1+
             * SHA-512	    1+
             */
            private fun hashString(type: String, input: String): String {
                val HEX_CHARS = "0123456789ABCDEF"
                val bytes = MessageDigest
                    .getInstance(type)
                    .digest(input.toByteArray())
                val result = StringBuilder(bytes.size * 2)

                bytes.forEach {
                    val i = it.toInt()
                    result.append(HEX_CHARS[i shr 4 and 0x0f])
                    result.append(HEX_CHARS[i and 0x0f])
                }

                return result.toString()
            }
        }

        fun getFirstNodeById(root: Node, id: String): Node? {
            root.getChildList()!!.forEach { child ->
                if (child.id!!.contentEquals(id)) {
                    return child
                }
            }
            return null
        }

        fun findFirstNodeById(node: Node, id: String): Node? {
            if (node.id != id) {
                if (node.getChildList() != null) {
                    node.getChildList()!!.forEach { childNode ->
                        if (childNode.id == id) {
                            return childNode
                        }
                    }
                }
            }
            return findFirstNodeById(node, id)
        }

        fun getChildTextFields(node: Node, nodes: MutableList<Node> = mutableListOf()): MutableList<Node>? {
            if (node.getChildList() != null) {
                node.getChildList()!!.forEach { childNode ->
                    getChildTextFields(childNode, nodes)
                    if (childNode.javaClass.simpleName == "TextField") {
                        nodes.add(childNode)
                    }
                }
            }
            return nodes
        }

        fun setVisibilityGone(node: Node) {
            node.visibleProperty().set(false)
            node.managedProperty().bind(node.visibleProperty())
        }

        fun setVisibilityGone(node: Node, boolean: Boolean = true) {
            node.visibleProperty().set(!boolean)
            node.managedProperty().bind(node.visibleProperty())
        }
    }
}