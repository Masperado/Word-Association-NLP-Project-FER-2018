from math import pow
from gensim.models.keyedvectors import KeyedVectors
from nltk import WordNetLemmatizer, pos_tag
from nltk.corpus import wordnet
from random import randint
import math


class EngWord2VecModel:
    def __init__(self, path):
        """
        Init new model
        :param path: path to gensim word2vec model
        """
        self.model = KeyedVectors.load_word2vec_format(path, binary=True, limit=70000) # TODO check model again
        self.lemmatizer = WordNetLemmatizer()


    def similarity(self, word1, word2): # TODO
        """
        Calculates similarity between 2 words, 0 if not similar, 1 if same
        """
        word1 = self.is_in_vocab(word1)
        word2 = self.is_in_vocab(word2)

        if word1 is None or word2 is None:
            return 0

        return self.model.similarity(word1, word2)

    def generate_solution(self, word_list, skip_list, topn=5): # TODO
        """
        Calculates most similar word2s to given words.
        :param word_list: words in a list
        :return: top 5 most similar words as list where first is most similar and last is least similar,
        or None if none of the words are in model
        """
        # parameters to config
        l_size = 300
        choose_from_top_n = 20
        beta = 1
        gama = 0.5
        ######################

        word_list = [w for w in [self.is_in_vocab(w) for w in word_list] if w is not None]

        if len(word_list) == 0:
            return None

        l = self.model.most_similar(positive=word_list, topn=l_size)
        # l = l[10:]

        l_fitness = []
        for i, w in enumerate(l):

            similarity = w[1]
            for k in word_list:
                similarity *= math.pow(self.model.similarity(w[0], k), beta)

            # todo experiment with functions f(w[1]) and f(count)
            l_fitness.append((i, w[0], similarity * math.pow(self.model.vocab[w[0]].count, gama) * self.score_per_tag(w[0])))

        l_fitness.sort(key=lambda w: w[2], reverse=True)

        return l_fitness[randint(0, choose_from_top_n)][1], l_fitness

    def generate_word_list(self, init_word, skip_list, number_of_words=4): # TODO

        skip_list = []

        """
        Generates new words asociated to init_word
        :param init_word:
        :param number_of_words: number of generated words
        """

        # parametes to config
        limit_vocab = 25000
        l_size = 200
        choose_from_top_n = 40
        remove_top_n = 5
        alfa = 2.5
        beta = 2
        gama = 1.5

        init_word = self.is_in_vocab(init_word)

        if init_word is None:
            return None

        # init_word = lemmatizer.lemmatize(init_word)
        # skip_list = [lemmatizer.lemmatize(word) for word in skip_list if word in model.vocab]

        generated_words = []

        l = self.model.most_similar(positive=init_word, topn=l_size, restrict_vocab=limit_vocab)
        # l = l[remove_top_n:]

        l_fitness = []
        for i, w in enumerate(l):

            similarity = 1
            for s in skip_list:
                similarity *= pow(1 - self.model.similarity(s, w[0]), beta)

            # todo experiment with functions f(w[1]) and f(count)
            l_fitness.append((i, w[0], pow(w[1], alfa) * similarity *
                              pow(self.model.vocab[w[0]].count, gama)))

        # take only most frequent words, similarity does not impact
        l_fitness.sort(key=lambda w: w[2], reverse=True)

        indexes = set()
        while len(indexes) < number_of_words:
            indexes.add(randint(0, choose_from_top_n))

        for i in indexes:
            generated_words.append(l_fitness[i][1])

        return generated_words

    def generate_random_word(self): # TODO make custom list of random words, 500 should do it
        """
        :return: random word from vocabulary
        """
        return self.model.index2word[randint(0, 500)]

    def get_wordnet_pos(self, treebank_tag):
        if treebank_tag.startswith('J'):
            return wordnet.ADJ
        elif treebank_tag.startswith('V'):
            return wordnet.VERB
        elif treebank_tag.startswith('N'):
            return wordnet.NOUN
        elif treebank_tag.startswith('R'):
            return wordnet.ADV
        else:
            return None

    def score_per_tag(self, word):
        treebank_tag = pos_tag([word])[0][1]
        if treebank_tag.startswith('J'):
            return 1  # wordnet.ADJ
        elif treebank_tag.startswith('V'):
            return 1  # wordnet.VERB
        elif treebank_tag.startswith('N'):
            return 1.2  # wordnet.NOUN
        elif treebank_tag.startswith('R'):
            return 0.5  # wordnet.ADV
        else:
            return 0.5

    def is_in_vocab(self, word):
        word = word.lower()

        if word not in self.model.vocab:
            word = self.lemmatize(word)

            if word not in self.model.vocab:
                return None

        return word

    def lemmatize(self, word):
        return self.lemmatizer.lemmatize(word=word, pos=self.get_wordnet_pos(pos_tag([word])[0][1]))
