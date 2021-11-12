import java.lang.StringBuilder


enum class Alignment {
    LEFT,
    RIGHT,
    CENTER
}

val wordSeparators = arrayListOf(' ')

fun alignText(text: String, alignment: Alignment = Alignment.LEFT, lineWidth: Int = 33): String {
    if (lineWidth < 1)
        throw Exception("The line width argument cannot be negative or null.")

    var lines: MutableList<String> = splitTextByLines(text, lineWidth)

    if (alignment == Alignment.LEFT)
        lines = alignLeft(lines)
    if (alignment == Alignment.RIGHT)
        lines = alignRight(lines, lineWidth)
    if (alignment == Alignment.CENTER)
        lines = alignCenter(lines, lineWidth)

    return convertToString(lines)
}


fun isWordSeparator(symbol: Char): Boolean = wordSeparators.indexOf(symbol) != -1

fun fullAccommodationPossible(text: String, line: String): Boolean =
    text.length == line.length || isWordSeparator(text[line.length])

fun alignLeft(lines: MutableList<String>): MutableList<String> = lines.map { line -> line.trim() }
    .toMutableList()

fun alignRight(lines: MutableList<String>, lineWidth: Int): MutableList<String> = lines.map { line ->
    line
        .padStart(lineWidth)
}
    .toMutableList()


fun alignCenter(lines: MutableList<String>, lineWidth: Int): MutableList<String> = lines.map { line ->
    line
        .padStart((line.length + lineWidth) / 2)
}
    .toMutableList()

fun splitTextByLines(text: String, lineWidth: Int): MutableList<String> {
    var currentText = text
    val lines: MutableList<String> = arrayListOf()
    var line: String

    while (currentText.isNotEmpty()) {
        line = currentText.protectSubstring(0, lineWidth).trim() // Берем часть из начала localText
        if (currentText.getLengthFirstWord() > -1 && currentText.getLengthFirstWord() <= lineWidth) {
            if (fullAccommodationPossible(currentText, line))
                lines.add(line)
            else {
                line = line.substring(0, line.lastIndexOf(wordSeparators.first()))
                lines.add(line)
            }
        } else
            lines.add(line)
        currentText = currentText.protectSubstring(line.length, currentText.length).trim()
    }
    return lines
}

fun convertToString(lines: MutableList<String>): String {
    val result = StringBuilder()
    for (i in 0..lines.size - 2)
        result.append(lines[i] + "\n")
    if (lines.size > 0)
        result.append(lines.last())
    return result.toString()
}

fun String.protectSubstring(start: Int, end: Int): String {
    return if (this.length < end)
        this.substring(start, this.length)
    else
        this.substring(start, end)
}

fun String.getLengthFirstWord(): Int = this.indexOf(wordSeparators.first())


fun main() {
    try {
        val text =
            "Dog cat bird with drink on Monday. Chess play, Bob in Paris, Max a bad, all - the human."
        val alignResult = alignText(text, Alignment.RIGHT, 12)
        print(alignResult)
    } catch (e: Exception) {
        println(e.message)
    }
}







