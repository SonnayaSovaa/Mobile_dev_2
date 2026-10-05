package ru.mirea.nagishevakv.backeryproject.presentation;

import android.content.Context;
import android.util.Log;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ToxicityClassifier {
    private static final String TAG = "ToxicityClassifier";
    private static final String MODEL_FILE = "model.tflite";
    private NLClassifier classifier;
    private final Context context;
    private final ExecutorService executorService;
    private volatile boolean isReady = false;

    public ToxicityClassifier(Context context) {
        this.context = context;
        this.executorService = Executors.newSingleThreadExecutor();
        initClassifier();
    }

    private void initClassifier() {
        executorService.execute(() -> {
            try {
                NLClassifier.NLClassifierOptions options = 
                        NLClassifier.NLClassifierOptions.builder()
                                .setBaseOptions(BaseOptions.builder().setNumThreads(4).build())
                                .build();
                classifier = NLClassifier.createFromFileAndOptions(context, MODEL_FILE, options);
                isReady = true;
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage(), e);
            }
        });
    }

    public boolean isReady() {
        return isReady;
    }

    public double getToxicityScore(String text) {
        if (!isReady || classifier == null) {
            return 0.0;
        }
        try {
            List<Category> results = classifier.classify(text);
            double toxicityScore = 0.0;
            double positiveScore = 0.0;
            for (Category category : results) {
                String label = category.getLabel().toLowerCase();
                float score = category.getScore();
                if (label.contains("toxic") || label.contains("neg") || label.equals("0") || label.equals("label_0")) {
                    if (score > toxicityScore) toxicityScore = score;
                } else if (label.contains("pos") || label.contains("neut") || label.equals("1") || label.equals("label_1")) {
                    if (score > positiveScore) positiveScore = score;
                }
            }
            if (toxicityScore == 0 && positiveScore == 0 && results.size() >= 2) {
                return results.get(0).getScore() - results.get(1).getScore();
            }
            return toxicityScore - positiveScore;
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            return 0.0;
        }
    }

    public void close() {
        if (classifier != null) {
            classifier.close();
        }
        executorService.shutdown();
    }
}
