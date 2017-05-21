
# NotePad
我写的notepad有3个功能
=
1.时间戳
2.查询框
3.支持图片和视频资源
----
# 一.时间戳
### 1.在notelist.xml中添加textview来显示时间戳
<TextView
        android:id="@+id/tvDate"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
        
### 2.操作数据库
private static class NoteColumn {
        // id
        static final String ID = "_id";
        // 标题
        static final String TITLE = "title";
        // 内容
        static final String CONTENT = "content";
        // 创建时间
        static final String CREATE_TIME = "create_time";
        // 修改时间
        static final String LAST_MODIFY_TIME = "last_modify_time";
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table note(" +
                "_id integer primary key autoincrement," +
                "title text," +
                "content text," +
                "create_time text," +
                "last_modify_time text)");
    }
    
    public long updateNote(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(NoteColumn.TITLE, note.getTitle());
        cv.put(NoteColumn.CONTENT, note.getContent());
        cv.put(NoteColumn.LAST_MODIFY_TIME, DateFormat.format("yyyy-MM-dd HH:mm:ss", note.getLastModifyTime()).toString());

        String where = NoteColumn.ID + "=?";
        String[] whereValues = new String[]{String.valueOf(note.getId())};
        return getWritableDatabase().update(TABLE_NOTE, cv, where, whereValues);
    }

    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
  
    // 操作数据库
		db = new NotesDB(this);
		dbRead = db.getReadableDatabase();
		// 查询数据库并将数据显示在ListView上。
		// 建议使用CursorLoader，这个操作因为在UI线程，容易引起无响应错误
  	adapter = new SimpleCursorAdapter(this, R.layout.notes_list_cell, null,
				new String[] { NotesDB.COLUMN_NAME_NOTE_NAME,
						NotesDB.COLUMN_NAME_NOTE_DATE }, new int[] {
						R.id.tvName, R.id.tvDate });
public Cursor searchByTitle(String word) {
        String where = NoteColumn.TITLE + " like ?";
        String[] whereValues = new String[]{"%" + word + "%"};
        return getWritableDatabase().query(TABLE_NOTE, null, where, whereValues, null, null, null);
    }		setListAdapter(adapter);

		refreshNotesListView();

		findViewById(R.id.btnAddNote).setOnClickListener(
				btnAddNote_clickHandler);


	}
  
  显示时间戳
![date](https://github.com/katydid2009/NotePad/blob/master/screenshot/3.png)

#二.查询
### 1.在main.xml中先添加一个searchview
<SearchView
        android:id="@+id/searchView"
        android:layout_width="350dp"
        android:layout_height="50dp"
        />
### 2.操作数据库
/**
     * 查询所有的笔记
     * @return 一个 cursor(游标)，配合 SimpleCursorAdapter 使用
     */
    public Cursor queryAll() {
        return getWritableDatabase().query(TABLE_NOTE, null, null, null, null, null, null);
    }
    
    public Cursor searchByTitle(String word) {
        String where = NoteColumn.TITLE + " like ?";
        String[] whereValues = new String[]{"%" + word + "%"};
        return getWritableDatabase().query(TABLE_NOTE, null, where, whereValues, null, null, null);
    }
### 3.
public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);	SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			public boolean onQueryTextSubmit(String query) {
				mAdapter.swapCursor(notesDB.searchByTitle(query));
				return false;
			}


			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		// 监听 SearchView 的折叠/展开
		MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

			public boolean onMenuItemActionCollapse(MenuItem item) {
				mAdapter.swapCursor(notesDB.queryAll());
				return true;
			}


			public boolean onMenuItemActionExpand(MenuItem item) {
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
     
### 4.查询成功
![search](https://github.com/katydid2009/NotePad/blob/master/screenshot/2.png)

#三.调用手机相机拍照或者录像并可以将相片或者视频保存到notepad中
### 1.photoview.java
import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * 显示照片的Activity
 * 
 * @author TOPS
 * 
 */
public class PhotoViewer extends Activity {

	private ImageView iv;

	public static final String EXTRA_PATH = "path";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		iv = new ImageView(this);
		setContentView(iv);

		String path = getIntent().getStringExtra(EXTRA_PATH);
		if (path != null) {
			iv.setImageURI(Uri.fromFile(new File(path)));
		} else {
			finish();
		}
	}

}

### 2.videoview.java
public class VideoViewer extends Activity {

	private VideoView vv;

	public static final String EXTRA_PATH = "path";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		vv = new VideoView(this);
		vv.setMediaController(new MediaController(this));
		setContentView(vv);

		String path = getIntent().getStringExtra(EXTRA_PATH);
		if (path != null) {
			vv.setVideoPath(path);
		} else {
			finish();
		}
	}

}

![sc](https://github.com/katydid2009/NotePad/blob/master/screenshot/1.png)
