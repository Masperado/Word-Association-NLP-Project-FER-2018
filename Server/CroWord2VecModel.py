import _pickle as pickle
from scipy import spatial
import random
from operator import itemgetter

from Croatian_stemmer import stem


class CroWord2VecModel:

    def __init__(self, dictionary):
        with open(dictionary, 'rb') as dict_items_open:
            self.vectors = pickle.load(dict_items_open)

    def generate_random_word(self):
        return random.choice(list(self.vectors.keys()))

    def generate_word_list(self, word, skip_list):
        return self._solve([word], skip_list)[0:4]

    def generate_solution(self, word_list, skip_list):
        return self._solve(word_list, skip_list)[0]

    def _cosine_similarity(self, word1, word2):
        if (word1 not in self.vectors) | (word2 not in self.vectors):
            return 1
        return 1 - spatial.distance.cosine(self.vectors[word1], self.vectors[word2])

    def _solve(self, vector_list, skip):

        word_list = []

        for word in self.vectors:
            similarity = 0
            for a in vector_list:
                if a == word:
                    break
                similarity += abs(self._cosine_similarity(a, word))
            if similarity / len(vector_list) > 0.3:
                word_list.append(tuple((word, similarity / len(vector_list))))

        word_list.sort(key=itemgetter(1), reverse=True)

        list = self._remove_stem(word_list[0:50], vector_list, skip)
        list = self._optimze(list)
        return list[0:4]


    def _optimze(self, word_list):
        new_word_list = []
        new_word_list.append(word_list[0])

        for i in range (1, len(word_list)):
            for j in range (0, len(new_word_list)):
                if 0.5 > self._cosine_similarity(word_list[i], new_word_list[j]):
                    new_word_list.append(word_list[i])
                    break
            if len(new_word_list)>=4:
                break

        return new_word_list


    def _remove_stem(self, word_list, vector_list, skip_list):
        new_word_list = []

        stem_list = []
        for i in range(0, len(vector_list)):
            stem_list.append(stem(vector_list[i]))
        for i in range(0, len(skip_list)):
            stem_list.append(stem(skip_list[i]))

        for word in word_list:

            word_doesnt_exist = True
            stem_word = stem(word[0])
            if stem_word == "":
                continue
            for listWord in stem_list:
                if stem_word == listWord:
                    word_doesnt_exist = False
                    break
            if word_doesnt_exist:
                new_word_list.append(word[0])
                stem_list.append(stem_word)

        return new_word_list
