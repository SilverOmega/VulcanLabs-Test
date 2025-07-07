package service;

import model.Seat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CinemaService {
    private int rows;
    private int cols;
    private int minDistance;
    private int[][] layout;

    public void configure(int rows, int cols, int minDistance){
        this.rows = rows;
        this.cols = cols;
        this.minDistance = minDistance;
        this.layout = new int[rows][cols];
    }

    public synchronized boolean reserve(List<Seat> seats){
        for (Seat s : seats){
            if(!isSeatAvailable(s.getRow(), s.getCol())) return false;
        }
        for (Seat s : seats) layout[s.getRow()][s.getCol()] = 1;
        return true;
    }

    public synchronized void cancel(List<Seat> seats){
        for (Seat s : seats){
            layout[s.getRow()][s.getCol()] = 0;
        }
    }

    private boolean isSeatAvailable(int r, int c){
        if (layout[r][c] == 1) return false;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if (layout[i][j] == 1 && manhattan(i,j,r,c) < minDistance)
                    return false;
            }
        }
        return true;
    }

    public List<List<Seat>> getAvailableGroups(int groupSize){
        List<List<Seat>> result = new ArrayList<>();
        for(int i = 0; i< rows; i++){
            for (int j = 0; j <= cols - groupSize; j++){
                List<Seat> group = new ArrayList<>();
                boolean valid = true;
                for (int k = 0; k < groupSize; k++){
                    Seat s = new Seat(i, j + k);
                    if (!isSeatAvailable(s.getRow(), s.getCol())){
                        valid = false;
                        break;
                    }
                    group.add(s);
                }
                if (valid) result.add(group);
            }
        }
        return result;
    }
    private int manhattan(int x1, int y1, int x2, int y2){
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
