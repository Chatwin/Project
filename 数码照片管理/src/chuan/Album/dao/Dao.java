package chuan.Album.dao;

import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;

public class Dao extends BaseDao{
	
	private static final Dao dao;
	static {
		dao = new Dao();
	}
	
	public static Dao getInstance() {
		return dao;
	}
	
	//œ‡≤·
	public Vector<Object> selectAlbum(int id) {
		return super.selectOnlyNote("select * from tb_album where id=" + id);
	}
	
	public Vector<Vector<Object>> selectAlbums(int fatherId) {
		return super.selectSomeNote("select * from tb_album where father_id=" + fatherId);
	}
	
	public Vector<Object> selectChildAlbumId(int fatherId) {
		Vector<Object> childId = new Vector<Object>(
				selectSomeValue("select id from tb_album where father_id=" + fatherId));
		int index = 0;
		while (childId.size() != index) {
			int count = childId.size();
			for (int j = index; j < count; j ++) {
				childId.addAll((Collection<?>) selectSomeValue("select id from tb_album where father_id=" + childId.get(j)));
			}
			index = count;
		}
		return childId;
	}
	
	public int insertAlbum(int fatherId, String name) {
		Object maxId = super.selectOnlyValue("select max(id) from tb_album");
		int id = (maxId == null ? 1 : (Integer)maxId + 1);
		super.longHaul("insert into tb_album(id,father_id,name) values(" + id + "," + fatherId + ",'" + name + "')");
		return id;
	}
	
	public boolean updateAlbum(int id, String newName) {
        return super.longHaul("update tb_album set name='" + newName + "' where id=" + id);
    }

    public boolean deleteAlbum(int id) {
        return super.longHaul("delete from tb_album where id=" + id);
    }
    
    //’’∆¨
    public Vector<Object> selectPhoto(String photoName) {
    	return super.selectOnlyNote("select * from tb_photo where num='" + photoName + "'");
    }
    
    public Vector<Vector<Object>> selectPhoto(int albumId, String title, String date,
    		char compare, String note) {
    	if (date.length() == 0) {
    		return selectPhoto(albumId, title, "", note);
        } else {
            return selectPhoto(albumId, title, " date" + compare + "'" + date + "'", note);
        }
    }
    
    public Vector<Vector<Object>> selectPhoto(int albumId, String title, String startDate,
            String endDate, String note) {
        if (startDate.length() == 0) {
            return selectPhoto(albumId, title, "", note);
        } else {
            return selectPhoto(albumId, title, " date>='" + startDate + "' and date<='" + endDate + "'", note);
        }
    }
    
    public Vector<Vector<Object>> selectPhoto(int albumId, String title, String date,
            String note) {
        boolean isSelectAll = true;
        StringBuffer sqlBuffer = new StringBuffer("select * from tb_photo");
        if (albumId != 0) {
            isSelectAll = false;
            sqlBuffer.append(" where album_id in (");
            sqlBuffer.append(albumId);
            for (Iterator<Object> it = selectChildAlbumId(albumId).iterator(); it.hasNext();) {
                sqlBuffer.append(",");
                sqlBuffer.append(it.next());
            }
            sqlBuffer.append(")");
        }
        title = title.trim();
        if (title.length() > 0) {
            if (isSelectAll) {
                sqlBuffer.append(" where");
            } else {
                sqlBuffer.append(" and");
            }
            sqlBuffer.append(" title like '%");
            sqlBuffer.append(title.replace(' ', '%'));
            sqlBuffer.append("%'");
        }
        if (date.length() > 0) {
            if (isSelectAll) {
                sqlBuffer.append(" where");
            } else {
                sqlBuffer.append(" and");
            }
            sqlBuffer.append(date);
        }
        note = note.trim();
        if (note.length() > 0) {
            if (isSelectAll) {
                sqlBuffer.append(" where");
            } else {
                sqlBuffer.append(" and");
            }
            sqlBuffer.append(" note like '%");
            sqlBuffer.append(note.replace(' ', '%'));
            sqlBuffer.append("%'");
        }
        return super.selectSomeNote(sqlBuffer.toString());
    }

    public boolean insertPhoto(Object[] values) {
        System.out.println("insert into tb_photo values('" + values[0] + "'," + values[1] + ",'" + values[2] + "','" + values[3] + "','" + values[4] + "')");
        return longHaul("insert into tb_photo values('" + values[0] + "'," + values[1] + ",'" + values[2] + "','" + values[3] + "','" + values[4] + "')");
    }

    public boolean updatePhoto(String photoName, String title, String date,
            String note) {
        return longHaul("update tb_photo set title='" + title + "', date='" + date + "', note='" + note + "' where num='" + photoName + "'");
    }

    public boolean deletePhoto(String photoName) {
        return longHaul("delete from tb_photo where num='" + photoName + "'");
    }

    Vector<Vector<Object>> aa() {
        return selectSomeNote("select * from tb_album");
    }
}
