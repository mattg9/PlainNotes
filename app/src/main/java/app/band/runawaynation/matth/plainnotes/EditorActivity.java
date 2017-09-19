package app.band.runawaynation.matth.plainnotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        editor = (EditText) findViewById(R.id.editText);
        
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        
        if(uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString("New Note"));
        }
    }
    
    private void finishEditing() {
        String newText = editor.getText().toString().trim();
        
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELLED);
                } else {
                    insertNote(newText);
                }
        }
        finish();
    }
    
    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }
    
    @Override
    private void onBackPressed() {
        finishEditing();
    }
}
