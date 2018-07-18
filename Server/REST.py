#!/usr/bin/python3

from flask import Flask, jsonify, request, make_response
from CroWord2VecModel import CroWord2VecModel
from EngWord2VecModel import EngWord2VecModel

app = Flask(__name__)

cro_model_ana = CroWord2VecModel(app.root_path + '/hrv_vector_dict_ana.bin')
cro_model_gene = CroWord2VecModel(app.root_path + '/hrv_vector_dict_gene.bin')
eng_model_ana = EngWord2VecModel(app.root_path + '/GoogleNews-vectors-negative300-SLIM-filtered2.bin')
eng_model_gene = eng_model_ana

@app.route('/cro/randomword', methods=['GET'])
def cro_generate_random_word():
    return jsonify({'word': cro_model_gene.generate_random_word()})


@app.route('/cro/wordlist', methods=['POST'])
def cro_generate_word_list():
    if not request.json or 'word' not in request.json or 'skip_list' not in request.json:
        return make_response(jsonify({'error': 'Bad Request'}), 400)
    word = request.json['word']
    skip_list = request.json['skip_list']
    word_list = cro_model_gene.generate_word_list(word, [word, *skip_list])
    return make_response(jsonify({'word_list': word_list}), 200)


@app.route('/cro/solution', methods=['POST'])
def cro_generate_solution():
    if not request.json or 'word_list' not in request.json or 'skip_list' not in request.json:
        return make_response(jsonify({'error': 'Bad Request'}), 400)
    word_list = request.json['word_list']
    skip_list = request.json['skip_list']
    word = cro_model_ana.generate_solution(word_list, [*word_list, *skip_list])
    return make_response(jsonify({'word': word}), 200)


@app.route('/eng/randomword', methods=['GET'])
def eng_generate_random_word():
    return jsonify({'word': eng_model_gene.generate_random_word()})


@app.route('/eng/wordlist', methods=['POST'])
def eng_generate_word_list():
    if not request.json or 'word' not in request.json or 'skip_list' not in request.json:
        return make_response(jsonify({'error': 'Not found'}), 404)
    word = request.json['word']
    skip_list = request.json['skip_list']
    word_list = eng_model_gene.generate_word_list(word, [word, *skip_list])
    return make_response(jsonify({'word_list': word_list}), 200)


@app.route('/eng/solution', methods=['POST'])
def eng_generate_solution():
    if not request.json or 'word_list' not in request.json or 'skip_list' not in request.json:
        return make_response(jsonify({'error': 'Bad Request'}), 400)
    word_list = request.json['word_list']
    skip_list = request.json['skip_list']
    word = eng_model_ana.generate_solution(word_list, [*word_list, *skip_list])
    return make_response(jsonify({'word': word}), 200)


if __name__ == '__main__':
    app.run(threaded=True, host='0.0.0.0', port=80)
