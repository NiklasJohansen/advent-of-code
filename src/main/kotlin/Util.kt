
fun readText(path: String) =
    object {}.javaClass.getResource(path)!!
        .readText()
        .replace("\r", "")

fun readLines(path: String, delimiter: String = "\n") =
    readText("./$path").split(delimiter)