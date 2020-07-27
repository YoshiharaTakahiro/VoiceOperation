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

        //音声認識用のインテントを作成

        //実行ボタンのクリックイベント
        outputBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //入力テキストの内容を出力結果にセット

            }
        });

        //音声操作ボタンのクリックイベント

    }

    //インテントから処理が戻ってきた時に実行されるメソッド
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //音声認識が正常に処理を完了した場合
        if(requestCode == REQUEST_VOICE_CODE && resultCode == RESULT_OK) {

            //認識した文字の解析を行うパターンを設定
            Pattern searchP = Pattern.compile("yourPattern"); // Web検索用
            Pattern mapP = Pattern.compile("yourPattern");  // マップ検索用

            //音声認識の結果がdataに入っているので取り出す（リスト形式で）
            ArrayList<String> voiceResList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //認識結果が一つ以上ある場合はテキストビューに結果を表示する
            if (voiceResList.size() > 0) {

                //一番最初にある認識結果を取得する


                //音声テキストを出力結果にセット


                //認識した文字の解析をする
                Matcher searchM = searchP.matcher("");
                Matcher mapM = mapP.matcher("");

                if (searchM.find()) {  //「○○を調べて」と発言した時

                    //「○○を調べて」の○○を取得する


                    //Androidにインストールされているブラウザを起動させる


                }else if (mapM.find()) {  //「○○に行きたい」と発言した時

                    try {
                        //「○○に行きたい」の○○を取得する

                        // URIに対応するためエンコードを行う
                        String mapTextEncode = URLEncoder.encode("", "UTF-8");

                        // 交通手段（徒歩）


                        // GoogleMapを起動させる
                        // ランドマーク検索

                        /*
                        // ナビゲーション検索
                        Uri uri = Uri.parse("google.navigation:q=" + mapTextEncode + "&mode="+trafficMode); //道案内
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage("com.google.android.apps.maps");
                        */


                    } catch (UnsupportedEncodingException e) {
                        // URLEncoderでエラーが発生した時
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
