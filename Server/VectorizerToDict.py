import numpy as np
import _pickle as pickle
from Croatian_stemmer import stem


def main():
    i = 0
    vectors_dict_gene = dict()
    vectors_dict_ana = dict()
    with open('/home/ivan/Downloads/projekt/vector.txt') as vectors: ### mijenjao
        for word in vectors:
            if i == 0:
                i += 1
                continue
            if i == 50000:
                break
            if word.split(" ")[0].endswith("V") or word.split(" ")[0].endswith("A"):
                continue
            key = word.split(" ")[0][:-2]
            if len(stem(key)) > 2:
                values = np.asarray(word.split(" ")[1:301])
                float_values = values.astype(float)
                if i < 1000:
                    print(key)
                    vectors_dict_gene[key] = float_values
                vectors_dict_ana[key] = float_values
            i = i + 1
            print(i)
    with open('hrv_vector_dict_gene.bin', 'wb') as dict_save:
        pickle.dump(vectors_dict_gene, dict_save)
    with open('hrv_vector_dict_ana.bin', 'wb') as dict_save:
        pickle.dump(vectors_dict_ana, dict_save)


if __name__ == "__main__":
    main()
