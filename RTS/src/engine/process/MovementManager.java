package engine.process;

import data.map.Map;
import data.map.Position;
import data.mobile.Unit;
import java.util.ArrayList;
import java.util.List;

public class MovementManager {
    
    private Map map;
    
    public MovementManager(Map map) {
        this.map = map;
    }
    
    public Position calculateNextStep(Unit unit, Position currentPosition, Position targetPosition) {
        if (isCloseToTarget(currentPosition, targetPosition, 1)) {
            return targetPosition;
        }
        
        boolean horizontalFirst = Math.abs(targetPosition.getColumn() - currentPosition.getColumn()) > 
                                  Math.abs(targetPosition.getLine() - currentPosition.getLine());
        
        List<Position> possibleMoves =addPossibleMoves( currentPosition, targetPosition, horizontalFirst);
        
        List<Position> validMoves = filterValidMoves(possibleMoves, unit);
        
        if (validMoves.isEmpty()) {
            return handleBlockedPath(unit, currentPosition, targetPosition);
        }
        
        
        return validMoves.get(0);
    }
    
    /**
     * Checks if two positions are close to each other
     */
    private boolean isCloseToTarget(Position pos1, Position pos2, int threshold) {
        return Math.abs(pos1.getLine() - pos2.getLine()) <= threshold &&
               Math.abs(pos1.getColumn() - pos2.getColumn()) <= threshold;
    }
    
    /**
     * Adds possible moves in the 8 directions, prioritized toward target
     */
    private List<Position> addPossibleMoves(Position current, Position target, boolean horizontalFirst) {
    	List<Position> moves=new ArrayList<Position>();
        int dLine = Integer.compare(target.getLine() - current.getLine(), 0);
        int dColumn = Integer.compare(target.getColumn() - current.getColumn(), 0);
        
        if (dLine != 0 && dColumn != 0) {
            moves.add(new Position(current.getLine() + dLine, current.getColumn() + dColumn));
        }
        
        if (horizontalFirst) {
            if (dColumn != 0) {
                moves.add(new Position(current.getLine(), current.getColumn() + dColumn));
            }
            if (dLine != 0) {
                moves.add(new Position(current.getLine() + dLine, current.getColumn()));
            }
        } else {
            if (dLine != 0) {
                moves.add(new Position(current.getLine() + dLine, current.getColumn()));
            }
            if (dColumn != 0) {
                moves.add(new Position(current.getLine(), current.getColumn() + dColumn));
            }
        }

        
        if (dLine != 0) moves.add(new Position(current.getLine() + dLine, current.getColumn() - dColumn));
        if (dColumn != 0) moves.add(new Position(current.getLine() - dLine, current.getColumn() + dColumn));

        
        return moves;
    }
    
   
    private List<Position> filterValidMoves(List<Position> moves, Unit unit) {
        List<Position> validMoves = new ArrayList<>();
        
        for (Position move : moves) {
            if (move.getLine() >= 0 && move.getLine() < map.getLineCount() &&
                move.getColumn() >= 0 && move.getColumn() < map.getColumnCount()) {                
                if (!map.isfull(move) && !map.isfullUnits(move)) {
                     validMoves.add(move);
                }
            }
        }
        
        return validMoves;
    }
    
    /**
     * Handles situations where the path is blocked
     */
    private Position handleBlockedPath(Unit unit, Position current, Position target) {
        int[] directions = {-1, 0, 1};
        List<Position> alternativeMoves = new ArrayList<>();
        
        for (int dLine : directions) {
            for (int dColumn : directions) {
                if (dLine != 0 && dColumn != 0) {
                
	                Position newPos = new Position(current.getLine() + dLine, current.getColumn() + dColumn);
	                
	                if ( newPos.getLine() < map.getLineCount() && newPos.getColumn() < map.getColumnCount() &&
	                    !map.isfull(newPos) && !map.isfullUnits(newPos)) {
	                    
	                    int currentDistance = calculateDistance(current, target);
	                    int newDistance = calculateDistance(newPos, target);
	                    
	                    if (newDistance <= currentDistance) {
	                        alternativeMoves.add(newPos);
	                    }
	                }
                }
            }
        }
        
        if (!alternativeMoves.isEmpty()) {
            return alternativeMoves.get(0);
        }
        
        return current;
    }
    
    /**
     * Calculates Manhattan distance between two positions
     */
    private int calculateDistance(Position p1, Position p2) {
        return Math.abs(p1.getLine() - p2.getLine()) + Math.abs(p1.getColumn() - p2.getColumn());
    }
    
    
}