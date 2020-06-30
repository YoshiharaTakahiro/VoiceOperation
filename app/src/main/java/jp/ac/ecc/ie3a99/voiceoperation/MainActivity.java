package jp.ac.ecc.ie3a99.voiceoperation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //オブジェクトの宣言
    EditText inputText;
    Button outputBt;
    TextView resText;
    FloatingActionButton voiceOpeFab;

    Intent voiceIntent;

    //音声認識の識別番号
    private static final int REQUEST_VOICE_CODE = 100;


    //画面が作成される時に実行されるメソッド
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                 //レイアウト読込

        inputText = findViewById(R.id.inputText);               //入力テキスト
        outputBt = findViewById(R.id.outputButton);             //実行ボタン
        resText = findViewById(R.id.resText);                   //出力結果
        voiceOpeFab = findViewById(R.id.voiceOperationFab);     //音声操作ボタン

        //音声認識用のインテントを作成
        voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPANESE.toString());  //認識する言語を指定（この場合は日本語）
        voiceIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);                 //認識する候補数の指定
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "話しかけてください");      //音声認識時に表示する案内を設定

        //実行ボタンのクリックイベント
        outputBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //入力テキストの内容を出力結果にセット
                resText.setText(inputText.getText());
            }
        });

        //音声操作ボタンのクリックイベント
        voiceOpeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //音声認識を開始
                startActivityForResult(voiceIntent, REQUEST_VOICE_CODE);
            }
        });
    }

    //インテントから処理が戻ってきた時に実行されるメソッド
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //音声認識が正常に処理を完了した場合
        if(requestCode == REQUEST_VOICE_CODE && resultCode == RESULT_OK) {

            //認識した文字の解析を行うパターンを設定
            Pattern searchP = Pattern.compile("を調べて"); // Web検索用
            Pattern mapP = Pattern.compile("に行きたい");  // マップ検索用

            //音声認識の結果がdataに入っているので取り出す（リスト形式で）
            ArrayList<String> voiceResList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //認識結果が一つ以上ある場合はテキストビューに結果を表示する
            if (voiceResList.size() > 0) {

                //一番最初にある認識結果を取得する
                String voiceText = voiceResList.get(0);

                //音声テキストを出力結果にセット
                resText.setText(voiceText);

                //認識した文字の解析をする
                Matcher searchM = searchP.matcher(voiceText);
                Matcher mapM = mapP.matcher(voiceText);

                if (searchM.find()) {  //「○○を調べて」と発言した時

                    //「○○を調べて」の○○を取得する
                    String searchText = voiceText.substring(0, searchM.start());

                    //Androidにインストールされているブラウザを起動させる
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, searchText);
                    startActivity(intent);

                }else if (mapM.find()) {  //「○○に行きたい」と発言した時

                    try {
                        //「○○に行きたい」の○○を取得する
                        String mapText = voiceText.substring(0, mapM.start());

                        String trafficMode = "w"; // 交通手段（徒歩）

                        // GoogleMapを起動させる
                        // Uri uri = Uri.parse("geo:0.0?q=" + URLEncoder.encode(mapText, "UTF-8")); //ランドマーク検索
                        Uri uri = Uri.parse("google.navigation:q=" + URLEncoder.encode(mapText, "UTF-8") + "&mode="+trafficMode); //道案内
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);

                    } catch (UnsupportedEncodingException e) {
                        // URLEncoderでエラーが発生した時
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
