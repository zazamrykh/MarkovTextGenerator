# MarkovTextGenerator
Program that 1) get text 2) make Markov matrix of transition from one word to another that contains probabilities of appearance next word after previous 3) generate new text
First of all we must put text in data.txt
Then read from data.txt, put words in dictionary, set indexes, count quantity of usages and make matrix of number of usages one word after another.
Then devide rows of matrix on quantity of usages of this word and we got matrix of probabilities of transition to next word from previous
Generate text. Generations starts from first word at that text and ends when we stumble at last word at text with probability of ending text after this word.
You can use method printMatrix() to see what is hapening in every moment.
That program is based on Markov chain.
