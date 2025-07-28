import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

internal class DateMaskVisualTransformation(
    private val mask: String,
    private val delimiter: Char,
    private val textColor: Color,
    private val maskColor: Color
) : VisualTransformation {

    private val groups = mask.split(delimiter)
    private val totalDigits = groups.sumOf { it.length }

    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText = formatText(text.text)
        val annotatedString = buildAnnotatedString {
            append(formattedText)
            applyStyles(text.text)
        }
        val offsetMapping = DateOffsetMapping(mask, delimiter, text.length)
        return TransformedText(annotatedString, offsetMapping)
    }

    private fun formatText(input: String): String {
        val inputDigits = input.take(totalDigits)
        val result = StringBuilder()
        var inputIndex = 0

        for (group in groups) {
            if (result.isNotEmpty()) result.append(delimiter)
            for (char in group) {
                result.append(if (inputIndex < inputDigits.length) inputDigits[inputIndex++] else char)
            }
        }
        return result.toString()
    }

    private fun AnnotatedString.Builder.applyStyles(input: String) {
        val inputDigits = input.take(totalDigits)
        var inputIndex = 0
        var position = 0

        for (groupIndex in groups.indices) {
            if (groupIndex > 0) {
                addStyle(SpanStyle(color = maskColor), position, position + 1)
                position++
            }

            for (char in groups[groupIndex]) {
                val isUserInput = inputIndex < inputDigits.length
                val color = if (isUserInput) textColor else maskColor
                addStyle(SpanStyle(color = color), position, position + 1)
                position++
                if (isUserInput) inputIndex++
            }
        }
    }
}

class DateOffsetMapping(
    mask: String,
    delimiter: Char,
    private val originalLength: Int
) : OffsetMapping {

    private val groups = mask.split(delimiter)
    private val groupSizes = groups.map { it.length }
    private val transformedLength = mask.length

    override fun originalToTransformed(offset: Int): Int {
        if (offset <= 0) return 0

        var total = 0
        var remaining = offset

        for (i in groupSizes.indices) {
            if (remaining <= 0) break
            val groupSize = groupSizes[i]
            val take = minOf(remaining, groupSize)
            total += take
            remaining -= take

            // Add delimiter after group if needed
            if (take > 0 && i < groupSizes.size - 1) {
                if (take == groupSize || remaining > 0) {
                    total += 1
                }
            }
        }
        return total.coerceAtMost(transformedLength)
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= 0) return 0

        var total = 0
        var remaining = offset

        for (i in groupSizes.indices) {
            if (remaining <= 0) break
            val groupSize = groupSizes[i]

            // Process digits in current group
            val take = minOf(remaining, groupSize)
            total += take
            remaining -= take

            // Skip delimiter if present
            if (take == groupSize && i < groupSizes.size - 1 && remaining > 0) {
                remaining = maxOf(0, remaining - 1)
            }
        }
        return minOf(total, originalLength)
    }
}