import java.io.*

fun main() {
    // Hello everybody to Markov Text Generator!
    // I will show you how to generate random text using Markov chain! LETS'GO!!!!!

    // First, greeting to you, my friends!!!
    println("Hello world!")
    // Then make Markov matrix! Firstly it will keep number of usages next word after current, but then we will divide
    // Every element in a row at number of all usages of next word.
    val transitionMatrix = SquareMatrix(1)

    // Make dictionary that will keep words, their index and quantity.
    val dictionary = mutableMapOf<String, Pair<Int, Int>>()

    // Open file that keep initial text
    val file = File("data.txt")
    val reader = BufferedReader(FileReader(file, Charsets.UTF_8))

    // Directly algorithm of drafting dictionary and Markov matrix.
    var indexOfPreviousWord = -1
    var indexOfCurrentWord: Int
    var isFirstWord = true
    var isRepeatedWord: Boolean
    var numberOfAllWords = 0
    for (line in reader.lines()) {
        val wordsInLine = line.split(" ")
        for (word in wordsInLine) {
            numberOfAllWords++
            if (dictionary.containsKey(word)) {
                indexOfCurrentWord = dictionary.getValue(word).first
                dictionary[word] = dictionary[word]!!.copy(second = dictionary[word]!!.second + 1)
                isRepeatedWord = true
            } else {
                indexOfCurrentWord = dictionary.size
                dictionary[word] = Pair(indexOfCurrentWord, 1)
                isRepeatedWord = false
            }

            if (isFirstWord) {
                indexOfPreviousWord = indexOfCurrentWord
                isFirstWord = false
                continue
            }

            if (isRepeatedWord) {
                transitionMatrix.setElementAt(
                    transitionMatrix
                        .getElementAt(indexOfPreviousWord, indexOfCurrentWord) + 1,
                    indexOfPreviousWord, indexOfCurrentWord
                )
            } else {
                transitionMatrix.expandMatrix()
                transitionMatrix.setElementAt(
                    1.0,
                    indexOfPreviousWord, indexOfCurrentWord
                )
            }
            indexOfPreviousWord = indexOfCurrentWord
        }
    }
    reader.close()
    // If you look at resulting matrix, you can see that last word row don't give sum of probabilities equals 1.
    // It will be used in future for quiting of generation text.

    // Divide every row of matrix at number of usages, so we will get probabilities matrix.
    val matrixSize = dictionary.size
    var numberOfAppearance: Int
    var indexOfWord: Int
    for (dic in dictionary) {
        numberOfAppearance = dic.value.second
        indexOfWord = dic.value.first
        for (j in 0 until matrixSize) {
            if (numberOfAppearance == 0) {
                transitionMatrix.setElementAt(
                    -1.0,
                    indexOfWord, j
                )
            } else {
                transitionMatrix.setElementAt(
                    transitionMatrix.getElementAt(indexOfWord, j) / numberOfAppearance,
                    indexOfWord, j
                )
            }
        }
    }

    // Generation algorithm. We quit that when we stumble at last word with probability of quiting after it word in
    // initial text.
    File("result.txt").bufferedWriter().use { out ->
        var wordIndex = 0
        var randomWord: String
        out.write("${getWordUsingIndex(dictionary, wordIndex)} ")
        for (i in 0..1000000000) {
            wordIndex = getRandomIndexOfWordAfter(transitionMatrix, wordIndex)
            randomWord = getWordUsingIndex(dictionary, wordIndex).toString()
            if (wordIndex == -1) {
                break
            }
            if ((i + 1) % 15 == 0) {
                out.write("$randomWord \n")
            } else {
                out.write("$randomWord ")
            }
        }
    }
}

fun getWordUsingIndex(dictionary: MutableMap<String, Pair<Int, Int>>, index: Int): String? {
    for (dic in dictionary) {
        if (index == dic.value.first) {
            return dic.key
        }
    }
    return null
}

fun getRandomIndexOfWordAfter(probabilities: SquareMatrix, wordIndex: Int): Int {
    val rowOfProbabilityNextWord: MutableList<Double> = probabilities.getRow(wordIndex)
    var randomValue = getRandomValueFromZeroToOne()
    for (indexOfNextWord in 0 until rowOfProbabilityNextWord.size) {
        if (rowOfProbabilityNextWord[indexOfNextWord] < 0) {
            return -1
        }
        randomValue -= rowOfProbabilityNextWord[indexOfNextWord]
        if (randomValue < 0) {
            return indexOfNextWord
        }
    }
    if (randomValue > 0) {
        return -1
    }
    return rowOfProbabilityNextWord.size - 1
}

fun getRandomValueFromZeroToOne(): Double {
    return ((0..10000).shuffled().last().toDouble() / 10000)
}
