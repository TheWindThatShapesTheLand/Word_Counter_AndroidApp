package com.example.wordcounter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<String, Integer> wordMap;
    private int totalWords;

    private boolean onlyRussian = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.inputText);

        Button btnProcess = findViewById(R.id.btnProcess);
        Button btnLoadFile = findViewById(R.id.btnLoadFile);
        Button btnToggleRussian = findViewById(R.id.btnToggleRussian);

        GridLayout sortGroup = findViewById(R.id.sortGroup);

        Button btnSortAsc = findViewById(R.id.btnSortAsc);
        Button btnSortDesc = findViewById(R.id.btnSortDesc);
        Button btnSortAlphaAsc = findViewById(R.id.btnSortAlphaAsc);
        Button btnSortAlphaDesc = findViewById(R.id.btnSortAlphaDesc);

        Button btnClear = findViewById(R.id.btnClear);

        TextView result = findViewById(R.id.result);

        ActivityResultLauncher<Intent> filePicker =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        res -> {
                            if (res.getResultCode() == RESULT_OK && res.getData() != null) {
                                Uri uri = res.getData().getData();

                                try {
                                    BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(getContentResolver().openInputStream(uri))
                                    );

                                    StringBuilder text = new StringBuilder();
                                    String line;

                                    while ((line = reader.readLine()) != null) {
                                        text.append(line).append("\n");
                                    }

                                    reader.close();
                                    input.setText(text.toString());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

        btnLoadFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/*");
            filePicker.launch(intent);
        });

        btnToggleRussian.setOnClickListener(v -> {
            onlyRussian = !onlyRussian;

            btnToggleRussian.setText(
                    onlyRussian ? "Только русские слова: ON" : "Только русские слова: OFF"
            );

            if (wordMap != null) {
                updateResult(wordMap);
            }
        });

        btnProcess.setOnClickListener(v -> {
            String text = input.getText().toString();

            wordMap = WordCounter.countWords(text);
            Map<String, Integer> filtered = applyFilter(wordMap);
            totalWords = WordCounter.getTotalWords(filtered);

            showResult(filtered);

            sortGroup.setVisibility(GridLayout.VISIBLE);
        });

        btnClear.setOnClickListener(v -> {

            input.setText("");

            wordMap = null;
            totalWords = 0;

            onlyRussian = false;
            btnToggleRussian.setText("Только русские слова: OFF");

            sortGroup.setVisibility(GridLayout.GONE);
        });

        btnSortAsc.setOnClickListener(v ->
                updateResult(WordSorter.sortAscending(wordMap))
        );

        btnSortDesc.setOnClickListener(v ->
                updateResult(WordSorter.sortDescending(wordMap))
        );

        btnSortAlphaAsc.setOnClickListener(v ->
                updateResult(WordSorter.sortAlphaAsc(wordMap))
        );

        btnSortAlphaDesc.setOnClickListener(v ->
                updateResult(WordSorter.sortAlphaDesc(wordMap))
        );

        input.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sortGroup.setVisibility(GridLayout.GONE);
            }

            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void showResult(Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder();

        sb.append("Всего слов: ")
                .append(totalWords)
                .append("\n\n");

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue())
                    .append("\n");
        }

        TextView result = findViewById(R.id.result);
        result.setText(sb.toString());
    }

    private void updateResult(Map<String, Integer> map) {

        Map<String, Integer> filtered = applyFilter(map);

        totalWords = WordCounter.getTotalWords(filtered);

        showResult(filtered);
    }

    private Map<String, Integer> applyFilter(Map<String, Integer> map) {

        if (!onlyRussian || map == null) {
            return map;
        }

        return WordFilter.keepRussianOnly(map);
    }
}