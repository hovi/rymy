package sample


fun Result.toTDHtml(): String {
    return """
        <td class="slovo">${slovo}</td>
        <td class="zakladni-forma">(${zakladniForma})</td>
        <td class="zbytek">${zbytek}</td>
        <td class="shoda">${shoda}</td>
    """.trimIndent()
}

data class Result(
    val slovo: String,
    val zakladniForma: String,
    val shoda: String,
    val zbytek: String,
    val trida: String
) {

    companion object {
        fun parse(text: String, className: String): Result {
            return Result(
                slovo = text.substringAfter("<b>").substringBefore("</b>"),
                zakladniForma = text.substringAfter("(").substringBefore(")"),
                shoda = text.substringAfterLast("<var>").substringBefore("</var>").replace(" ", ""),
                zbytek = text.substringAfter(")").substringBefore("<var>").replace(";", "; ").replace(",", ", "),
                trida = className
            )
        }
    }
}