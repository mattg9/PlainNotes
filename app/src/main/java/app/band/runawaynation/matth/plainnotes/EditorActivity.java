package app.band.runawaynation.matth.plainnotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;
    
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
        } else {
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
            
            Cursor cursor = getContentResolver().query(uri,
                               DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DbOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();
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
            case Intent.ACTION_EDIT:
                if (newText.length() == 0) {
                    //deleteNote();
                } else if (oldText.equals(newText)) {
                    setResult(RESULT_CANCELLED);
                } else {
                    updateNote(newText);
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
    
    private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.maketext(this, "Note updated", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }
}
