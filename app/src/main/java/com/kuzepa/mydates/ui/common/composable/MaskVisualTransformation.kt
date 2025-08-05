import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

internal class MaskVisualTransformation(
    private val mask: String,
    private val delimiters: CharArray,
    private val textColor: Color,
    private val maskColor: Color,
) : VisualTransformation {
    private val maskGroups = mask.split(delimiters = delimiters).filter { it.isNotEmpty() }

    // Map of [delimiter positions in the mask] -> [the delimiter itself]
    private val delimiterMap = mask
        .withIndex()  // Get (index, char) pairs
        .filter { (_, char) -> char in delimiters }  // Keep only delimiters
        .associate { (index, char) -> index to char }  // Build map: Index -> Delimiter

    // The total number of characters that can be entered by the user
    private val totalInputLength = maskGroups.sumOf { it.length }

    override fun filter(text: AnnotatedString): TransformedText {
        // Text formatted to match the mask
        val formattedText = formatText(text.text)
        val annotatedString = buildAnnotatedString {
            append(formattedText)
            applyStyles(text.text)
        }
        // Offset the cursor position from the original position of the user input
        // to the correct position in the mask ans vice versa
        val offsetMapping = MaskOffsetMapping(
            mask,
            maskGroups,
            delimiterMap,
            text.length
        )
        return TransformedText(annotatedString, offsetMapping)
    }

    private fun formatText(input: String): String {
        val inputDigits = input.take(totalInputLength)
        val resultText = StringBuilder()
        var inputIndex = 0

        for (maskIndex in mask.indices) {
            resultText.append(
                if (delimiterMap.containsKey(maskIndex)) {
                    // Add delimiter to formatted test
                    delimiterMap[maskIndex]
                } else {
                    if (inputIndex < inputDigits.length) {
                        // Add user input to the correct position in the mask
                        inputDigits[inputIndex++]
                    } else {
                        // Add the mask placeholder if there is no user input at this position
                        mask[maskIndex]
                    }
                }
            )
        }

        return resultText.toString()
    }

    private fun AnnotatedString.Builder.applyStyles(input: String) {
        val inputDigits = input.take(totalInputLength)
        var inputIndex = 0

        for (maskIndex in mask.indices) {
            if (delimiterMap.containsKey(maskIndex)) {
                // Set style for delimiter
                addStyle(SpanStyle(color = maskColor), maskIndex, maskIndex + 1)
            } else {
                val isUserInput = inputIndex < inputDigits.length
                val color = if (isUserInput) textColor else maskColor
                addStyle(SpanStyle(color = color), maskIndex, maskIndex + 1)
                if (isUserInput) inputIndex++
            }
        }
    }
}

private class MaskOffsetMapping(
    mask: String,
    maskGroups: List<String>,
    private val delimiterMap: Map<Int, Char>,
    private val originalLength: Int
) : OffsetMapping {
    private val maskGroupSizes = maskGroups.map { it.length }
    private val transformedLength = mask.length

    override fun originalToTransformed(offset: Int): Int {
        // The count of delimiters before the current position
        // (this value have to be added to the original offset to get the transformed offset)
        var delimiterCount = 0
        // The offset index to check if there is a delimiter at the current position
        var lastOffsetIndex = 0
        // Sum of all the previous mask group sizes
        // and the current group size for the current loop
        var sumOfMaskGroupSizes = 0
        for (i in maskGroupSizes.indices) {
            // If there is a delimiter at this position
            // then we need to shift the offset to the end of this delimiter
            while (delimiterMap.containsKey(lastOffsetIndex)) {
                lastOffsetIndex++
                delimiterCount++
            }
            // Adding the current character group size
            // to indicate the upper limit of input length before the next delimiter
            sumOfMaskGroupSizes += maskGroupSizes[i]
            // Note: we subtract 1 from the upper limit
            // because if this is the last character before the next delimiter
            // then we will shift the offset to the end of the next delimiter in the next loop
            if (offset <= sumOfMaskGroupSizes - 1) {
                // If the offset is before the next delimiter
                // then just add the count of delimiter offsets
                // that are placed before the current position
                return (offset + delimiterCount).coerceIn(0, transformedLength)
            }
            // Set the offset index to the last checked position in the mask
            lastOffsetIndex = sumOfMaskGroupSizes + delimiterCount
        }
        // If the offset is at the end of the input
        // then we move the offset to the last position in the mask
        return transformedLength
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= 0) return 0
        // The count of delimiters before the current position
        // (this value have to be subtracted from the transformed offset to get the original offset)
        var delimiterCount = 0
        // The offset index to check if there is a delimiter at the current position
        var lastOffsetIndex = 0
        // Sum of all the previous mask group sizes
        // and the current group size for the current loop
        var sumOfMaskGroupSizes = 0
        for (i in maskGroupSizes.indices) {
            // If there is a delimiter at this position
            // then we need to shift the offset to the end of this delimiter
            while (delimiterMap.containsKey(lastOffsetIndex)) {
                lastOffsetIndex++
                delimiterCount++
            }
            sumOfMaskGroupSizes += maskGroupSizes[i]
            if (offset <= sumOfMaskGroupSizes + delimiterCount) {
                // Remove delimiters count to get the offset of the original input
                return (offset - delimiterCount).coerceIn(0, originalLength)
            }
            // Set the offset index to the last checked position in the mask
            lastOffsetIndex = sumOfMaskGroupSizes + delimiterCount
        }
        // If the offset is at the end of the input
        // then we move the offset to the last position in the input
        return originalLength
    }
}