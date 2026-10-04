package ru.mirea.nagishevakv.backeryproject.presentation;

import android.content.Context;
import java.util.Arrays;
import java.util.List;

/**
 * Класс для классификации токсичности текста.
 * В реальном приложении здесь должна быть загрузка и использование TFLite модели.
 */
public class ToxicityClassifier {
    
    // Список плохих слов для примера (в реальности используется нейросеть)
    private static final List<String> TOXIC_KEYWORDS = Arrays.asList("плохо", "ужасно", "дерьмо", "отвратительно", "тупо");
    private static final List<String> POSITIVE_KEYWORDS = Arrays.asList("вкусно", "отлично", "супер", "рекомендую", "лучший");

    public ToxicityClassifier(Context context) {
        // Здесь должна быть инициализация TFLite:
        // Interpreter tflite = new Interpreter(loadModelFile(context, "toxicity_model.tflite"));
    }

    /**
     * Возвращает оценку токсичности от -1.0 (очень позитивно) до 1.0 (очень токсично).
     */
    public double getToxicityScore(String text) {
        String lowerText = text.toLowerCase();
        double score = 0.0;

        for (String word : TOXIC_KEYWORDS) {
            if (lowerText.contains(word)) score += 0.3;
        }

        for (String word : POSITIVE_KEYWORDS) {
            if (lowerText.contains(word)) score -= 0.3;
        }

        // Ограничиваем диапазон [-1, 1]
        return Math.max(-1.0, Math.min(1.0, score));
    }
}