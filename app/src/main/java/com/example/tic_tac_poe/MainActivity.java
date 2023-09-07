package com.example.tic_tac_poe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tic_tac_poe.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //private ActivityMainBinding binding;
    private LinearLayout board;
    private ArrayList<Button> squares = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        View.OnClickListener listener = (view)->{
            Button btn = (Button) view;
            //Если текст из кнопки не пустой то выходим из события
            if(!btn.getText().toString().equals("")) return;
            //Ставим либо X либо O в зависимости от того, кто ходит
            if(GameInfo.isTurn) {
                btn.setText(GameInfo.firstSymbol);
                int [] comb = calcWinnPositions(GameInfo.firstSymbol);
                if(comb != null) Toast.makeText(
                        getApplicationContext(),
                        "winner is "+GameInfo.firstSymbol,
                        Toast.LENGTH_LONG).show();
            }
            else {
                btn.setText(GameInfo.secondSymbol);
                int [] comb = calcWinnPositions(GameInfo.secondSymbol);
                if(comb != null) Toast.makeText(
                        getApplicationContext(),
                        "winner is "+GameInfo.secondSymbol,
                        Toast.LENGTH_LONG).show();
            }
            //Меняем очередность хода true->false
            GameInfo.isTurn = !GameInfo.isTurn;

        };
        board = findViewById(R.id.board);
        generateBoard(3,3,board);
        setListenerToSquares(listener);
        initClearBordBtn();
    }

    private void initClearBordBtn(){
        Button clearBtn = findViewById(R.id.clear_board_value);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        getApplicationContext(),
                        "Новая игра",
                        Toast.LENGTH_LONG).show();
                for(Button square: squares) {
                    square.setText("");
                    square.setBackgroundTintList(
                            ContextCompat.getColorStateList(
                                    getApplicationContext(),
                                    R.color.gray_light));
                }
            }
        });
    }

    public void generateBoard(int rowCount, int columnCount, LinearLayout board){
        //Генерация строчек от 0 до rowCount
        for(int row = 0; row < rowCount;row++){
            //Создаем контейнер(нашу строку) и вносим ее в board
            LinearLayout rowContainer = generateRow(columnCount);
            board.addView(rowContainer);
        }
    }
    //Устанавливаем слушателя всем кнопкам
    private void setListenerToSquares(View.OnClickListener listener){
        for(int i = 0; i < squares.size();i++)
            //Получаем кнопку из списка и устанавливаем ей слушателя события
            squares.get(i).setOnClickListener(listener);
    }
    //метод генерации строк для board
    private LinearLayout generateRow(int squaresCount){
        //Созданный контейнер (стоока) который будет возвращен с кнопками
        LinearLayout rowContainer = new LinearLayout(getApplicationContext());
        rowContainer.setOrientation(LinearLayout.HORIZONTAL);
        rowContainer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        for(int square = 0; square < squaresCount;square++){
            //Создаем кнопку для добавления в строку
            Button button = new Button(getApplicationContext());
            //Устанавливаем цвет с помощью tint
            button.setBackgroundTintList(
                    ContextCompat.getColorStateList(
                            getApplicationContext(),
                            R.color.gray_light));
            button.setWidth(convertToPixel(50));
            button.setHeight(convertToPixel(90));
            rowContainer.addView(button);//Добавляем кнопку в строку
            squares.add(button);
        }
        return rowContainer;
    }
    public int convertToPixel(int digit){
        float density = getApplicationContext()
                .getResources().getDisplayMetrics().density;
        return (int)(digit * density + 0.5);//перевод
    }
    public int [] calcWinnPositions(String symbol){
        //перебор всем комбинаций
        for(int i = 0; i < GameInfo.winCombination.length;i++){
            //Найдена ли комбинайия
            boolean findComb = true;
            //Перебираем все три символа пример [0,1,2]
            for(int j = 0; j < GameInfo.winCombination[0].length;j++){
                int index = GameInfo.winCombination[i][j];//0, 1, 2
                //Смотрим кнопка с индексом index имеет в себе symbol или нет
                if (!squares.get(index).getText().toString().equals(symbol)) {
                    //если нет то комбинация не выйгрышная
                    findComb = false;
                    break;
                }
            }
            //если комбинация не поменялась на false то она выйгрышная
            if(findComb) return GameInfo.winCombination[i];
        }
        return null;

    }


}