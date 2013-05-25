package chuan.Album.mwing;

//DefaultMutableTreeNode 是树数据结构中的通用节点。
import javax.swing.tree.DefaultMutableTreeNode;

public class MTreeNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private boolean load = false;
	
	public MTreeNode() {
		super();
	}
	
	public MTreeNode(Object userObject) {
		super(userObject, true);
		this.id = 0;
	}
	
	public MTreeNode(int id, Object userObject) {
		super(userObject, true);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isLoad() {
		return load;
	}
	
	public void setLoad(boolean load) {
		this.load = load;
	}
}
