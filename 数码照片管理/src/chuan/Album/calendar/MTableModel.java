package chuan.Album.calendar;

import javax.swing.*;
import javax.swing.table.*;

public class MTableModel extends AbstractTableModel {
	private final String[] columnNames;
	private final Object[][] tableDatas;
	public MTableModel (String[] columnNames, Object[][] tableDatas) {
		super();
		this.columnNames = columnNames;
		this.tableDatas = tableDatas;
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return tableDatas.length;
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return tableDatas[rowIndex][columnIndex];
	}
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		tableDatas[rowIndex][columnIndex] = aValue;
	}
	@Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return JButton.class;
    }
}
