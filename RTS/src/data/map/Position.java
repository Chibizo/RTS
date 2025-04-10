package data.map;


public class Position {
	
	int line ;
	int column;
	
	public Position(int line,int column) {
		this.line=line;
		this.column=column;
	}	
	
	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	

	public void setLine(int line) {
		this.line = line;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "Block [line=" + line + ", column=" + column + "]";
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return column == other.column && line == other.line;
	}
	
}
