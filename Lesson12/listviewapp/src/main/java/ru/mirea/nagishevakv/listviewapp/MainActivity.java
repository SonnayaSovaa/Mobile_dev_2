package ru.mirea.nagishevakv.listviewapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String[] authors = {"Лев Толстой", "Фёдор Достоевский", "Михаил Булгаков", "Габриэль Гарсиа Маркес","Джордж Оруэлл", "Джоан Роулинг", "Дж. Р. Р. Толкин", "Антуан де Сент-Экзюпери", "Фрэнсис Скотт Фицджеральд", "Герман Мелвилл", "Лев Толстой", "Оскар Уайльд", "Эрих Мария Ремарк", "Джек Лондон", "Рэй Брэдбери", "Эрнест Хемингуэй", "Михаил Шолохов", "Борис Пастернак", "Данте Алигьери", "Гомер", "Джейн Остин", "Шарлотта Бронте", "Эмили Бронте", "Виктор Гюго", "Александр Дюма", "Артур Конан Дойл", "Агата Кристи", "Стивен Кинг", "Джон Стейнбек", "Харпер Ли"};

    private String[] books = {"Война и мир", "Преступление и наказание", "Мастер и Маргарита", "Сто лет одиночества", "1984", "серия о Гарри Поттере", "Властелин колец", "Маленький принц", "Великий Гэтсби", "Моби Дик, или Белый Кит", "Анна Каренина", "Портрет Дориана Грея", "Три товарища", "Мартин Иден", "451 градус по Фаренгейту", "Старик и море", "Тихий Дон", "Доктор Живаго", "Божественная комедия", "Одиссея", "Гордость и предубеждение", "Джейн Эйр", "Грозовой перевал", "Отверженные", "Граф Монте-Кристо", "Приключения Шерлока Холмса", "Убийство в Восточном экспрессе", "Оно", "Гроздья гнева", "Убить пересмешника"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView countriesList = findViewById(R.id.country_list_view);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, authors) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                text2.setText(getItem(position).toString());
                text1.setText(String.valueOf(position+1+" - "+books[position]));
                return view;
            }
        };
        countriesList.setAdapter(adapter);
    }

}