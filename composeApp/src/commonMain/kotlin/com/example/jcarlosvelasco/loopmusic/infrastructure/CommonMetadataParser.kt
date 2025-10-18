package com.example.jcarlosvelasco.loopmusic.infrastructure

class CommonMetadataParser {
    fun fixEncoding(text: String?): String? {
        return text?.let { original ->
            val fixes = mapOf(
                "â€™" to "'",
                "â€œ" to """,  
                "â€\u009D" to """,
                "â€TMt" to "'",
                "â€TMre" to "'re",
                "â€TMs" to "'s",
                "â€TMd" to "'d",
                "â€TMll" to "'ll",
                "â€TMve" to "'ve"
            )

            var fixed = original
            fixes.forEach { (wrong, correct) ->
                fixed = fixed.replace(wrong, correct)
            }
            fixed
        }
    }
}