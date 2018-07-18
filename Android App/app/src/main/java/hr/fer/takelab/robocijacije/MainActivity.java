package hr.fer.takelab.robocijacije;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.fer.takelab.robocijacije.model.RandomWord;
import hr.fer.takelab.robocijacije.model.WordList;
import hr.fer.takelab.robocijacije.model.WordListRequest;
import hr.fer.takelab.robocijacije.service.RandomWordService;
import hr.fer.takelab.robocijacije.service.WordListService;
import hr.fer.takelab.robocijacije.words.Words;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.one)
    Button one;
    @BindView(R.id.two)
    Button two;
    @BindView(R.id.three)
    Button three;
    @BindView(R.id.four)
    Button four;
    @BindView(R.id.finalButton)
    Button finalButton;

    @BindView(R.id.first_word)
    Button firstWord;
    @BindView(R.id.second_word)
    Button secondWord;
    @BindView(R.id.third_word)
    Button thirdWord;
    @BindView(R.id.fourth_word)
    Button fourthWord;

    @BindView(R.id.user_input)
    EditText input;
    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.first_wrapper)
    LinearLayout firstWrapper;
    @BindView(R.id.second_wrapper)
    LinearLayout secondWrapper;
    @BindView(R.id.third_wrapper)
    LinearLayout thirdWrapper;
    @BindView(R.id.fourth_wrapper)
    LinearLayout fourthWrapper;
    @BindView(R.id.final_wrapper)
    LinearLayout finalWrapper;

    @BindView(R.id.solving_layout)
    RelativeLayout solvingLayout;

    @BindView(R.id.wheel)
    AVLoadingIndicatorView avi;

    @BindView(R.id.first_word_card)
    CardView firstWordCard;
    @BindView(R.id.second_word_card)
    CardView secondWordCard;
    @BindView(R.id.third_word_card)
    CardView thirdWordCard;
    @BindView(R.id.fourth_word_card)
    CardView fourthWordCard;

    String baseUrl = "http://35.189.67.111:80/cro/";
    String randomWordRelative = "randomword";

    RandomWord randomWord;

    Words firstFour;
    Words secondFour;
    Words thirdFour;
    Words fourthFour;
    Words finalFour;

    int currentCategory = 1;

    Map<Integer, Words> numToCategory = new HashMap<>();
    Map<Integer, Button> numToCategoryButton = new HashMap<>();
    Map<Integer, LinearLayout> numToWrapper = new HashMap<>();
    Map<Integer, Button> numToWordButton = new HashMap<>();

    List<String> skipList = new ArrayList<>();

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        finalFour = new Words("", new String[]{"error", "error", "error", "error"});
        firstFour = new Words("", new String[]{"error", "error", "error", "error"});
        secondFour = new Words("", new String[]{"error", "error", "error error", "error"});
        thirdFour = new Words("", new String[]{"error", "error", "error", "error"});
        fourthFour = new Words("", new String[]{"error", "error", "error", "error"});

        numToCategory.put(1, firstFour);
        numToCategory.put(2, secondFour);
        numToCategory.put(3, thirdFour);
        numToCategory.put(4, fourthFour);
        numToCategory.put(5, finalFour);

        numToCategoryButton.put(1, one);
        numToCategoryButton.put(2, two);
        numToCategoryButton.put(3, three);
        numToCategoryButton.put(4, four);
        numToCategoryButton.put(5, finalButton);

        numToWrapper.put(1, firstWrapper);
        numToWrapper.put(2, secondWrapper);
        numToWrapper.put(3, thirdWrapper);
        numToWrapper.put(4, fourthWrapper);
        numToWrapper.put(5, finalWrapper);

        numToWordButton.put(1, firstWord);
        numToWordButton.put(2, secondWord);
        numToWordButton.put(3, thirdWord);
        numToWordButton.put(4, fourthWord);

        startAnim();
        getNewTask();
    }

    @OnClick(R.id.submit)
    public void submit() {
        Words currentWords = numToCategory.get(currentCategory);
        Log.d("pressed", currentWords.toString());
        if (input.getText().toString().toLowerCase().trim().equals(currentWords.getAnswer().toLowerCase().trim())) {
            if (currentCategory < 5) {
                finalFour.setRevealed(currentCategory, true);
            }
            currentWords.setSolved(true);
            numToCategoryButton.get(currentCategory).setEnabled(false);
            display();
        } else {
            YoYo.with(Techniques.Shake).playOn(input);
        }
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void display() {
        if (currentCategory < 5) {
            currentCategory += 1;
        } else {
            //TODO: makni ovo
            Toast.makeText(this, "Congratulations", Toast.LENGTH_LONG).show();
            startAnim();
            getNewTask();
        }
        Words nextWords = numToCategory.get(currentCategory);
        firstWord.setText(nextWords.getWord(1));
        secondWord.setText(nextWords.getWord(2));
        thirdWord.setText(nextWords.getWord(3));
        fourthWord.setText(nextWords.getWord(4));
        input.setText("");
        switching();
        changeColors();
    }

    private void getNewTask() {

        final RandomWordService randomWordService = retrofit.create(RandomWordService.class);

        randomWordService.getWord(randomWordRelative).enqueue(new Callback<RandomWord>() {
            @Override
            public void onResponse(Call<RandomWord> call, Response<RandomWord> response) {
                randomWord = response.body();
                //Toast.makeText(MainActivity.this, randomWord.getWord(), Toast.LENGTH_LONG).show();
                numToCategory.get(5).setAnswer(randomWord.getWord());
                skipList.add(randomWord.getWord());

                final WordListService getWords = retrofit.create(WordListService.class);

                WordListRequest solutionsRequest = new WordListRequest(randomWord.getWord(), skipList.toArray(new String[0]));
                Call<WordList> wordListCall = getWords.getResponse(solutionsRequest);
                wordListCall.enqueue(new Callback<WordList>() {
                    @Override
                    public void onResponse(Call<WordList> call, Response<WordList> response) {
                        WordList list = response.body();

                        if (response.body() != null) {
                            String[] words = list.getWordList();
                            for (String s : words) {
                                skipList.add(s);
                            }
                            numToCategory.get(5).setWords(words);

                            for (int i = 1; i < 5; i++) {
                                WordListRequest nthCategoryRequest = new WordListRequest(words[i - 1], skipList.toArray(new String[0]));
                                Log.d("skipList", "0: " + skipList.toArray(new String[0])[0] + "1: " + skipList.toArray(new String[0])[1] + "2: " + skipList.toArray(new String[0])[2] );
                                Call<WordList> nthCategoryCall = getWords.getResponse(nthCategoryRequest);
                                final int count = i;
                                nthCategoryCall.enqueue(new Callback<WordList>() {
                                    @Override
                                    public void onResponse(Call<WordList> call, Response<WordList> response) {
                                        WordList list = response.body();
                                        String[] words = list.getWordList();

                                        for (String s : words) {
                                            skipList.add(s);
                                        }

                                        numToCategory.get(count).setWords(words);
                                        numToCategory.get(count).setAnswer(numToCategory.get(5).getWord(count));
                                        currentCategory = 1;
                                        reEnableEverything();
                                        skipList.clear();
                                        one.performClick();
                                        stopAnim();
                                    }

                                    @Override
                                    public void onFailure(Call<WordList> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WordList> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "failed to acquire words from api", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<RandomWord> call, Throwable t) {
                firstWord.setText(t.toString());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void reEnableEverything() {
        for (int i = 1; i < 6; i++) {
            numToCategoryButton.get(i).setEnabled(true);
            numToCategory.get(i).setSolved(false);
            for(int j = 1; j < 5; j++){
                numToCategory.get(i).setRevealed(j, false);
            }
        }
    }

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.finalButton})
    public void switching() {
        YoYo.with(Techniques.FadeInUp).playOn(firstWord);
        YoYo.with(Techniques.FadeInUp).playOn(firstWordCard);
        YoYo.with(Techniques.FadeInUp).playOn(secondWord);
        YoYo.with(Techniques.FadeInUp).playOn(secondWordCard);
        YoYo.with(Techniques.FadeInUp).playOn(thirdWord);
        YoYo.with(Techniques.FadeInUp).playOn(thirdWordCard);
        YoYo.with(Techniques.FadeInUp).playOn(fourthWord);
        YoYo.with(Techniques.FadeInUp).playOn(fourthWordCard);
    }

    @OnClick(R.id.one)
    public void displayFirstFour() {
        currentCategory = 1;
        firstWord.setText(firstFour.getWord(1));
        secondWord.setText(firstFour.getWord(2));
        thirdWord.setText(firstFour.getWord(3));
        fourthWord.setText(firstFour.getWord(4));
        changeColors();
    }

    private void changeColors() {
        for (Integer i : numToCategoryButton.keySet()) {
            numToWrapper.get(i).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            if (!numToCategoryButton.get(i).isEnabled()) {
                numToWrapper.get(i).setBackgroundColor(getResources().getColor(R.color.light_grey));
            }
        }
        numToWrapper.get(currentCategory).setBackgroundColor(getResources().getColor(R.color.colorAccent));

        for (int i = 1; i < 5; i++) {
            if (numToCategory.get(currentCategory).checkIfRevealed(i)) {
                numToWordButton.get(i).setTextColor(getResources().getColor(R.color.black));
            } else {
                numToWordButton.get(i).setTextColor(getResources().getColor(R.color.white));
            }
            ;
        }
    }

    @OnClick(R.id.first_word)
    public void clickFirstWord() {
        if (currentCategory < 5) {
            numToWordButton.get(1).setTextColor(getResources().getColor(R.color.black));
            numToCategory.get(currentCategory).setRevealed(1, true);
        }
    }

    @OnClick(R.id.second_word)
    public void clickSecondWord() {
        if (currentCategory < 5) {
            numToWordButton.get(2).setTextColor(getResources().getColor(R.color.black));
            numToCategory.get(currentCategory).setRevealed(2, true);
        }
    }

    @OnClick(R.id.third_word)
    public void clickThirdWord() {
        if (currentCategory < 5) {
            numToWordButton.get(3).setTextColor(getResources().getColor(R.color.black));
            numToCategory.get(currentCategory).setRevealed(3, true);
        }
    }

    @OnClick(R.id.fourth_word)
    public void clickFourthWord() {
        if (currentCategory < 5) {
            numToWordButton.get(4).setTextColor(getResources().getColor(R.color.black));
            numToCategory.get(currentCategory).setRevealed(4, true);
        }
    }

    @OnClick(R.id.two)
    public void displaySecondFour() {
        currentCategory = 2;
        firstWord.setText(secondFour.getWord(1));
        secondWord.setText(secondFour.getWord(2));
        thirdWord.setText(secondFour.getWord(3));
        fourthWord.setText(secondFour.getWord(4));
        changeColors();
    }

    @OnClick(R.id.three)
    public void displayThirdFour() {
        currentCategory = 3;
        firstWord.setText(thirdFour.getWord(1));
        secondWord.setText(thirdFour.getWord(2));
        thirdWord.setText(thirdFour.getWord(3));
        fourthWord.setText(thirdFour.getWord(4));
        changeColors();
    }

    @OnClick(R.id.four)
    public void displayFourthFour() {
        currentCategory = 4;
        firstWord.setText(fourthFour.getWord(1));
        secondWord.setText(fourthFour.getWord(2));
        thirdWord.setText(fourthFour.getWord(3));
        fourthWord.setText(fourthFour.getWord(4));
        changeColors();
    }

    @OnClick(R.id.finalButton)
    public void displayFinalFour() {
        currentCategory = 5;
        firstWord.setText(finalFour.getWord(1));
        secondWord.setText(finalFour.getWord(2));
        thirdWord.setText(finalFour.getWord(3));
        fourthWord.setText(finalFour.getWord(4));
        changeColors();
    }

    void startAnim() {
        avi.smoothToShow();
        //avi.show();
        solvingLayout.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeOutDown).playOn(solvingLayout);
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.smoothToHide();
        //avi.hide();
        solvingLayout.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeInDown).playOn(solvingLayout);

    }



    //    @OnClick(R.id.submit)
//    public void submit(){
//        final RandomWordService service = retrofit.create(RandomWordService.class);
//
//        service.getWord(randomWordRelative).enqueue(new Callback<RandomWord>() {
//            @Override
//            public void onResponse(Call<RandomWord> call, Response<RandomWord> response) {
//                RandomWord randomWord = response.body();
//                firstWord.setText("yay!");
//                Toast.makeText(MainActivity.this, randomWord.getWord(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFailure(Call<RandomWord> call, Throwable t) {
//                firstWord.setText(t.toString());
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
