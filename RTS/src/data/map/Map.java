package data.map;

public class Map {

	int lineCount;
	int columnCount;
	
	Position[][] block;
	
	public Map(int lineCount,int columnCount ) {
		System.out.println("Création de la carte avec dimensions : " + lineCount + "x" + columnCount);
		init(lineCount,columnCount);
		
		for(int lineIndex=0; lineIndex<lineCount;lineIndex++) {
			for(int columnIndex=0;columnIndex<columnCount;columnIndex++) {
				block[lineIndex][columnIndex]=new Position(lineIndex,columnIndex);
			}
		}
	}	
	public void init(int lineCount,int columnCount) {
		this.lineCount=lineCount;
		this.columnCount=columnCount;
		
		block=new Position[lineCount][columnCount];
	}
	
	
	public int getLineCount() {
		return lineCount;
	}
	public int getColumnCount() {
		return columnCount;
	}
	public Position[][] getBlock() {
		return block;
	}
	
	public Position getBlock(int line, int column) {
		return block[line][column];
	}

	public boolean isOnTop(Position position) {
		int line = position.getLine();
		return line == 0;
	}

	public boolean isOnBottom(Position position) {
		int line = position.getLine();
		return line == lineCount - 1;
	}

	public boolean isOnLeftBorder(Position position) {
		int column = position.getColumn();
		return column == 0;
	}

	public boolean isOnRightBorder(Position position) {
		int column = position.getColumn();
		return column == columnCount - 1;
	}

	public boolean isOnBorder(Position position) {
		return isOnTop(position) || isOnBottom(position) || isOnLeftBorder(position) || isOnRightBorder(position);
	}

	
	
	
}
